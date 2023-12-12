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

import it.wldt.adapter.digital.DigitalAdapter;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateEvent;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateRelationshipInstance;
import it.wldt.core.state.IDigitalTwinState;

/**
 * This class represents the WoDT Digital Twin Interface component of the Abstract Architecture.
 * It is implemented as a Digital Adapter for the WLDT Framework.
 */
public final class WoDTDigitalTwinInterface extends DigitalAdapter<Void> {

    /**
     * Default constructor.
     * @param id the id of the Digital Adapter.
     */
    public WoDTDigitalTwinInterface(final String id) {
        super(id, null);
    }

    @Override
    protected void onStateChangePropertyCreated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStateChangePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStateChangePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStatePropertyUpdated(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStatePropertyDeleted(final DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStateChangeActionEnabled(final DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeActionUpdated(final DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeActionDisabled(final DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeEventRegistered(final DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onStateChangeEventRegistrationUpdated(final DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onStateChangeEventUnregistered(final DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onDigitalTwinStateEventNotificationReceived(
            final DigitalTwinStateEventNotification<?> digitalTwinStateEventNotification
    ) {

    }

    @Override
    protected void onStateChangeRelationshipCreated(final DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {

    }

    @Override
    protected void onStateChangeRelationshipInstanceCreated(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {

    }

    @Override
    protected void onStateChangeRelationshipDeleted(final DigitalTwinStateRelationship<?> digitalTwinStateRelationship) {

    }

    @Override
    protected void onStateChangeRelationshipInstanceDeleted(
            final DigitalTwinStateRelationshipInstance<?> digitalTwinStateRelationshipInstance
    ) {

    }

    @Override
    public void onAdapterStart() {

    }

    @Override
    public void onAdapterStop() {

    }

    @Override
    public void onDigitalTwinSync(final IDigitalTwinState digitalTwinState) {

    }

    @Override
    public void onDigitalTwinUnSync(final IDigitalTwinState digitalTwinState) {

    }

    @Override
    public void onDigitalTwinCreate() {

    }

    @Override
    public void onDigitalTwinStart() {

    }

    @Override
    public void onDigitalTwinStop() {

    }

    @Override
    public void onDigitalTwinDestroy() {

    }
}
