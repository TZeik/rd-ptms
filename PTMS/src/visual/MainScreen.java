package visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.TableRow;
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
import logic.PathFinder;
import logic.Route;
import logic.Stop;

public class MainScreen extends Application{
    public Stop selectedStop;
    public Route selectedRoute;
    private Circle selectedNode;
    private Circle nodeSymbol;
    private Line selectedEdge;
    private Line edgeSymbol;
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
    private Button actionButton4;
    private Button actionButton5;
    
    ArrayList<Circle> graphNodes;
    ArrayList<Line> graphEdges;
    
    TableView<Stop> table;
    ObservableList<Stop> tableData;
    BorderPane symbolPane;
    
    Label infoLabel1;
    Label infoLabel2;
    Label infoLabel3;
    
    VBox infoButtonBox;
    
    //Definitions
    static final double circleRadius = 20;
    static final double buttonWidth = 300;
    
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
        graphPane.setOnMouseClicked(this::handleObjectClick);

        // Right Panel: Stop info/details
        infoPane = new VBox();
        infoPane.setPadding(new Insets(100,20,20,20));
        infoPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");
        infoPane.setPrefWidth(300); // Set a fixed width for the info pane
        
     // Add a BorderPane for the symbol display
        symbolPane = new BorderPane();
        symbolPane.setPrefSize(100, 200);
        symbolPane.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #666666;");
        
        // Info Symbol
        nodeSymbol = new Circle(100, 100, 20);
        edgeSymbol = new Line(70,50,170,135);
        edgeSymbol.setStrokeWidth(10);
        
        symbolPane.setCenter(nodeSymbol);
        
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

        // Stop Info Manager Buttons
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
        
        // Route Info Manager Buttons
        actionButton4 = new Button("Editar Ruta");
        actionButton4.setOnAction(e -> new EditRouteDialog(this).show());
        actionButton5 = new Button("Eliminar Ruta");
        actionButton5.setOnAction(e -> {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar " + selectedRoute.getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar " + selectedRoute.getLabel() + "?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.isPresent() && result.get() == yesButton) {
        		deleteRoute(selectedRoute);
        	}
        });
        
        
        // Button Propierties
        actionButton1.setPrefWidth(buttonWidth);
        actionButton2.setPrefWidth(buttonWidth);
        actionButton3.setPrefWidth(buttonWidth);
        actionButton4.setPrefWidth(buttonWidth);
        actionButton5.setPrefWidth(buttonWidth);
        
        infoButtonBox = new VBox(20); // Increased spacing between buttons
        infoButtonBox.setAlignment(Pos.CENTER);
        
        // Arrange labels and buttons in a vertical layout
        VBox labelsAndButtons = new VBox(10);
        labelsAndButtons.setAlignment(Pos.CENTER);
        labelsAndButtons.getChildren().addAll(infoLabel1, infoLabel2, infoLabel3, infoButtonBox);

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
        
        table.setRowFactory(tv -> {
            TableRow<Stop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) { // Single click
                    
                	Circle lastNode = selectedNode;
                    
                    selectedStop = row.getItem();
                    
                    selectNode(selectedStop.getVisual());
                            
                    
                    if(lastNode != null && lastNode.equals(selectedNode)) {
                    	selectNode(null);
                    	selectedStop = null;
                    }
                    
                    updateInfo();
                    
                } else if (!row.isEmpty() && event.getClickCount() == 2) { // Double click
                    selectedStop = row.getItem();
                    new EditStopDialog(this).show();
                }
            });
            return row;
        });
        
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
        searchPathButton.setOnAction(e -> waitForUserAction(3));

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
    	
    	if(arg == 3) {
    		graphPane.setCursor(Cursor.CROSSHAIR);
    		instructionLabel.setText("Haz click en la parada de destino o presiona ESC para cancelar");
            root.setTop(instructionBox);
            
            // Mouse click listener
            graphPane.setOnMouseClicked(this::handlePathFinder);

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
        graphPane.setOnMouseClicked(this::handleObjectClick);
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
        
        for (Circle node : PTMS.getInstance().getGraph().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getGraph().getStopbyVisual(node);
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
    
    private void handleObjectClick(MouseEvent event) {
    	
        double clickX = event.getX();
        double clickY = event.getY();
        
        Circle lastNode = selectedNode;
        Line lastRoute = selectedEdge;
        
        for (Circle node : PTMS.getInstance().getGraph().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getGraph().getStopbyVisual(node);
            }
        }
        
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        }
        
        for(Line edge : PTMS.getInstance().getGraph().getRouteVisuals()) {
        	if(edge.contains(clickX, clickY)) {
        		if(selectedStop == null) {
        			selectEdge(edge);
        			selectedRoute = PTMS.getInstance().getGraph().getRoutebyVisual(edge);
        		}
        	}
        }
        	
        if(lastRoute != null && lastRoute.equals(selectedEdge)) {
        	selectEdge(null);
        	selectedRoute = null;
        }
        
        updateInfo();

    };
    
    private void handlePathFinder(MouseEvent event) {
    	
    	double clickX = event.getX();
        double clickY = event.getY();
        Stop lastStop = selectedStop;
        
        Circle lastNode = selectedNode;
        
        for (Circle node : PTMS.getInstance().getGraph().getStopVisuals()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = PTMS.getInstance().getGraph().getStopbyVisual(node);
            }
        }
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        	endUserAction();
        }else {
        	searchPath(lastStop, selectedStop);
            endUserAction();
        }
    	
    };
    
    private void searchPath(Stop from, Stop to) {
    	
    	PathFinder pathfinder = new PathFinder(PTMS.getInstance().getGraph());
    	List<Stop> path;
    	
    	path = pathfinder.dijkstra(from, to);
    	pathfinder.printPath(path);
    	
    	/*path = pathfinder.bellmanFord(from, to);
    	pathfinder.printPath(path);
    	path = pathfinder.floydWarshall(from, to);
    	pathfinder.printPath(path);
    	path = pathfinder.kruskalMST(from, to);
    	pathfinder.printPath(path);*/
    	
    	selectNode(null);
    	updateGraph();
    	updateInfo();
    }
    
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
    
    private void selectEdge(Line edge) {
    	if(edge != null) selectedEdge = edge; else selectedEdge = null;
    	updateGraph();
    }
    
    private void updateInfo() {
		symbolPane.getChildren().clear();
    	// This Method updates the infoPane with the details of the selected stop
    	if((selectedStop != null && selectedNode != null) || (selectedRoute != null && selectedEdge != null)) {
    		
    		if(selectedStop != null && selectedNode != null) {
  
    			symbolPane.setCenter(nodeSymbol);
    			nodeSymbol.setStyle(selectedNode.getStyle());
        		infoButtonBox.getChildren().clear();
        		infoButtonBox.getChildren().addAll(actionButton1, actionButton2, actionButton3);
        		infoLabel1.setText("ID\n"+selectedStop.getId());
                infoLabel2.setText("Parada\n"+selectedStop.getLabel());
                infoLabel3.setText("Coordenadas\n\n"+"x ="+selectedStop.getX()+"\n"+"y ="+selectedStop.getY());
    		}
    		
    		if(selectedRoute != null && selectedEdge != null) {
    			
    			Polygon arrowhead = createArrowhead(edgeSymbol.getStartX(), edgeSymbol.getStartY(), edgeSymbol.getEndX(), edgeSymbol.getEndY());
    			edgeSymbol.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 8;");
    			arrowhead.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 15;");
    			symbolPane.getChildren().addAll(edgeSymbol, arrowhead);
    			infoButtonBox.getChildren().clear();
    			infoButtonBox.getChildren().addAll(actionButton4, actionButton5);
        		infoLabel1.setText("ID\n"+selectedRoute.getId());
                infoLabel2.setText("Ruta\n"+selectedRoute.getLabel());
                infoLabel3.setText("Distancia\n"+selectedRoute.getDistance());
    		}
    		
            
    	}else {
    		symbolPane.setCenter(nodeSymbol);
    		nodeSymbol.setStyle("-fx-fill: #a3a8ab;");
    		infoButtonBox.getChildren().clear();
    		infoLabel1.setText("Ninguna Parada/Ruta Seleccionada");
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
    	updateInfo();
    }
    
    public void addRoute(Route route) {
    	Line visual = new Line(route.getSrc().getX(), route.getSrc().getY(), route.getDest().getX(), route.getDest().getY());
        visual.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
        route.setVisual(visual);
        PTMS.getInstance().getGraph().addRoute(route);
        graphEdges.add(visual);
        selectNode(null);
        updateGraph();
        updateInfo();
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
    	updateInfo();
    }
    
    public void editRoute(Route route) {
    	PTMS.getInstance().getGraph().modifyRoute(route);
    	updateInfo();
    }
    
    public void deleteStop(Stop stop) {
    	 graphNodes.remove(stop.getVisual());
    	 PTMS.getInstance().getGraph().deleteStop(stop);
    	 selectNode(null);
    	 remakeRoutes();
    	 updateGraph();
    	 updateEdges();
    	 updateInfo();
    }
    public void deleteRoute(Route route) {
    	graphEdges.remove(route.getVisual());
    	PTMS.getInstance().getGraph().deleteRoute(route.getSrc(), route.getDest());
    	selectEdge(null);
    	updateEdges();
    	updateInfo();
    }
    
    private void remakeRoutes() {
    	graphEdges.clear();
    	Line currentRoute;
    	for(LinkedList<Stop> currentList : PTMS.getInstance().getGraph().getAdjList()) {
    		for(Stop stop : currentList) {
    			if(stop != currentList.get(0)) {
    				Stop start = currentList.get(0);
    				Stop end = stop;
    				currentRoute = PTMS.getInstance().getGraph().getRoute(start, end).getVisual();
    				currentRoute.setStartX(start.getX()); currentRoute.setStartY(start.getY());
    				currentRoute.setEndX(end.getX()); currentRoute.setEndY(end.getY());
        	        graphEdges.add(currentRoute);
    			}
    		}
    	}
    }
    
    private void updateGraph() {
    	tableData = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStops());
    	table.setItems(tableData);
    	table.refresh();
    	graphPane.getChildren().clear();
    	
    	// Adding nodes to the graphPane
    	graphPane.getChildren().addAll(graphNodes);
    	// Adding edges to the graphPane
    	for(Line l : graphEdges) {
   			Polygon arrowhead = createArrowhead(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
   			if(selectedEdge != null && selectedEdge.equals(l)) {
   				l.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 6;");
   				arrowhead.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 6;");
   			}else {
   				l.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
   				arrowhead.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
   			}
    	    // Add the edge and the arrowhead to the root pane
    		graphPane.getChildren().addAll(l, arrowhead);
   		}
    }

    private void updateEdges() {
    	graphEdges.clear();
    	for(Entry<String, Route> entry : PTMS.getInstance().getGraph().getRoutesMap().entrySet()) {
    		graphEdges.add(entry.getValue().getVisual());
    	}
    }
    
    private Polygon createArrowhead(double startX, double startY, double endX, double endY) {
        // Arrowhead size
        double arrowLength = 14;  // Length of the arrowhead
        double arrowWidth = 12;    // Width of the arrowhead

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

        return arrowhead;
    }
  
     
}
