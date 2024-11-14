package visual;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import logic.PTMS;
import logic.Stop;

public class MainScreen extends Application{
    public Stop selectedStop;
    private Circle selectedNode;
    private BorderPane root;
	private Pane graphPane;
    private VBox infoPane;
    private StackPane instructionBox;
    private StackPane blankBox;
    private Label instructionLabel;
    
    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Sistema de Transporte Público PTMS");

        // Left Panel: Menu
        VBox menuPane = createMenuPane();
        menuPane.setSpacing(20);

        // Center Panel: Graph view
        graphPane = new Pane();
        graphPane.setStyle("-fx-background-color: lightgray;");
        graphPane.setOnMouseClicked(this::handleNodeClick);

        // Right Panel: Stop info
        infoPane = new VBox();
        infoPane.setPadding(new Insets(10));
        
        
        // Setting up screen
        root = new BorderPane();
        root.setLeft(menuPane);
        root.setCenter(graphPane);
        root.setRight(infoPane);
        
        // Creating Instruction Box Label
        blankBox = new StackPane();
        blankBox.setPadding(new Insets(8.5));
        instructionBox = new StackPane();
        instructionLabel = new Label("Haz click para insertar la parada o presiona ESC para cancelar");
        instructionBox.getChildren().add(instructionLabel);
        root.setTop(blankBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenuPane() {
        VBox menuPane = new VBox();
        menuPane.setPadding(new Insets(10));
        menuPane.setSpacing(5);

        Button addNodeButton = new Button("Agregar Parada");
        addNodeButton.setPrefHeight(100);
        addNodeButton.setPrefWidth(125);
        addNodeButton.setOnAction(e -> new AddStopDialog(this).show());

        Button editNodeButton = new Button("Editar Parada");
        editNodeButton.setPrefHeight(100);
        editNodeButton.setPrefWidth(125);
        editNodeButton.setDisable(true);
        editNodeButton.setOnAction(e -> deleteNode());

        Button addEdgeButton = new Button("Agregar Ruta");
        addEdgeButton.setPrefHeight(100);	
        addEdgeButton.setPrefWidth(125);
        addEdgeButton.setOnAction(e -> new AddRouteDialog(this).show());

        Button editEdgeButton = new Button("Editar Ruta");
        editEdgeButton.setPrefHeight(100);
        editEdgeButton.setPrefWidth(125);
        editEdgeButton.setDisable(true);
        editEdgeButton.setOnAction(e -> deleteEdge());

        menuPane.getChildren().addAll(addNodeButton, editNodeButton, addEdgeButton, editEdgeButton);
        return menuPane;
    }
    
    public void waitForUserAction() {
    	
        graphPane.setCursor(Cursor.CROSSHAIR);
        root.setTop(instructionBox);
        
        // Mouse click listener
        graphPane.setOnMouseClicked(this::handleMouseClick);

        // Key press listener on the scene to detect 'N' key press
        graphPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
            	endUserAction();
            }
        });
    }
    
    private void endUserAction() {
    	selectedStop = null;
    	root.setTop(blankBox);
        graphPane.setCursor(Cursor.DEFAULT);
        graphPane.setOnMouseClicked(this::handleNodeClick);
    }
    
    private void handleMouseClick(MouseEvent event) {
    		selectedStop.setX(event.getX());
            selectedStop.setY(event.getY());
            addStop(selectedStop);
            endUserAction();
    }
    
    private void handleNodeClick(MouseEvent event) {
    	
        double clickX = event.getX();
        double clickY = event.getY();
        
        for (Circle node : PTMS.getInstance().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getStopbyVisual(node);
            }
        }
        
    };
    
    private void selectNode(Circle node) {
        // Deselect the previously selected node (if any)
        if (selectedNode != null) {
            selectedNode.setStyle("-fx-fill: #3498db;");  // Reset to original color
        }
        
     // Highlight the newly selected node
        selectedNode = node;  // Update the selected node reference
        selectedNode.setStyle("-fx-fill: #ea5a5a;");  // Change color to red (indicating selection)
    }
    
    private void addStop(Stop stop) {
    	Circle node = new Circle(stop.getX(), stop.getY(), 10);
    	node.setStyle("-fx-fill: #3498db;");
    	stop.setVisual(node);
       	PTMS.getInstance().getGraph().addStop(stop);
    	graphPane.getChildren().add(node);
    	selectedStop = null;
    }
    
    public void addRoute(Stop a, Stop b) {
    	Line route = new Line(a.getX(), a.getY(), b.getX(), b.getY());
        route.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 2;");
        route.setStroke(Color.BLACK);
        route.setStrokeWidth(2);
        Polygon arrowhead = createArrowhead(a.getX(), a.getY(), b.getX(), b.getY());

        // Add the edge and the arrowhead to the root pane
        graphPane.getChildren().addAll(route, arrowhead);
    }
    
    private Polygon createArrowhead(double startX, double startY, double endX, double endY) {
        // Arrowhead size
        double arrowLength = 10;  // Length of the arrowhead
        double arrowWidth = 8;    // Width of the arrowhead

        // Calculate the angle of the edge
        double angle = Math.atan2(endY - startY, endX - startX);

        // Create a polygon for the arrowhead (triangle)
        Polygon arrowhead = new Polygon();

        // Calculate the points for the arrowhead (triangle)
        double arrowX1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);
        double arrowY1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);
        double arrowX2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);
        double arrowY2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

        // Use the arrowWidth to spread the base of the arrowhead
        double baseOffsetX1 = arrowWidth * Math.cos(angle - Math.PI / 2); // Left base offset
        double baseOffsetY1 = arrowWidth * Math.sin(angle - Math.PI / 2);
        double baseOffsetX2 = arrowWidth * Math.cos(angle + Math.PI / 2); // Right base offset
        double baseOffsetY2 = arrowWidth * Math.sin(angle + Math.PI / 2);

        // Add the points for the arrowhead (tip of the arrow + two sides and base)
        arrowhead.getPoints().addAll(
                endX, endY,  // Tip of the arrowhead (this is the end point of the line)
                arrowX1 + baseOffsetX1, arrowY1 + baseOffsetY1,  // Left side of the arrowhead
                arrowX2 + baseOffsetX2, arrowY2 + baseOffsetY2   // Right side of the arrowhead
        );

        arrowhead.setFill(Color.BLACK);

        return arrowhead;
    }
    
    private void deleteNode() { /* Delete node logic */ }
    private void deleteEdge() { /* Delete edge logic */ }
    private void selectStop(Node node) { /* Select node logic */ }
    
    
    
}
