package com.example.logiccircuit;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Point2D;

import java.io.IOException;

public class DraggableNode extends AnchorPane {
    @FXML
    AnchorPane root_pane;
    private EventHandler  mContextDragOver;
    private EventHandler  mContextDragDropped;
    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);

    @FXML private Label title_bar;
    @FXML private Label close_button;
    @FXML private AnchorPane node_body;

    private final DraggableNode self;

    public DraggableNode() {
        self = this;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DraggableNode.fxml"));
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
        buildNodeDragHandlers();
    }

    public void buildNodeDragHandlers() {
        /*Our first handler detects mouse drag events on the title bar label at the top of the node.
         It sets the needed DragOver / DragDropped handlers, and begins drag operations.*/
        //drag detection for node dragging
        // TODO change this later
        node_body.setOnDragDetected ( new EventHandler <MouseEvent> () {

            @Override
            public void handle(MouseEvent event) {

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver (mContextDragOver);
                getParent().setOnDragDropped (mContextDragDropped);

                //begin drag ops
                mDragOffset = new Point2D(event.getX(), event.getY());

                relocateToPoint (new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData ("type", mType.toString());
                content.put(DragContainer.DragNode, container);

                startDragAndDrop (TransferMode.ANY).setContent(content);

                event.consume();
            }

        });

        mContextDragOver = new EventHandler <DragEvent> () {

            //dragover to handle node dragging in the right pane view
            @Override
            public void handle(DragEvent event) {

                event.acceptTransferModes(TransferMode.ANY);
                relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

                event.consume();
            }
        };

        //dragdrop for node dragging
        mContextDragDropped = new EventHandler <DragEvent> () {

            @Override
            public void handle(DragEvent event) {

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                event.setDropCompleted(true);

                event.consume();
            }
        };

//close button click
        close_button.setOnMouseClicked( new EventHandler <MouseEvent> () {

            @Override
            public void handle(MouseEvent event) {

                AnchorPane parent  = (AnchorPane) self.getParent();
                parent.getChildren().remove(self);
            }
        });
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
        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }


}
