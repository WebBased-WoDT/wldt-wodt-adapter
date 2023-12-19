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

package io.github.webbasedwodt.integration.wldt;

import it.wldt.adapter.physical.PhysicalAdapter;
import it.wldt.adapter.physical.PhysicalAssetAction;
import it.wldt.adapter.physical.PhysicalAssetDescription;
import it.wldt.adapter.physical.PhysicalAssetProperty;
import it.wldt.adapter.physical.PhysicalAssetRelationship;
import it.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceCreatedWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceDeletedWldtEvent;
import it.wldt.exception.EventBusException;
import it.wldt.exception.PhysicalAdapterException;

import java.util.logging.Logger;

/**
 * The physical adapter of the lamp Digital Twins used during tests.
 */
public final class LampPhysicalAdapter extends PhysicalAdapter {
    private static final String IS_ON_PROPERTY_KEY = "is-on-property-key";
    private static final String SWITCH_ACTION_KEY = "switch-action-key";
    private static final String LOCATED_INSIDE_RELATIONSHIP_KEY = "located-inside";
    private static final int EMULATION_WAIT_TIME = 2000;

    private boolean status;
    private final PhysicalAssetRelationship<String> locatedInside =
            new PhysicalAssetRelationship<>(LOCATED_INSIDE_RELATIONSHIP_KEY);

    /**
     * Default constructor.
     */
    public LampPhysicalAdapter() {
        super("lamp-physical-adapter");
        this.status = false;
    }

    @Override
    public void onIncomingPhysicalAction(final PhysicalAssetActionWldtEvent<?> physicalActionEvent) {
        if (physicalActionEvent != null && physicalActionEvent.getActionKey().equals(SWITCH_ACTION_KEY)) {
            try {
                //Create a new event to notify the variation of a Physical Property
                this.status = !this.status;
                final PhysicalAssetPropertyWldtEvent<Boolean> newStatus = new PhysicalAssetPropertyWldtEvent<>(
                        IS_ON_PROPERTY_KEY,
                        this.status
                );
                publishPhysicalAssetPropertyWldtEvent(newStatus);
                Logger.getLogger(LampPhysicalAdapter.class.getName()).info("ACTION called: Switch");
            } catch (EventBusException e) {
                Logger.getLogger(LampPhysicalAdapter.class.getName()).info(e.getMessage());
            }
        }
    }

    @Override
    public void onAdapterStart() {
        final PhysicalAssetDescription pad = new PhysicalAssetDescription();
        final PhysicalAssetProperty<Boolean> statusProperty = new PhysicalAssetProperty<>(IS_ON_PROPERTY_KEY, this.status);
        pad.getProperties().add(statusProperty);
        final PhysicalAssetAction switchAction = new PhysicalAssetAction(SWITCH_ACTION_KEY, "status.switch", "");
        pad.getActions().add(switchAction);
        pad.getRelationships().add(locatedInside);

        try {
            this.notifyPhysicalAdapterBound(pad);
        } catch (PhysicalAdapterException | EventBusException e) {
            Logger.getLogger(LampPhysicalAdapter.class.getName()).info(e.getMessage());
        }

        new Thread(this::emulatedDevice).start();
    }

    @Override
    public void onAdapterStop() {

    }

    private void emulatedDevice() {
        try {
            final var instance = locatedInside.createRelationshipInstance("http://example.com/house");
            publishPhysicalAssetRelationshipCreatedWldtEvent(
                    new PhysicalAssetRelationshipInstanceCreatedWldtEvent<>(
                            instance
                    )
            );
            Thread.sleep(EMULATION_WAIT_TIME);
            publishPhysicalAssetRelationshipDeletedWldtEvent(
                    new PhysicalAssetRelationshipInstanceDeletedWldtEvent<>(
                            instance
                    )
            );
            Thread.sleep(EMULATION_WAIT_TIME);
            publishPhysicalAssetRelationshipCreatedWldtEvent(
                    new PhysicalAssetRelationshipInstanceCreatedWldtEvent<>(
                            instance
                    )
            );
            while (true) {
                Thread.sleep(EMULATION_WAIT_TIME);
                publishPhysicalAssetPropertyWldtEvent(new PhysicalAssetPropertyWldtEvent<>(IS_ON_PROPERTY_KEY, status));
                Logger.getLogger(LampPhysicalAdapter.class.getName()).info("STATUS: " + this.status);
            }
        } catch (EventBusException | InterruptedException e) {
            Logger.getLogger(LampPhysicalAdapter.class.getName()).info(e.getMessage());
        }
    }
}
