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

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class FXGraph extends ScrollPane {

    static final double NODES_Z_OFFSET = 10;
    static final double EDGES_Z_OFFSET = 10;

    Pane contentPane;

    FXGraphModel model;
    FXGraphSelectionTool selectionTool;
    FXGraphZoomHandler zoomHandler;

    FXTool currentTool;

    private FXGraphMouseHandler mouseHandler;

    public FXGraph() {

        model = new FXGraphModel();

        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        contentPane = new Pane();
        setContent(contentPane);

        zoomHandler = new FXGraphZoomHandler(this);
        selectionTool = new FXGraphSelectionTool(contentPane, model, zoomHandler);
        mouseHandler = new FXGraphMouseHandler(this);

        mouseHandler.registerHandlerFor(contentPane);

        currentTool = selectionTool;
    }

    void updateEdgeNodesFor(FXNode aNode) {
        for (FXEdge theEdge : model.getEdges()) {
            if (theEdge.source == aNode || theEdge.destination == aNode) {
                contentPane.getChildren().remove(theEdge.getDisplayShape());
                theEdge.computeDisplayShape();

                Node theNode = theEdge.getDisplayShape();
                contentPane.getChildren().add(theNode);
                theNode.toBack();
            }
        }
    }

    public void updateSelectionInScene() {
        selectionTool.updateSelectionInScene();
    }

    public void addNode(FXNode aNode) {
        mouseHandler.registerNewNode(aNode);

        aNode.wrappedNode.setTranslateZ(NODES_Z_OFFSET);

        contentPane.getChildren().add(aNode.wrappedNode);

        model.registerNewNode(aNode);
    }

    public void addEdge(FXEdge aEdge) {
        aEdge.computeDisplayShape();
        Node theNode = aEdge.getDisplayShape();
        theNode.setTranslateZ(EDGES_Z_OFFSET);

        contentPane.getChildren().add(theNode);
        theNode.toBack();

        model.registerNewEdge(aEdge);
    }
}