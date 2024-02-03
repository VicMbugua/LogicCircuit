package com.example.logiccircuit;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RootLayout extends AnchorPane {
    @FXML
    SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML
    VBox left_pane;

    private EventHandler<DragEvent> mIconDragOverRoot = null;
    private EventHandler<DragEvent> mIconDragDropped = null;
    private EventHandler<DragEvent> mIconDragOverRightPane = null;


    private DragIcon mDragOverIcon = null;

    public RootLayout() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("RootLayout.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize(){
        //Add one icon that will be used for the drag-drop process
        //This is added as a child to the root anchorpane, so it can be visible
        //on both sides of the split pane.
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 3; i++) {

            DragIcon icn = new DragIcon();

            addDragDetection(icn);

            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
        }

        buildDragHandlers();
    }

    private void addDragDetection(DragIcon dragIcon){
        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                base_pane.setOnDragOver(mIconDragOverRoot);
                right_pane.setOnDragOver(mIconDragOverRightPane);
                right_pane.setOnDragOver(mIconDragDropped);

                DragIcon icn = (DragIcon) mouseEvent.getSource();

                mDragOverIcon.setType(icn.getType());
                mDragOverIcon.relocateToPoint(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.putString(icn.getType().toString());

                mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
                mDragOverIcon.setVisible(true);
                mDragOverIcon.setMouseTransparent(true);
                mouseEvent.consume();

            }
        });
    }

    public void buildDragHandlers(){
        mIconDragOverRoot = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Point2D p = right_pane.sceneToLocal(dragEvent.getSceneX(), dragEvent.getSceneY());

                if(!right_pane.boundsInLocalProperty().get().contains(p)){
                    mDragOverIcon.relocateToPoint(new Point2D(dragEvent.getSceneX(), dragEvent.getSceneY()));
                }

                dragEvent.consume();
            }
        };

        mIconDragOverRightPane = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
                mDragOverIcon.relocateToPoint(new Point2D(dragEvent.getSceneX(), dragEvent.getSceneY()));
                dragEvent.consume();
            }
        };

        mIconDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragEvent.setDropCompleted(true);

                right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

                mDragOverIcon.setVisible(false);

                dragEvent.consume();
            }
        };

        this.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

                mDragOverIcon.setVisible(false);

                dragEvent.consume();
            }
        });
    }
}
