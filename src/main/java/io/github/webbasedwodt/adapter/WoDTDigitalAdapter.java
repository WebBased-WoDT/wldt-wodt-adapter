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

import io.github.webbasedwodt.application.component.DTKGEngine;
import io.github.webbasedwodt.model.ontology.DTOntology;
import it.wldt.adapter.digital.DigitalAdapter;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateEvent;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateRelationshipInstance;
import it.wldt.core.state.IDigitalTwinState;

/**
 * This class represents the WLDT Framework Digital Adapter that allows to implement the WoDT Digital Twin layer
 * implementing the components of the Abstract Architecture.
 */
public final class WoDTDigitalAdapter extends DigitalAdapter<Void> {
    private final DTOntology ontology;
    private final DTKGEngine dtkgEngine;

    /**
     * Default constructor.
     * @param digitalAdapterId the id of the Digital Adapter
     * @param ontology the ontology to use for the semantics
     * @param digitalTwinUri the uri of the WoDT Digital Twin
     */
    public WoDTDigitalAdapter(final String digitalAdapterId, final DTOntology ontology, final String digitalTwinUri) {
        super(digitalAdapterId, null);
        this.ontology = ontology;
        this.dtkgEngine = new JenaDTKGEngine(digitalTwinUri);
    }

    @Override
    protected void onStateChangePropertyCreated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStateChangePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        this.ontology.convertPropertyValue(
                digitalTwinStateProperty.getKey(),
                digitalTwinStateProperty.getValue()
        ).ifPresent(triple ->
                this.dtkgEngine.addDigitalTwinPropertyUpdate(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    protected void onStateChangePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStatePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStatePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) { }

    @Override
    protected void onStateChangeActionEnabled(final DigitalTwinStateAction digitalTwinStateAction) { }

    @Override
    protected void onStateChangeActionUpdated(final DigitalTwinStateAction digitalTwinStateAction) { }

    @Override
    protected void onStateChangeActionDisabled(final DigitalTwinStateAction digitalTwinStateAction) { }

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
    }

    @Override
    protected void onStateChangeRelationshipInstanceCreated(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {
        this.ontology.convertRelationship(
                digitalTwinStateRelationshipInstance.getRelationshipName(),
                digitalTwinStateRelationshipInstance.getTargetId().toString()
        ).ifPresent(triple ->
                this.dtkgEngine.addRelationship(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    protected void onStateChangeRelationshipDeleted(final DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {
    }

    @Override
    protected void onStateChangeRelationshipInstanceDeleted(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {
        this.ontology.convertRelationship(
                digitalTwinStateRelationshipInstance.getRelationshipName(),
                digitalTwinStateRelationshipInstance.getTargetId().toString()
        ).ifPresent(triple ->
                this.dtkgEngine.removeRelationship(triple.getLeft(), triple.getRight())
        );
    }

    @Override
    public void onAdapterStart() {
        // start the components
    }

    @Override
    public void onAdapterStop() {
        // delete from registered platforms
    }

    @Override
    public void onDigitalTwinSync(final IDigitalTwinState digitalTwinState) {
        digitalTwinState.getRelationshipList().ifPresent(relationships ->
                observeDigitalTwinRelationships(relationships.stream().map(DigitalTwinStateRelationship::getName).toList())
        );
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
