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

import io.github.sanecity.wot.thing.Thing;
import io.github.webbasedwodt.application.component.DTDManager;
import io.github.webbasedwodt.integration.wldt.LampDTOntology;
import io.github.webbasedwodt.model.ontology.DTOntology;
import io.github.webbasedwodt.model.ontology.Property;
import io.github.webbasedwodt.model.ontology.WoDTVocabulary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link WoTDTDManager}.
 */
class WoTDTDManagerTest {
    private static final String SNAPSHOT_PROPERTY = "snapshot";
    private static final int TEST_PORT_NUMBER = 3000;
    private final DTOntology lampOntology = new LampDTOntology();
    private DTDManager dtdManager;

    @BeforeEach
    public void init() {
        this.dtdManager = new WoTDTDManager(
                "http://example/dt",
                this.lampOntology,
                "lampPA",
                TEST_PORT_NUMBER);
    }

    @Test
    @DisplayName("A DTD should carry the mandatory metadata even when empty")
    void testDTDMetadata() {
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertTrue(thingDescription.getProperties().containsKey(SNAPSHOT_PROPERTY));
        assertFalse(thingDescription.getProperty(SNAPSHOT_PROPERTY).getForms().isEmpty());
        assertTrue(thingDescription.getMetadata().containsKey(WoDTVocabulary.PHYSICAL_ASSET_ID.getUri()));
        assertTrue(thingDescription.getMetadata().containsKey(WoDTVocabulary.VERSION.getUri()));
    }

    @Test
    @DisplayName("A property should contain the mandatory information")
    void testCorrectInformationOnProperty() {
        final String propertyName = "is-on-property-key";
        this.dtdManager.addProperty(propertyName);
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        generalTestOnThingDescriptionProperty(thingDescription, propertyName);
        assertTrue(thingDescription.getProperty(propertyName).getOptionalProperties()
                .containsKey(WoDTVocabulary.AUGMENTED_INTERACTION.getUri()));
        assertFalse((Boolean) thingDescription.getProperty(propertyName)
                .getOptional(WoDTVocabulary.AUGMENTED_INTERACTION.getUri()));
    }

    @Test
    @DisplayName("It should be possible to delete a property")
    void testDeletionOfProperty() {
        final String propertyName = "is-on-property-key";
        this.dtdManager.addProperty(propertyName);
        assertTrue(this.dtdManager.removeProperty(propertyName));
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertFalse(thingDescription.getProperties().containsKey(propertyName));
    }

    @Test
    @DisplayName("A relationship should be represented in the DTD with the mandatory information")
    void testCorrectInformationOnRelationship() {
        final String relationshipName = "located-inside";
        this.dtdManager.addRelationship(relationshipName);
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        generalTestOnThingDescriptionProperty(thingDescription, relationshipName);
    }

    @Test
    @DisplayName("It should be possible to delete a relationship")
    void testDeletionOfRelationship() {
        final String relationshipName = "located-inside";
        this.dtdManager.addRelationship(relationshipName);
        assertTrue(this.dtdManager.removeRelationship(relationshipName));
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertFalse(thingDescription.getProperties().containsKey(relationshipName));
    }

    @Test
    @DisplayName("It should be possible to add an action")
    void testCorrectInformationOnAction() {
        final String actionName = "switch-action-key";
        this.dtdManager.addAction(actionName);
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertTrue(thingDescription.getActions().containsKey(actionName));
        assertFalse(thingDescription.getAction(actionName).getForms().isEmpty());
    }

    @Test
    @DisplayName("It should be possible to delete an action")
    void testDeletionOfAction() {
        final String actionName = "switch-action-key";
        this.dtdManager.addAction(actionName);
        assertTrue(this.dtdManager.removeAction(actionName));
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertFalse(thingDescription.getActions().containsKey(actionName));
    }

    @Test
    @DisplayName("It should be possible to add a Platform to which it is registered")
    void addPlatform() {
        final String platformUrl = "http://platform.it";
        this.dtdManager.addPlatform(platformUrl);
        final Thing<?, ?, ?> thingDescription = this.dtdManager.getDTD();
        assertTrue(thingDescription.getMetadata().containsKey("links"));
        assertEquals(1, ((List<?>) thingDescription.getMetadata("links")).size());
    }

    void generalTestOnThingDescriptionProperty(final Thing<?, ?, ?> thingDescription, final String propertyName) {
        assertTrue(thingDescription.getProperties().containsKey(propertyName));
        assertFalse(thingDescription.getProperty(propertyName).getObjectType().isEmpty());
        assertTrue(thingDescription.getProperty(propertyName).isReadOnly());
        assertTrue(thingDescription.getProperty(propertyName).isObservable());
        assertTrue(thingDescription.getProperty(propertyName).getOptionalProperties()
                .containsKey(WoDTVocabulary.DOMAIN_PREDICATE.getUri()));
        assertEquals(thingDescription.getProperty(propertyName).getOptional(WoDTVocabulary.DOMAIN_PREDICATE.getUri()),
                this.lampOntology.obtainProperty(propertyName).flatMap(Property::getUri).get());
    }
}
