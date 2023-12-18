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
import io.github.webbasedwodt.model.ontology.BlankNode;
import io.github.webbasedwodt.model.ontology.Individual;
import io.github.webbasedwodt.model.ontology.Literal;
import io.github.webbasedwodt.model.ontology.Node;
import io.github.webbasedwodt.model.ontology.Property;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.shared.Lock;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class provides an implementation of the {@link io.github.webbasedwodt.application.component.DTKGEngine} using
 * Apache Jena.
 */
public final class JenaDTKGEngine implements DTKGEngine {
    private final Model model;
    private final Resource digitalTwinResource;

    /**
     * Default constructor.
     * @param digitalTwinUri the uri of the Digital Twin for which this class creates the DTKG
     */
    public JenaDTKGEngine(final String digitalTwinUri) {
        this.model = ModelFactory.createDefaultModel();
        this.digitalTwinResource = this.model.createResource(digitalTwinUri);
    }

    @Override
    public void removeDigitalTwin() {
        this.writeModel(Model::removeAll);
    }

    @Override
    public void addDigitalTwinPropertyUpdate(final Property property, final Node newValue) {
        if (property.getUri().isPresent()) {
            this.writeModel(model -> {
                this.digitalTwinResource.removeAll(this.model.getProperty(property.getUri().get()));
                addProperty(this.digitalTwinResource, Pair.of(property, newValue));
            });
        }
    }

    @Override
    public void addRelationship(final Property relationshipPredicate, final Individual targetIndividual) {
        if (relationshipPredicate.getUri().isPresent()) {
            this.writeModel(model -> {
                this.digitalTwinResource.removeAll(this.model.getProperty(relationshipPredicate.getUri().get()));
                addProperty(this.digitalTwinResource, Pair.of(relationshipPredicate, targetIndividual));
            });
        }
    }

    @Override
    public boolean removeRelationship(final Property relationshipPredicate, final Individual targetIndividual) {
        if (relationshipPredicate.getUri().isPresent()
                && this.digitalTwinResource.hasProperty(this.model.getProperty(relationshipPredicate.getUri().get()))) {
            this.writeModel(model ->
                    this.digitalTwinResource.removeAll(this.model.getProperty(relationshipPredicate.getUri().get()))
            );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCurrentDigitalTwinKnowledgeGraph() {
        try {
            this.model.enterCriticalSection(Lock.READ);
            return RDFWriter.create().lang(Lang.TTL).source(this.model).asString();
        } finally {
            this.model.leaveCriticalSection();
        }
    }

    private void addProperty(final Resource resourceToAdd, final Pair<Property, Node> predicate) {
        final String propertyUri = predicate.getLeft().getUri().orElse("");
        final var property = this.model.createProperty(propertyUri);
        if (predicate.getRight() instanceof Property) {
            resourceToAdd.addProperty(
                    property,
                    model.createProperty(((Property) predicate.getRight()).getUri().orElse(""))
            );
        } else if (predicate.getRight() instanceof BlankNode) {
            resourceToAdd.addProperty(
                    property,
                    addProperties(this.model.createResource(), ((BlankNode) predicate.getRight()).getPredicates())
            );
        } else if (predicate.getRight() instanceof Literal<?>) {
            resourceToAdd.addLiteral(property, ((Literal<?>) predicate.getRight()).getValue());
        } else if (predicate.getRight() instanceof Individual) {
            resourceToAdd.addProperty(
                    property,
                    this.model.createResource(((Individual) predicate.getRight()).getUri().orElse(""))
            );
        }
    }

    private Resource addProperties(final Resource resourceToAdd, final List<Pair<Property, Node>> predicates) {
        predicates.forEach(predicate -> addProperty(resourceToAdd, predicate));
        return resourceToAdd;
    }

    private void writeModel(final Consumer<Model> modelConsumer) {
        this.model.enterCriticalSection(Lock.WRITE);
        modelConsumer.accept(this.model);
        this.model.leaveCriticalSection();
    }
}