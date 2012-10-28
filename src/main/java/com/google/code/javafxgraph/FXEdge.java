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

public class FXEdge {

    FXNode source;
    FXNode destination;
    Node displayShape;

    public FXEdge(FXNode aSource, FXNode aDestination) {
        source = aSource;
        destination = aDestination;
    }

    public Node getDisplayShape() {
        return  displayShape;
    }

    public void computeDisplayShape() {
        Path thePath = new Path();
        Bounds theSourceBounds = source.wrappedNode.getBoundsInParent();
        MoveTo theMoveTo = new MoveTo(theSourceBounds.getMinX() + theSourceBounds.getWidth() / 2, theSourceBounds.getMinY() + theSourceBounds.getHeight() / 2);
        thePath.getElements().add(theMoveTo);
        Bounds theDestinationBounds = destination.wrappedNode.getBoundsInParent();
        LineTo theLineTo = new LineTo(theDestinationBounds.getMinX() + theDestinationBounds.getWidth() / 2, theDestinationBounds.getMinY() + theDestinationBounds.getHeight() / 2);
        thePath.getElements().add(theLineTo);

        thePath.setStroke(Color.RED);
        thePath.setStrokeWidth(2);

        displayShape = thePath;
    }
}
