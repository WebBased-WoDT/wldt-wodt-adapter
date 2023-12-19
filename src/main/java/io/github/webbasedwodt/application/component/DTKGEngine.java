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

package io.github.webbasedwodt.application.component;

import io.github.webbasedwodt.model.ontology.Individual;
import io.github.webbasedwodt.model.ontology.Node;
import io.github.webbasedwodt.model.ontology.Property;

/**
 * This interface models the DTKGEngine component of the Abstract Architecture in a compatible way with the WLDT
 * Framework.
 */
public interface DTKGEngine {
    /**
     * Method that allows to signal the deletion or the stop of the underlying Digital Twin.
     */
    void removeDigitalTwin();

    /**
     * Add or update a Digital Twin property within the Digital Twin Knowledge Graph.
     * @param property the property to add/update
     * @param newValue the value of the property.
     */
    void addDigitalTwinPropertyUpdate(Property property, Node newValue);

    /**
     * Remove a Digital Twin property within the Digital Twin Knowledge Graph.
     * @param property the property to delete.
     * @return true if deleted, false if not-existent.
     */
    boolean removeProperty(Property property);

    /**
     * Add a relationship with another Digital Twin.
     * @param relationshipPredicate the associated predicate
     * @param targetIndividual the target individual
     */
    void addRelationship(Property relationshipPredicate, Individual targetIndividual);

    /**
     * Delete an existing relationship with another Digital Twin.
     * @param relationshipPredicate the associated predicate
     * @param targetIndividual the target individual.
     * @return true if correctly deleted, false if the relationship doesn't exist
     */
    boolean removeRelationship(Property relationshipPredicate, Individual targetIndividual);

    /**
     * Obtain the current status of the Digital Twin Knowledge Graph.
     * @return the current state of the DTKG
     */
    String getCurrentDigitalTwinKnowledgeGraph();
}
