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

import io.github.webbasedwodt.adapter.WoDTDigitalAdapter;
import it.wldt.core.engine.WldtEngine;
import it.wldt.exception.EventBusException;
import it.wldt.exception.ModelException;
import it.wldt.exception.WldtConfigurationException;
import it.wldt.exception.WldtRuntimeException;

import java.util.logging.Logger;

/**
 * Manual Integration test for the WoDT Digital Twin implemented with the WLDT Framework.
 */
public final class LampDT {
    private LampDT() { }

    /**
     * Main process.
     * @param args the args
     */
    public static void main(final String... args) {
        try {
            final WldtEngine digitalTwinEngine = new WldtEngine(new MirrorShadowingFunction(), "lamp-dt");
            digitalTwinEngine.addPhysicalAdapter(new LampPhysicalAdapter());
            digitalTwinEngine.addDigitalAdapter(new WoDTDigitalAdapter(
                    "wodt-dt-adapter",
                    new LampDTOntology(),
                    "https://example.com/dt")
            );
            digitalTwinEngine.startLifeCycle();
        } catch (ModelException
                 | EventBusException
                 | WldtRuntimeException
                 | WldtConfigurationException e) {
            Logger.getLogger(LampDT.class.getName()).info(e.getMessage());
        }
    }
}
