/*
 * Copyright (c) 2023. Andrea Giulianelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.webbasedwodt.adapter;

import io.github.webbasedwodt.application.component.DTDManager;
import io.github.webbasedwodt.application.component.DTKGEngine;
import io.github.webbasedwodt.application.component.PlatformManagementInterface;
import io.github.webbasedwodt.application.component.WoDTWebServer;
import it.wldt.adapter.digital.DigitalAdapter;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateEvent;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateRelationshipInstance;
import it.wldt.core.state.IDigitalTwinState;
import it.wldt.exception.EventBusException;
import it.wldt.exception.WldtDigitalTwinStateActionException;
import it.wldt.exception.WldtDigitalTwinStatePropertyException;

import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class represents the WLDT Framework Digital Adapter that allows to implement the WoDT Digital Twin layer
 * implementing the components of the Abstract Architecture.
 */
public final class WoDTDigitalAdapter extends DigitalAdapter<WoDTDigitalAdapterConfiguration> {
    private final DTKGEngine dtkgEngine;
    private final DTDManager dtdManager;
    private final WoDTWebServer woDTWebServer;
    private final PlatformManagementInterface platformManagementInterface;

    /**
     * Default constructor.
     * @param digitalAdapterId the id of the Digital Adapter
     * @param configuration the configuration of the Digital Adapter
     */
    public WoDTDigitalAdapter(final String digitalAdapterId, final WoDTDigitalAdapterConfiguration configuration) {
        super(digitalAdapterId, configuration);
        this.dtkgEngine = new JenaDTKGEngine(this.getConfiguration().getDigitalTwinUri());
        this.dtdManager = new WoTDTDManager(
                this.getConfiguration().getDigitalTwinUri(),
                this.getConfiguration().getOntology(),
                this.getConfiguration().getPhysicalAssetId(),
                this.getConfiguration().getPortNumber()
        );
        this.woDTWebServer = new WoDTWebServerImpl(
                this.getConfiguration().getPortNumber(),
                this.dtkgEngine,
                this.dtdManager,
                (actionName, body) -> {
                    try {
                        publishDigitalActionWldtEvent(actionName, body);
                        return true;
                    } catch (EventBusException e) {
                        Logger.getLogger(WoDTDigitalAdapter.class.getName())
                                .info("Impossible to forward action: " + e);
                        return false;
                    }
                });
        this.platformManagementInterface = new BasePlatformManagementInterface(
                this.getConfiguration().getDigitalTwinUri());
    }

    @Override
    protected void onStateChangePropertyCreated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        this.dtdManager.addProperty(digitalTwinStateProperty.getKey());
    }

    @Override
    protected void onStateChangePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        this.getConfiguration().getOntology().convertPropertyValue(
                digitalTwinStateProperty.getKey(),
                digitalTwinStateProperty.getValue()
        ).ifPresent(triple ->
                this.dtkgEngine.addDigitalTwinPropertyUpdate(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    protected void onStateChangePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        this.getConfiguration()
                .getOntology()
                .obtainProperty(digitalTwinStateProperty.getKey())
                .ifPresent(this.dtkgEngine::removeProperty);
        this.dtdManager.removeProperty(digitalTwinStateProperty.getKey());
    }

    @Override
    protected void onStatePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStatePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStateChangeActionEnabled(final DigitalTwinStateAction digitalTwinStateAction) {
        this.dtdManager.addAction(digitalTwinStateAction.getKey());
        this.dtkgEngine.addActionId(digitalTwinStateAction.getKey());
    }

    @Override
    protected void onStateChangeActionUpdated(final DigitalTwinStateAction digitalTwinStateAction) { }

    @Override
    protected void onStateChangeActionDisabled(final DigitalTwinStateAction digitalTwinStateAction) {
        this.dtdManager.removeAction(digitalTwinStateAction.getKey());
        this.dtkgEngine.removeActionId(digitalTwinStateAction.getKey());
    }

    @Override
    protected void onStateChangeEventRegistered(final DigitalTwinStateEvent digitalTwinStateEvent) { }

    @Override
    protected void onStateChangeEventRegistrationUpdated(final DigitalTwinStateEvent digitalTwinStateEvent) { }

    @Override
    protected void onStateChangeEventUnregistered(final DigitalTwinStateEvent digitalTwinStateEvent) { }

    @Override
    protected void onDigitalTwinStateEventNotificationReceived(
            final DigitalTwinStateEventNotification<?> digitalTwinStateEventNotification
    ) { }

    @Override
    protected void onStateChangeRelationshipCreated(final DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {
        this.dtdManager.addRelationship(digitalTwinStateRelationship.getName());
    }

    @Override
    protected void onStateChangeRelationshipInstanceCreated(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {
        this.getConfiguration().getOntology().convertRelationship(
                digitalTwinStateRelationshipInstance.getRelationshipName(),
                digitalTwinStateRelationshipInstance.getTargetId().toString()
        ).ifPresent(triple ->
                this.dtkgEngine.addRelationship(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    protected void onStateChangeRelationshipDeleted(final DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {
        this.dtdManager.removeRelationship(digitalTwinStateRelationship.getName());
    }

    @Override
    protected void onStateChangeRelationshipInstanceDeleted(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {
        this.getConfiguration().getOntology().convertRelationship(
                digitalTwinStateRelationshipInstance.getRelationshipName(),
                digitalTwinStateRelationshipInstance.getTargetId().toString()
        ).ifPresent(triple ->
                this.dtkgEngine.removeRelationship(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    public void onAdapterStart() {
        this.woDTWebServer.start();
        this.getConfiguration().getPlatformToRegister().forEach(platform -> {
            if (this.platformManagementInterface.registerToPlatform(platform, this.dtdManager.getDTD().toJson())) {
                this.dtdManager.addPlatform(platform);
            }
        });
    }

    @Override
    public void onAdapterStop() {
        this.platformManagementInterface.signalDigitalTwinDeletion();
    }

    @Override
    public void onDigitalTwinSync(final IDigitalTwinState digitalTwinState) {
        digitalTwinState.getRelationshipList().ifPresent(relationships ->
                observeDigitalTwinRelationships(relationships.stream()
                        .map(DigitalTwinStateRelationship::getName)
                        .collect(Collectors.toList()))
        );

        try {
            digitalTwinState.getPropertyList().ifPresent(properties ->
                    properties.forEach(property -> {
                        this.getConfiguration().getOntology().convertPropertyValue(
                                property.getKey(),
                                property.getValue()
                        ).ifPresent(triple ->
                                this.dtkgEngine.addDigitalTwinPropertyUpdate(triple.getLeft(), triple.getRight())
                        );
                        this.dtdManager.addProperty(property.getKey());
                    }));
            digitalTwinState.getRelationshipList().ifPresent(relationships ->
                    relationships.forEach(relationship -> this.dtdManager.addRelationship(relationship.getName())));
            digitalTwinState.getActionList().ifPresent(actions ->
                    actions.forEach(action -> {
                        this.dtdManager.addAction(action.getKey());
                        this.dtkgEngine.addActionId(action.getKey());
                    }));
        } catch (WldtDigitalTwinStatePropertyException | WldtDigitalTwinStateActionException e) {
            Logger.getLogger(WoDTDigitalAdapter.class.getName()).info("Error during loading: " + e);
        }
    }

    @Override
    public void onDigitalTwinUnSync(final IDigitalTwinState digitalTwinState) { }

    @Override
    public void onDigitalTwinCreate() { }

    @Override
    public void onDigitalTwinStart() { }

    @Override
    public void onDigitalTwinStop() { }

    @Override
    public void onDigitalTwinDestroy() { }
}
