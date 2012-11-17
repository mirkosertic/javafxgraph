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

import java.util.*;

public class FXGraphModel {

    private Map<Node, FXNode> nodes;
    private Set<FXEdge> edges;

    public FXGraphModel() {
        nodes = new HashMap<Node, FXNode>();
        edges = new HashSet<FXEdge>();
    }

    public void registerNewNode(FXNode aNode) {
        nodes.put(aNode.wrappedNode, aNode);
    }

    public Collection<FXNode> getNodes() {
        return nodes.values();
    }

    public void registerNewEdge(FXEdge aEdge) {
        edges.add(aEdge);
    }

    public Set<FXEdge> getEdges() {
        return edges;
    }
}
