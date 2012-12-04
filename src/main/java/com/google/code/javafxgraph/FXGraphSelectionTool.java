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

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class FXGraphSelectionTool extends FXTool {

    static final double SELECTION_Z_OFFSET = 20;

    private Pane owningControl;
    private FXGraphModel model;
    private FXGraphZoomHandler zoomHandler;

    private Set<FXNode> currentSelection;
    private Rectangle currentSelectionRectangle;
    private Timeline currentSelectionTimeline;
    private Rectangle interactiveSelectionRectangle;
    private Timeline interactiveSelectionTimeline;

    private boolean dragging;
    private double lastDragX;
    private double lastDragY;
    private boolean mousePressedOnNodeOrSelection;
    private FXEdgeWayPoint pressedWaypoint;

    FXGraphSelectionTool(Pane aOwningControl, FXGraphModel aModel, FXGraphZoomHandler aZoomHandler) {
        owningControl = aOwningControl;
        model = aModel;
        zoomHandler = aZoomHandler;

        currentSelection = new HashSet<FXNode>();
    }

    public void resetSelection() {
        currentSelection.clear();
    }

    public void add(FXNode aNode) {
        currentSelection.add(aNode);
    }

    public void updateSelectionInScene() {
        if (currentSelectionRectangle != null) {
            owningControl.getChildren().remove(currentSelectionRectangle);
            currentSelectionTimeline.stop();
        }

        if (currentSelection.size() > 0) {
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            for (FXNode theNode : currentSelection) {
                Bounds theBounds = theNode.wrappedNode.getBoundsInParent();
                minX = Math.min(minX, theBounds.getMinX());
                minY = Math.min(minY, theBounds.getMinY());
                maxX = Math.max(maxX, theBounds.getMaxX());
                maxY = Math.max(maxY, theBounds.getMaxY());
            }

            double startX = minX - 20;
            double startY = minY - 20;
            double width = maxX - minX + 40;
            double height = maxY - minY + 40;

            RectangleBuilder theBuilder = RectangleBuilder.create();
            theBuilder.x(startX).y(startY).width(width).height(height).strokeWidth(1).stroke(Color.BLACK).strokeDashArray(3.0, 7.0, 3.0, 7.0).fill(Color.TRANSPARENT).mouseTransparent(true);

            currentSelectionRectangle = theBuilder.build();
            currentSelectionRectangle.setTranslateZ(SELECTION_Z_OFFSET);
            owningControl.getChildren().add(currentSelectionRectangle);

            Duration theDuration = Duration.millis(1000 / 25);
            KeyFrame theOneFrame = new KeyFrame(theDuration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    currentSelectionRectangle.setStrokeDashOffset(currentSelectionRectangle.getStrokeDashOffset() + 1);
                }
            });

            currentSelectionTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(theOneFrame).build();
            currentSelectionTimeline.play();
        }

    }

    public Set<FXNode> getCurrentSelection() {
        return currentSelection;
    }

    public Rectangle getCurrentSelectionRectangle() {
        return currentSelectionRectangle;
    }

    public boolean contains(FXNode aNode) {
        return currentSelection.contains(aNode);
    }

    public void remove(FXNode aNode) {
        currentSelection.remove(aNode);
    }

    public boolean isSelectionMode() {
        return interactiveSelectionRectangle != null;
    }

    public void startSelectionAt(double aSceneX, double aSceneY) {
        RectangleBuilder theBuilder = RectangleBuilder.create();
        theBuilder.x(aSceneX).y(aSceneY).width(1).height(1).strokeWidth(1).stroke(Color.BLACK).strokeDashArray(3.0, 7.0, 3.0, 7.0).fill(Color.TRANSPARENT).mouseTransparent(true);

        interactiveSelectionRectangle = theBuilder.build();
        interactiveSelectionRectangle.setTranslateZ(SELECTION_Z_OFFSET);

        owningControl.getChildren().add(interactiveSelectionRectangle);

        Duration theDuration = Duration.millis(1000 / 25);
        KeyFrame theOneFrame = new KeyFrame(theDuration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                interactiveSelectionRectangle.setStrokeDashOffset(interactiveSelectionRectangle.getStrokeDashOffset() + 1);
            }
        });

        interactiveSelectionTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(theOneFrame).build();
        interactiveSelectionTimeline.play();

    }

    public void enhanceSelectionTo(double aSceneX, double aSceneY) {
        double width = aSceneX - interactiveSelectionRectangle.getX();
        double height = aSceneY - interactiveSelectionRectangle.getY();

        interactiveSelectionRectangle.setWidth(width);
        interactiveSelectionRectangle.setHeight(height);
    }

    public void endSelection() {

        for (FXNode theNode : model.getNodes()) {
            if (interactiveSelectionRectangle.intersects(theNode.wrappedNode.getBoundsInParent())) {
                add(theNode);
            }
        }

        interactiveSelectionTimeline.stop();
        interactiveSelectionTimeline = null;

        owningControl.getChildren().remove(interactiveSelectionRectangle);
        interactiveSelectionRectangle = null;

        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnNode(MouseEvent aEvent, FXNode aNode) {

        mousePressedOnNodeOrSelection = true;
        pressedWaypoint = null;

        if (!(aEvent.isControlDown() || aEvent.isShiftDown())) {
            resetSelection();
            add(aNode);
        } else {
            if (contains(aNode)) {
                remove(aNode);
            } else {
                add(aNode);
            }
        }
        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnEdge(MouseEvent aEvent, FXEdge aEdge) {

        mousePressedOnNodeOrSelection = true;
        pressedWaypoint = null;

        if (aEvent.isShiftDown()) {
            aEdge.addWayPoint(new FXEdgeWayPoint(aEdge, aEvent.getSceneX() / zoomHandler.currentZoomLevel, aEvent.getSceneY() / zoomHandler.currentZoomLevel));
        }
        resetSelection();
        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnEdgeWayPoint(MouseEvent aEvent, FXEdgeWayPoint aWayPoint) {
        mousePressedOnNodeOrSelection = false;
        pressedWaypoint = aWayPoint;
    }

    @Override
    public void mousePressed(MouseEvent aEvent) {

        mousePressedOnNodeOrSelection = false;
        pressedWaypoint = null;

        Rectangle theSelection = getCurrentSelectionRectangle();
        if (theSelection != null) {
            if (theSelection.contains(aEvent.getSceneX(), aEvent.getSceneY())) {
                mousePressedOnNodeOrSelection = true;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent aEvent) {
        if (mousePressedOnNodeOrSelection || pressedWaypoint != null) {
            if (!dragging) {
                dragging = true;
            } else {
                double movementX = aEvent.getSceneX() - lastDragX;
                double movementY = aEvent.getSceneY() - lastDragY;

                if (pressedWaypoint == null) {
                    for (FXNode theNode : getCurrentSelection()) {
                        theNode.translatePosition(movementX, movementY, zoomHandler.currentZoomLevel);
                    }
                } else {
                    pressedWaypoint.translatePosition(movementX / zoomHandler.currentZoomLevel, movementY / zoomHandler.currentZoomLevel, zoomHandler.currentZoomLevel);
                }


                updateSelectionInScene();
            }
            lastDragX = aEvent.getSceneX();
            lastDragY = aEvent.getSceneY();

        } else {

            if (!isSelectionMode()) {

                if (!(aEvent.isShiftDown() || aEvent.isControlDown())) {
                    resetSelection();
                }

                startSelectionAt(aEvent.getSceneX(), aEvent.getSceneY());
            } else {
                enhanceSelectionTo(aEvent.getSceneX(), aEvent.getSceneY());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent aEvent) {
        if (dragging) {
            dragging = false;

            updateSelectionInScene();
        }

        if (isSelectionMode()) {
            endSelection();
        }
    }
}