package com.example.logiccircuit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

            case not:
                getStyleClass().add("icon-not");
            break;

            case and:
                getStyleClass().add("icon-and");
            break;

            case or:
                getStyleClass().add("icon-or");
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
