/*
 * (C) Copyright 2012 JavaFXGraph (http://code.google.com/p/javafxgraph/).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package com.google.code.javafxgraph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class FXGraphMouseHandler {

    private FXGraph graph;

    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<ScrollEvent> scrolLEventHandler;

    public FXGraphMouseHandler(FXGraph aGraph) {

        graph = aGraph;

        mousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {

                Object theSource = aEvent.getSource();
                if (theSource instanceof Node) {
                    Node theNode = (Node) theSource;
                    Object theUserData = theNode.getUserData();

                    if (theUserData instanceof FXNode) {
                        graph.currentTool.mousePressedOnNode(aEvent, (FXNode) theUserData);
                    } else if (theUserData instanceof FXEdge) {
                        graph.currentTool.mousePressedOnEdge(aEvent, (FXEdge) theUserData);
                    } else if (theUserData instanceof FXEdgeWayPoint) {
                        graph.currentTool.mousePressedOnEdgeWayPoint(aEvent, (FXEdgeWayPoint) theUserData);
                    } else graph.currentTool.mousePressed(aEvent);
                } else {
                    graph.currentTool.mousePressed(aEvent);
                }

                aEvent.consume();
            }
        };

        mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {

                graph.currentTool.mouseDragged(aEvent);
                aEvent.consume();
            }
        };

        mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {

                graph.currentTool.mouseReleased(aEvent);
                aEvent.consume();
            }
        };

        scrolLEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent aEvent) {
                if (aEvent.getDeltaY() > 0) {
                    graph.zoomHandler.zoomOneStepOut();
                } else {
                    graph.zoomHandler.zoomOneStepIn();
                }
            }
        };

        aGraph.setOnScroll(scrolLEventHandler);
    }

    public void registerHandlerFor(Node aNode) {
        aNode.setOnMouseDragged(mouseDraggedEventHandler);
        aNode.setOnMousePressed(mousePressedEventHandler);
        aNode.setOnMouseReleased(mouseReleasedEventHandler);
        aNode.setOnScroll(scrolLEventHandler);
    }

    public void registerNewNode(FXNode aNode) {
        registerHandlerFor(aNode.wrappedNode);
    }

    public void registerNewEdge(FXEdge aEdge) {
        registerHandlerFor(aEdge.displayShape);
        for (Node theNode : aEdge.wayPointHandles.values()) {
            registerHandlerFor(theNode);
        }
    }
}
