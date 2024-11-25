package visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.stage.StageStyle;
import logic.PTMS;
import logic.Route;
import logic.Stop;

public class MainScreen extends Application{
    public Stop selectedStop;
    private Circle selectedNode;
    private Circle symbol;
    private BorderPane root;
	private Pane graphPane;
    private VBox infoPane;
    private StackPane instructionBox;
    private StackPane blankBox;
    private Label instructionLabel;
    
    private Button addNodeButton;
    private Button searchPathButton;
    private Button actionButton1;
    private Button actionButton2;
    private Button actionButton3;
    
    ArrayList<Circle> graphNodes;
    ArrayList<Line> graphEdges;
    
    TableView<Stop> table;
    ObservableList<Stop> tableData;
    
    Label infoLabel1;
    Label infoLabel2;
    Label infoLabel3;
    
    //Definitions
    static final double circleRadius = 20;
    
    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Sistema de Transporte Público PTMS");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        graphNodes = new ArrayList<>();
        graphEdges = new ArrayList<>();
        
        // Left Panel: Menu
        VBox menuPane = createMenuPane();
        menuPane.setSpacing(20);

        // Center Panel: Graph view
        graphPane = new Pane();
        graphPane.setStyle("-fx-background-color: lightgray;");
        graphPane.setOnMouseClicked(this::handleNodeClick);

        // Right Panel: Stop info/details
        infoPane = new VBox();
        infoPane.setPadding(new Insets(100,20,20,20));
        infoPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
        infoPane.setPrefWidth(300); // Set a fixed width for the info pane
        
     // Add a BorderPane for the symbol display
        BorderPane symbolPane = new BorderPane();
        symbolPane.setPrefSize(100, 200);
        symbolPane.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #666666;");
        
        // Info Symbol
        symbol = new Circle(100, 100, 20);
        symbolPane.setCenter(symbol);
        
        // Add labels for information
        infoLabel1 = new Label("");
        infoLabel2 = new Label("");
        infoLabel3 = new Label("");
        infoLabel1.setText("Ninguna Parada Seleccionada");
        infoLabel2.setText("");
        infoLabel3.setText("");
        
        infoLabel1.setPadding(new Insets(20, 0, 5, 0));
        infoLabel2.setPadding(new Insets(10, 0, 5, 0));
        infoLabel3.setPadding(new Insets(10, 0, 100, 0));

        // Add buttons below the labels
        actionButton1 = new Button("Agregar Ruta");
        actionButton1.setOnAction(e -> waitForUserAction(1));
        actionButton2 = new Button("Editar Parada");
        actionButton2.setOnAction(e -> new EditStopDialog(this).show());
        actionButton3 = new Button("Eliminar Parada");
        actionButton3.setOnAction(e -> {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar " + selectedStop.getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar " + selectedStop.getLabel() + "?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.isPresent() && result.get() == yesButton) {
        		deleteStop(selectedStop);
        	}
        });
        
        double buttonWidth = 300; // Desired width for buttons
        actionButton1.setPrefWidth(buttonWidth);
        actionButton2.setPrefWidth(buttonWidth);
        actionButton3.setPrefWidth(buttonWidth);
        
        actionButton1.setVisible(false);
        actionButton2.setVisible(false);
        actionButton3.setVisible(false);
        
        VBox buttonBox = new VBox(20); // Increased spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(actionButton1, actionButton2, actionButton3);
        
        // Arrange labels and buttons in a vertical layout
        VBox labelsAndButtons = new VBox(10);
        labelsAndButtons.setAlignment(Pos.CENTER);
        labelsAndButtons.getChildren().addAll(infoLabel1, infoLabel2, infoLabel3, buttonBox);

        // Add the symbol pane and labels/buttons to the info pane
        infoPane.getChildren().addAll(symbolPane, labelsAndButtons);
        
        StackPane bottomPane = new StackPane();
        bottomPane.setPadding(new Insets(8.5));
        
        // Creating Instruction Box Label
        blankBox = new StackPane();
        blankBox.setPadding(new Insets(8.5));
        instructionBox = new StackPane();
        instructionLabel = new Label("");
        instructionBox.getChildren().add(instructionLabel);
        
        // Setting up screen
        root = new BorderPane();
        root.setLeft(menuPane);
        root.setCenter(graphPane);
        root.setRight(infoPane);
        root.setTop(blankBox);
        root.setBottom(bottomPane);


        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenuPane() {
        VBox menuPane = new VBox();
        menuPane.setPadding(new Insets(100,10,10,10));
        menuPane.setSpacing(5);
        menuPane.setPrefWidth(300);
        
        table = new TableView<>();
        table.setPrefHeight(400); // Set preferred height
        table.setPrefWidth(275);  // Set preferred width
        
     // Create columns
        TableColumn<Stop, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setResizable(false);
        idColumn.setReorderable(false);
        idColumn.setPrefWidth(64);

        TableColumn<Stop, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        nameColumn.setResizable(false);
        nameColumn.setReorderable(false);
        nameColumn.setPrefWidth(136);
        
        TableColumn<Stop, Double> xColumn = new TableColumn<>("X");
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        xColumn.setResizable(false);
        xColumn.setReorderable(false);
        xColumn.setPrefWidth(40);
        
        TableColumn<Stop, Double> yColumn = new TableColumn<>("Y");
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        yColumn.setResizable(false);
        yColumn.setReorderable(false);
        yColumn.setPrefWidth(40);
        
        // Add columns to the table
        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(xColumn);
        table.getColumns().add(yColumn);
        

        // Add data to the table
        
        tableData = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStops());
        table.setItems(tableData);
        
        menuPane.getChildren().add(table);
        
        double buttonHeight = 50;
        double buttonWidth = 300;
        
        addNodeButton = new Button("Nueva Parada");
        addNodeButton.setPrefHeight(buttonHeight);
        addNodeButton.setPrefWidth(buttonWidth);
        addNodeButton.setOnAction(e -> waitForUserAction(0));
        
        searchPathButton = new Button("Buscar Camino");
        searchPathButton.setPrefHeight(buttonHeight);
        searchPathButton.setPrefWidth(buttonWidth);
        //searchPathButton.setOnAction(e -> );

        menuPane.getChildren().addAll(addNodeButton, searchPathButton);
        return menuPane;
    }
    
    public void waitForUserAction(int arg) {
    	
    	if(arg == 0) {
    		graphPane.setCursor(Cursor.CROSSHAIR);
    		instructionLabel.setText("Haz click para insertar la parada o presiona ESC para cancelar");
            root.setTop(instructionBox);
            
            // Mouse click listener
            graphPane.setOnMouseClicked(this::handleAddStop);

            // Key press listener on the scene to detect 'ESC' key press
            graphPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                	endUserAction();
                }
            });
    	}
    	
    	if(arg == 1) {
    		graphPane.setCursor(Cursor.CROSSHAIR);
    		instructionLabel.setText("Haz click en otra parada para conectarla o presiona ESC para cancelar");
            root.setTop(instructionBox);
            
            // Mouse click listener
            graphPane.setOnMouseClicked(this::handleAddRoute);

            // Key press listener on the scene to detect 'ESC' key press
            graphPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                	endUserAction();
                }
            });
    	}
    	
    	if(arg == 2) {
    		graphPane.setCursor(Cursor.CROSSHAIR);
    		instructionLabel.setText("Haz click para mover la parada o presiona ESC para cancelar");
            root.setTop(instructionBox);
            
            // Mouse click listener
            graphPane.setOnMouseClicked(this::handleMoveStop);

            // Key press listener on the scene to detect 'ESC' key press
            graphPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                	endUserAction();
                }
            });
    	}
    	
        
    }
    
    private void endUserAction() {
    	selectedStop = null;
    	root.setTop(blankBox);
        graphPane.setCursor(Cursor.DEFAULT);
        graphPane.setOnMouseClicked(this::handleNodeClick);
    }
    
    private void handleAddStop(MouseEvent event) {
        selectedStop = new Stop(null, null);
    	selectedStop.setX(event.getX());
    	selectedStop.setY(event.getY());
    	new AddStopDialog(this, selectedStop).show();
        endUserAction();
    }
    
    private void handleMoveStop(MouseEvent event) {
    	selectedStop.setX(event.getX());
    	selectedStop.setY(event.getY());
    	editStop(selectedStop);
        endUserAction();
    }
    
    private void handleAddRoute(MouseEvent event) {
    	
    	double clickX = event.getX();
        double clickY = event.getY();
        Stop lastStop = selectedStop;
        
        Circle lastNode = selectedNode;
        
        for (Circle node : PTMS.getInstance().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getStopbyVisual(node);
            }
        }
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        	endUserAction();
        }else {
        	new AddRouteDialog(this, lastStop, selectedStop).show();
            endUserAction();
        }
    };
    
    private void handleNodeClick(MouseEvent event) {
    	
        double clickX = event.getX();
        double clickY = event.getY();
        
        Circle lastNode = selectedNode;
        
        for (Circle node : PTMS.getInstance().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getStopbyVisual(node);
            }
        }
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        }
        
        updateInfo();

    };
    
    private void selectNode(Circle node) {
    	if(node != null) {
    		// Deselect the previously selected node (if any)
	        if (selectedNode != null) {
	            selectedNode.setStyle("-fx-fill: #3498db;");  // Reset to original color
	        }
	        
	     // Highlight the newly selected node
	        selectedNode = node;  // Update the selected node reference
	        selectedNode.setStyle("-fx-fill: #ea5a5a;");  // Change color to red (indicating selection)
	        
    	}else{
    		if(selectedNode != null) selectedNode.setStyle("-fx-fill: #3498db;");  // Reset to original color
    		selectedNode = null;
    	}
    }
    
    private void updateInfo() {
    	// This Method updates the infoPane with the details of the selected stop
    	if(selectedStop != null && selectedNode != null) {
    		symbol.setStyle(selectedNode.getStyle());
    		actionButton1.setVisible(true);
            actionButton2.setVisible(true);
            actionButton3.setVisible(true);
    		infoLabel1.setText("ID\n"+selectedStop.getId());
            infoLabel2.setText("Parada\n"+selectedStop.getLabel());
            infoLabel3.setText("Coordenadas\n\n"+"x ="+selectedStop.getX()+"\n"+"y ="+selectedStop.getY());
            
    	}else {
    		symbol.setStyle("-fx-fill: #a3a8ab;");
    		actionButton1.setVisible(false);
            actionButton2.setVisible(false);
            actionButton3.setVisible(false);
    		infoLabel1.setText("Ninguna Parada Seleccionada");
            infoLabel2.setText("");
            infoLabel3.setText("");
    	}
    }
    
    public void addStop(Stop stop) {
    	Circle node = new Circle(stop.getX(), stop.getY(), circleRadius);
    	node.setStyle("-fx-fill: #3498db;");
    	stop.setVisual(node);
       	PTMS.getInstance().getGraph().addStop(stop);
    	graphNodes.add(node);
    	selectNode(null);
    	updateGraph();
    }
    
    public void addRoute(Route route) {
    	Line visual = new Line(route.getSrc().getX(), route.getSrc().getY(), route.getDest().getX(), route.getDest().getY());
        visual.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 2;");
        visual.setStroke(Color.BLACK);
        visual.setStrokeWidth(2);
        route.setVisual(visual);
        PTMS.getInstance().getGraph().addRoute(route);
        graphEdges.add(visual);
        selectNode(null);
        updateGraph();
    }
    
    public void editStop(Stop stop) {
    	graphNodes.remove(stop.getVisual());
    	Circle node = new Circle(stop.getX(), stop.getY(), circleRadius);
    	node.setStyle("-fx-fill: #3498db;");
    	stop.setVisual(node);
    	PTMS.getInstance().getGraph().modifyStop(stop);
    	graphNodes.add(node);
    	selectNode(null);
    	remakeRoutes();
    	updateGraph();
    }
    
    public void deleteStop(Stop stop) {
    	 graphNodes.remove(stop.getVisual());
    	 PTMS.getInstance().getGraph().deleteStop(stop);
    	 selectNode(null);
    	 updateEdges();
    	 updateGraph();
    }
    public void deleteRoute() {
    	
    }
    
    private void remakeRoutes() {
    	graphEdges.removeAll(graphEdges);
    	Line currentRoute;
    	for(LinkedList<Stop> currentList : PTMS.getInstance().getGraph().getAdjList()) {
    		for(Stop stop : currentList) {
    			if(stop != currentList.get(0)) {
    				currentRoute = new Line(currentList.get(0).getX(), currentList.get(0).getY(), stop.getX(), stop.getY());
        			currentRoute.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 2;");
        	        currentRoute.setStroke(Color.BLACK);
        	        currentRoute.setStrokeWidth(2);
        	        graphEdges.add(currentRoute);
    			}
    		}
    	}
    }
    
    private void updateGraph() {
    	tableData = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStops());
    	table.setItems(tableData);
    	table.refresh();
    	graphPane.getChildren().removeAll(graphPane.getChildren());
    	
    	// Adding nodes to the graphPane
    		graphPane.getChildren().addAll(graphNodes);
    	// Adding edges to the graphPane
    		for(Line l : graphEdges) {
    			Polygon arrowhead = createArrowhead(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
    	        // Add the edge and the arrowhead to the root pane
    			graphPane.getChildren().addAll(l, arrowhead);
    		}
    }
    
    private void updateEdges() {
    	graphEdges.clear();
    	for(Entry<String, Route> entry : PTMS.getInstance().getGraph().getRoutes().entrySet()) {
    		graphEdges.add(entry.getValue().getVisual());
    	}
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
  
     
}
