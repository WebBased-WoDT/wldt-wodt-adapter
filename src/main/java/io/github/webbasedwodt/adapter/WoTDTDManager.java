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

import io.github.sanecity.wot.DefaultWot;
import io.github.sanecity.wot.WotException;
import io.github.sanecity.wot.thing.Context;
import io.github.sanecity.wot.thing.ExposedThing;
import io.github.sanecity.wot.thing.Thing;
import io.github.sanecity.wot.thing.form.Form;
import io.github.sanecity.wot.thing.form.Operation;
import io.github.sanecity.wot.thing.property.ExposedThingProperty;
import io.github.sanecity.wot.thing.property.ThingProperty;
import io.github.webbasedwodt.application.component.DTDManager;
import io.github.webbasedwodt.model.ontology.DTOntology;
import io.github.webbasedwodt.model.ontology.Property;
import io.github.webbasedwodt.model.ontology.WoDTVocabulary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This class provide an implementation of the {@link io.github.webbasedwodt.application.component.DTDManager} using
 * a WoT Thing Description to implement the Digital Twin Descriptor.
 */
final class WoTDTDManager implements DTDManager {
    private static final String THING_DESCRIPTION_CONTEXT = "https://www.w3.org/2019/wot/td/v1";
    private static final String VERSION = "1.0.0";
    private static final String SNAPSHOT_DTD_PROPERTY = "snapshot";
    private final String digitalTwinUri;
    private final String physicalAssetId;
    private final DTOntology ontology;
    private final int portNumber;
    private final List<WoDTDigitalTwinsPlatformLink> platformLinks;
    private final Map<String, ThingProperty<Object>> properties;
    private final Map<String, ThingProperty<Object>> relationships;
    private final Set<String> actions;


    /**
     * Default constructor.
     * @param digitalTwinUri the uri of the WoDT Digital Twin
     * @param ontology the ontology used to obtain the semantics
     * @param physicalAssetId the id of the associated physical asset
     * @param portNumber the port number where to offer the affordances
     */
    WoTDTDManager(final String digitalTwinUri,
                         final DTOntology ontology,
                         final String physicalAssetId,
                         final int portNumber) {
        this.digitalTwinUri = digitalTwinUri;
        this.ontology = ontology;
        this.physicalAssetId = physicalAssetId;
        this.portNumber = portNumber;
        this.platformLinks = Collections.synchronizedList(new ArrayList<>());
        this.properties = new HashMap<>();
        this.relationships = new HashMap<>();
        this.actions = new HashSet<>();
    }

    @Override
    public void addProperty(final String rawPropertyName) {
        this.createThingDescriptionProperty(rawPropertyName, true)
                .ifPresent(property -> this.properties.put(rawPropertyName, property));
    }

    @Override
    public boolean removeProperty(final String rawPropertyName) {
        return this.properties.remove(rawPropertyName) != null;
    }

    @Override
    public void addRelationship(final String rawRelationshipName) {
        this.createThingDescriptionProperty(rawRelationshipName, false)
                .ifPresent(relationship -> this.relationships.put(rawRelationshipName, relationship));
    }

    @Override
    public boolean removeRelationship(final String rawRelationshipName) {
        return this.relationships.remove(rawRelationshipName) != null;
    }

    @Override
    public void addAction(final String rawActionName) {
        this.actions.add(rawActionName);
    }

    @Override
    public boolean removeAction(final String rawActionName) {
        return this.actions.remove(rawActionName);
    }

    @Override
    public boolean addPlatform(final String platformUrl) {
        this.platformLinks.add(new WoDTDigitalTwinsPlatformLink(platformUrl));
        return true;
    }

    @Override
    public Set<String> getAvailableActionIds() {
        return new HashSet<>(this.actions);
    }

    @Override
    public Thing<?, ?, ?> getDTD() {
        try {
            final ExposedThing thingDescription = new DefaultWot().produce(new Thing.Builder()
                    .setId(this.digitalTwinUri)
                    .setObjectContext(new Context(THING_DESCRIPTION_CONTEXT))
                    .build()
            );
            this.initializeThingDescription(thingDescription);
            this.properties.forEach(thingDescription::addProperty);
            this.relationships.forEach(thingDescription::addProperty);
            this.actions.forEach(thingDescription::addAction);
            thingDescription.getActions().forEach((name, action) ->
                action.addForm(new Form.Builder()
                        .addOp(Operation.INVOKE_ACTION)
                        .setHref("http://localhost:" + this.portNumber + "/action/" + name)
                        .build())
            );
            return thingDescription;
        } catch (WotException e) {
            throw new IllegalStateException("Impossible to create the WoT DTD Manager in the current state", e);
        }
    }

    private void initializeThingDescription(final ExposedThing thingDescription) {
        thingDescription.addProperty(SNAPSHOT_DTD_PROPERTY, new ExposedThingProperty.Builder()
                .setReadOnly(true)
                .setObservable(true)
                .build());
        // Necessary to add the form afterward considering that the wot-servient library when adding a property
        // to an ExposedThing resets in an unexpected way the forms.
        final ThingProperty<?> snapshotProperty = thingDescription.getProperty(SNAPSHOT_DTD_PROPERTY);
        snapshotProperty.addForm(new Form.Builder()
                .addOp(Operation.OBSERVE_PROPERTY)
                .setHref("ws://localhost:" + this.portNumber + "/dtkg")
                .setSubprotocol("websocket")
                .build());
        thingDescription.getMetadata().put("links", this.platformLinks);
        thingDescription.getMetadata().put(WoDTVocabulary.PHYSICAL_ASSET_ID.getUri(), this.physicalAssetId);
        thingDescription.getMetadata().put(WoDTVocabulary.VERSION.getUri(), VERSION);
    }

    private Optional<ThingProperty<Object>> createThingDescriptionProperty(
            final String rawPropertyName,
            final boolean indicateAugmentation
    ) {
        final Optional<String> propertyValueType = this.ontology.obtainPropertyValueType(rawPropertyName);
        final Optional<String> domainPredicateUri = this.ontology.obtainProperty(rawPropertyName).flatMap(Property::getUri);

        if (propertyValueType.isPresent() && domainPredicateUri.isPresent()) {
            final Map<String, Object> metadata = new HashMap<>();
            metadata.put(WoDTVocabulary.DOMAIN_PREDICATE.getUri(), domainPredicateUri.get());
            if (indicateAugmentation) {
                metadata.put(WoDTVocabulary.AUGMENTED_INTERACTION.getUri(), false);
            }

            return Optional.of(new ThingProperty.Builder()
                    .setObjectType(propertyValueType.get())
                    .setReadOnly(true)
                    .setObservable(true)
                    .setOptionalProperties(metadata)
                    .build());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Class to describe a link to a WoDT Digital Twins Platform within the DTD.
     */
    private static class WoDTDigitalTwinsPlatformLink {
        private final String href;
        private final String rel;

        /**
         * Default constructor.
         * @param href the url of the WoDT Digital Twins Platform
         */
        WoDTDigitalTwinsPlatformLink(final String href) {
            this.href = href;
            this.rel = WoDTVocabulary.REGISTERED_TO_PLATFORM.getUri();
        }

        /**
         * Obtain the url of the WoDT Digital Twins Platform.
         * @return the url
         */
        public String getHref() {
            return this.href;
        }

        /**
         * Obtain the relationship of the link following the Web Linking Specification.
         * @return the relation of the link
         */
        public String getRel() {
            return this.rel;
        }
    }
}
