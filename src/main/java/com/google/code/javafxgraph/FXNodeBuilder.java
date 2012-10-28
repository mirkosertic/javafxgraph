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

public class FXNodeBuilder {

    private FXGraph graph;
    private Node node;
    private double positionX;
    private double positionY;

    public FXNodeBuilder(FXGraph aGraph) {
        graph = aGraph;
    }

    public FXNodeBuilder node(Node aNode) {
        node = aNode;
        return this;
    }

    public FXNodeBuilder x(double aX) {
        positionX = aX;
        return this;
    }

    public FXNodeBuilder y(double aY) {
        positionY = aY;
        return this;
    }

    public FXNode build() {
        FXNode theNode = new FXNode(graph, node);
        theNode.setPosition(positionX, positionY);

        graph.addNode(theNode);

        return theNode;
    }
}
