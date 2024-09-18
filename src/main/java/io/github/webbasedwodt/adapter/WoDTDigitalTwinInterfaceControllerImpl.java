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

import io.github.webbasedwodt.application.component.DTDManagerReader;
import io.github.webbasedwodt.application.component.DTKGEngineReader;
import io.github.webbasedwodt.application.component.WoDTDigitalTwinInterfaceController;
import io.github.webbasedwodt.model.ontology.WoDTVocabulary;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.HttpStatus;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Implementation of the controller for the WoDT Digital Twins Interface component of the Abstract Architecture.
 */
final class WoDTDigitalTwinInterfaceControllerImpl implements WoDTDigitalTwinInterfaceController {
    private final DTKGEngineReader dtkgEngine;
    private final DTDManagerReader dtdManager;
    private final BiFunction<String, String, Boolean> actionHandler;
    private final Set<WsContext> webSockets;

    /**
     * Default constructor.
     * @param dtkgEngine the DTKG Engine
     * @param dtdManager the DTD Manager
     * @param actionHandler the handler for actions
     */
    WoDTDigitalTwinInterfaceControllerImpl(
            final DTKGEngineReader dtkgEngine,
            final DTDManagerReader dtdManager,
            final BiFunction<String, String, Boolean> actionHandler
    ) {
        this.dtkgEngine = dtkgEngine;
        this.dtdManager = dtdManager;
        this.actionHandler = actionHandler;
        this.webSockets = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public void routeGetDigitalTwin(final Context context) {
        context.header(Header.LOCATION, "/dtkg");
        context.status(HttpStatus.SEE_OTHER);
    }

    @Override
    public void routeGetDigitalTwinKnowledgeGraph(final Context context) {
        final String dtkg = this.dtkgEngine.getCurrentDigitalTwinKnowledgeGraph();
        if (dtkg.isBlank()) {
            context.status(HttpStatus.NO_CONTENT);
        } else {
            context.status(HttpStatus.OK);
            context.header(Header.CONTENT_TYPE, "text/turtle");
            context.result(dtkg);
        }
        context.header(Header.LINK, "</dtd>; rel=\"" + WoDTVocabulary.HAS_DESCRIPTOR + "\"");
    }

    @Override
    public void routeGetDigitalTwinKnowledgeGraphEvents(final WsConfig wsContext) {
        wsContext.onConnect(context -> {
            context.enableAutomaticPings();
            this.webSockets.add(context);
            if (context.session.isOpen()) {
                context.send(this.dtkgEngine.getCurrentDigitalTwinKnowledgeGraph());
            }
        });
        wsContext.onClose(webSockets::remove);
    }

    @Override
    public void routeGetDigitalTwinDescriptor(final Context context) {
        final String dtd = this.dtdManager.getDTD().toJson();
        if (dtd.isBlank()) {
            context.status(HttpStatus.NO_CONTENT);
        } else {
            context.status(HttpStatus.OK);
            context.header(Header.CONTENT_TYPE, "application/td+json");
            context.result(dtd);
        }
        context.header(Header.LINK, "</dtkg>; rel=\"" + WoDTVocabulary.CURRENT_STATUS + "\"");
    }

    @Override
    public void routeHandleActionInvocation(final Context context) {
        final String action = context.pathParam("actionName");
        if (this.dtdManager.getAvailableActionIds().contains(action)) {
            context.status(
                    this.actionHandler.apply(action, context.body()) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST
            );
        } else {
            context.status(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void notifyNewDTKG(final String newDtkg) {
        this.webSockets.stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> session.send(newDtkg));
    }

    @Override
    public void registerRoutes(final Javalin app) {
        app.get("/", this::routeGetDigitalTwin);
        app.get("/dtkg", this::routeGetDigitalTwinKnowledgeGraph);
        app.get("/dtd", this::routeGetDigitalTwinDescriptor);
        app.ws("/dtkg", this::routeGetDigitalTwinKnowledgeGraphEvents);
        app.post("/action/{actionName}", this::routeHandleActionInvocation);
    }
}
