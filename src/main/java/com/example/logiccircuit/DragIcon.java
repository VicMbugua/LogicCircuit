package com.example.logiccircuit;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Point2D;

import java.io.IOException;

public class DragIcon extends AnchorPane {
    @FXML
    AnchorPane root_pane;

    private DragIconType mType = null;

    public DragIcon() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DragIcon.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {
    }

    public DragIconType getType() {
        return mType;
    }

    public void setType(DragIconType type) {

        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");

        switch (mType) {

            case blue:
                getStyleClass().add("icon-blue");
            break;

            case red:
                getStyleClass().add("icon-red");
            break;

            case green:
                getStyleClass().add("icon-green");
            break;

            default:
            break;

        }
    }

    public void relocateToPoint(Point2D p) {
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate(
                (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
                (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
        );
    }
}
