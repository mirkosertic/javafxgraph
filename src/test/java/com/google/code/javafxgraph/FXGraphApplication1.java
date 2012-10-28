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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FXGraphApplication1 extends Application {

    @Override
    public void start(Stage aStage) throws Exception {
        aStage.setMinWidth(800);
        aStage.setMinHeight(600);
        aStage.setTitle(getClass().getSimpleName());

        FXGraphBuilder theBuilder = FXGraphBuilder.create();
        FXGraph theGraph = theBuilder.build();

        List<FXNode> theNodes = new ArrayList<FXNode>();
        int centerX = 400;
        int centerY = 300;
        int numNodes = 20;
        int radius = 220;
        for (int i = 0; i < numNodes; i++) {
            Button button1 = new Button();
            button1.setText("Node " + i);

            double positionX = centerX + Math.cos(Math.toRadians(360 / numNodes * i)) * radius;
            double positionY = centerY + Math.sin(Math.toRadians(360 / numNodes * i)) * radius;

            FXNodeBuilder theNodeBuilder = new FXNodeBuilder(theGraph);
            theNodes.add(theNodeBuilder.node(button1).x(positionX).y(positionY).build());
        }

        for (int i = 0; i < theNodes.size() - 1; i++) {

            FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(theGraph);
            theEdgeBuilder.source(theNodes.get(i)).destination(theNodes.get(i+1)).build();
        }

        aStage.setScene(new Scene(theGraph));

        aStage.show();
    }

    public static void main(String[] args) {
        Application.launch(FXGraphApplication1.class, args);
    }
}
