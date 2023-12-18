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

import io.github.webbasedwodt.model.ontology.Individual;
import io.github.webbasedwodt.model.ontology.Literal;
import io.github.webbasedwodt.model.ontology.Node;
import io.github.webbasedwodt.model.ontology.Property;
import io.github.webbasedwodt.utils.TestingUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link JenaDTKGEngine}.
 */
class JenaDTKGEngineTest {
    private final List<Pair<Property, Node>> propertyList = List.of(
        Pair.of(new Property("https://healthcareontology.com/ontology#isBusy"), new Literal<>(true)),
        Pair.of(new Property("https://healthcareontology.com/ontology#hasFuelLevel"), new Literal<>(37.0))
    );

    private final List<Pair<Property, Node>> modifiedPropertyList = List.of(
            Pair.of(new Property("https://healthcareontology.com/ontology#isBusy"), new Literal<>(false)),
            Pair.of(new Property("https://healthcareontology.com/ontology#hasFuelLevel"), new Literal<>(39.0))
    );

    private final List<Pair<Property, Individual>> relationshipList = List.of(
        Pair.of(new Property("https://smartcityontology.com/ontology#isApproaching"), new Individual("intersection"))
    );

    private JenaDTKGEngine dtkgEngine;

    @BeforeEach
    public void init() {
        this.dtkgEngine = new JenaDTKGEngine("http://example.com/dt");
        this.propertyList.forEach(property ->
                this.dtkgEngine.addDigitalTwinPropertyUpdate(property.getLeft(), property.getRight())
        );
        this.relationshipList.forEach(relationship ->
                this.dtkgEngine.addRelationship(relationship.getLeft(), relationship.getRight())
        );
    }

    @Test
    @DisplayName("It should be possible to obtain the turtle representation of the Digital Twin")
    void testDTKGCreation() {
        assertEquals(
                TestingUtils.readResourceFile("DTKGWithRelationshipsTurtle.ttl").orElse(""),
                this.dtkgEngine.getCurrentDigitalTwinKnowledgeGraph()
        );
    }

    @Test
    @DisplayName("It should be possible to update a Property without seeing duplicates in the generated DTKG")
    void testDTKGPropertyUpdate() {
        this.modifiedPropertyList.forEach(property ->
                this.dtkgEngine.addDigitalTwinPropertyUpdate(property.getLeft(), property.getRight())
        );
        this.propertyList.forEach(property ->
                this.dtkgEngine.addDigitalTwinPropertyUpdate(property.getLeft(), property.getRight())
        );
        assertEquals(
                TestingUtils.readResourceFile("DTKGWithRelationshipsTurtle.ttl").orElse(""),
                this.dtkgEngine.getCurrentDigitalTwinKnowledgeGraph()
        );
    }

    @Test
    @DisplayName("When the Digital Twin is deleted, then the DTKG should be empty")
    void testDTKGDigitalTwinDeletion() {
        this.dtkgEngine.removeDigitalTwin();
        assertTrue(this.dtkgEngine.getCurrentDigitalTwinKnowledgeGraph().isEmpty());
    }

    @Test
    @DisplayName("It should be possible to delete an existing relationship")
    void testDTKGRelationshipDeletion() {
        assertTrue(this.dtkgEngine.removeRelationship(
                new Property("https://smartcityontology.com/ontology#isApproaching"),
                new Individual("intersection"))
        );
    }

    @Test
    @DisplayName("The request of deletion of a non-existent relationship should be correctly handled")
    void testDTKGNotExistentRelationshipDeletion() {
        assertFalse(this.dtkgEngine.removeRelationship(
                new Property("http://non-existent-uri"),
                new Individual("intersection"))
        );
    }
}
