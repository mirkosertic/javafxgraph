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

public class FXNode {

    Node wrappedNode;

    FXGraph owner;

    double positionX;

    double positionY;

    FXNode(FXGraph aOwner, Node aNode) {
        wrappedNode = aNode;
        owner = aOwner;
    }

    public void setPosition(double aPositionX, double aPositionY) {
        wrappedNode.relocate(aPositionX, aPositionY);
        positionX = aPositionX;
        positionY = aPositionY;

        owner.updateEdgeNodesFor(this);
    }

    public void translatePosition(double aMovementX, double aMovementY, double aZoomLevel) {
        wrappedNode.setLayoutX(wrappedNode.getLayoutX() + aMovementX);
        wrappedNode.setLayoutY(wrappedNode.getLayoutY() + aMovementY);

        positionX += aMovementX / aZoomLevel;
        positionY += aMovementY / aZoomLevel;

        owner.updateEdgeNodesFor(this);
    }

    public void setZoomLevel(double aZoomLevel) {
        wrappedNode.setLayoutX(positionX * aZoomLevel);
        wrappedNode.setLayoutY(positionY * aZoomLevel);
        wrappedNode.setScaleX(aZoomLevel);
        wrappedNode.setScaleY(aZoomLevel);

        owner.updateEdgeNodesFor(this);
    }
}
