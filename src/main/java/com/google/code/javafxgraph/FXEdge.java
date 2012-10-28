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
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.List;

public class FXEdge {

    FXNode source;
    FXNode destination;
    Node displayShape;
    List<FXEdgeWayPoint> wayPoints = new ArrayList<FXEdgeWayPoint>();

    public FXEdge(FXNode aSource, FXNode aDestination) {
        source = aSource;
        destination = aDestination;
    }

    public Node getDisplayShape() {
        return  displayShape;
    }

    public void addWayPoint(FXEdgeWayPoint aWayPoint) {
        wayPoints.add(aWayPoint);
    }

    public void removeWayPoint(FXEdgeWayPoint aWayPoint) {
        wayPoints.remove(aWayPoint);
    }

    public void computeDisplayShape() {
        Path thePath = new Path();

        // From the middle of the source
        Bounds theSourceBounds = source.wrappedNode.getBoundsInParent();
        MoveTo theMoveTo = new MoveTo(theSourceBounds.getMinX() + theSourceBounds.getWidth() / 2, theSourceBounds.getMinY() + theSourceBounds.getHeight() / 2);
        thePath.getElements().add(theMoveTo);

        // Thru the waypoints
        for (FXEdgeWayPoint theWayPoint : wayPoints) {
            LineTo theLineTo = new LineTo(theWayPoint.positionX, theWayPoint.positionY);
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
}
