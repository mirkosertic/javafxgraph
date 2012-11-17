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

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.RectangleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FXEdge {

    FXGraph graph;
    FXNode source;
    FXNode destination;
    Node displayShape;
    List<FXEdgeWayPoint> wayPoints = new ArrayList<FXEdgeWayPoint>();
    Map<FXEdgeWayPoint, Node> wayPointHandles = new HashMap<FXEdgeWayPoint, Node>();

    public FXEdge(FXGraph aGraph, FXNode aSource, FXNode aDestination) {
        graph = aGraph;
        source = aSource;
        destination = aDestination;
    }

    public void addWayPoint(FXEdgeWayPoint aWayPoint) {
        wayPoints.add(aWayPoint);

        graph.updateEdge(this, graph.zoomHandler.currentZoomLevel);
    }

    public void removeWayPoint(FXEdgeWayPoint aWayPoint) {
        wayPoints.remove(aWayPoint);

        graph.updateEdge(this, graph.zoomHandler.currentZoomLevel);
    }

    public Node compileDisplayShapeFor(FXEdgeWayPoint aWayPoint, double aZoomLevel) {
        RectangleBuilder theBuilder = RectangleBuilder.create();
        theBuilder.width(4).height(4).fill(Color.RED).stroke(Color.RED);
        Node theNode = theBuilder.build();
        theNode.setScaleX(aZoomLevel);
        theNode.setScaleY(aZoomLevel);
        theNode.setLayoutX((aWayPoint.positionX - 2) * aZoomLevel);
        theNode.setLayoutY((aWayPoint.positionY - 2) * aZoomLevel);
        theNode.setUserData(aWayPoint);
        return theNode;
    }

    public void computeDisplayShape(double aCurrentZoomLevel) {
        Path thePath = new Path();
        thePath.setUserData(this);

        // From the middle of the source
        Bounds theSourceBounds = source.wrappedNode.getBoundsInParent();
        MoveTo theMoveTo = new MoveTo(theSourceBounds.getMinX() + theSourceBounds.getWidth() / 2, theSourceBounds.getMinY() + theSourceBounds.getHeight() / 2);
        thePath.getElements().add(theMoveTo);

        wayPointHandles.clear();

        // Thru the waypoints
        for (FXEdgeWayPoint theWayPoint : wayPoints) {

            wayPointHandles.put(theWayPoint, compileDisplayShapeFor(theWayPoint, aCurrentZoomLevel));

            LineTo theLineTo = new LineTo(theWayPoint.positionX * aCurrentZoomLevel, theWayPoint.positionY * aCurrentZoomLevel);
            thePath.getElements().add(theLineTo);
        }

        // To the middle of the destination
        Bounds theDestinationBounds = destination.wrappedNode.getBoundsInParent();
        LineTo theLineTo = new LineTo(theDestinationBounds.getMinX() + theDestinationBounds.getWidth() / 2, theDestinationBounds.getMinY() + theDestinationBounds.getHeight() / 2);
        thePath.getElements().add(theLineTo);

        thePath.setStroke(Color.RED);
        thePath.setStrokeWidth(2);

        displayShape = thePath;
    }

    public void removeAllNodes(Pane aPane) {
        aPane.getChildren().remove(displayShape);
        aPane.getChildren().removeAll(wayPointHandles.values());
    }

    public void addAllNodes(Pane aPane, double aZIndex) {
        aPane.getChildren().add(displayShape);
        displayShape.setTranslateZ(aZIndex);
        displayShape.toBack();

        for (Node theNode : wayPointHandles.values()) {
            theNode.setTranslateZ(aZIndex);
            aPane.getChildren().add(theNode);
            theNode.toBack();
        }
    }
}
