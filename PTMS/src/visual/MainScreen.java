package visual;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Graph;
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
    private Pane terminalPane;
    private VBox textContainer;
    
    ComboBox<Graph> graphCombo;
    
    private ComboBox<String> algorythmsCombo;
    private ComboBox<Stop> initialStopCombo;
    private ComboBox<Stop> endStopCombo;
    
	private Label distanceDetail;
    private Label timeDetail;
    
    private Button addNodeButton;
    private Button searchPathButton;
    private Button actionButton1;
    private Button actionButton2;
    private Button actionButton3;
    private Button actionButton4;
    private Button actionButton5;
    private Button trailblazeButton;
    private Button editGraphButton;
    private Button deleteGraphButton;
    
    Map<Stop,Circle> graphNodes;
    Map<Route,Line> graphEdges;
    
    TableView<Stop> table;
    ObservableList<Stop> tableData;
    BorderPane symbolPane;
   
    Label infoDesc1;
    Label infoDesc2;
    Label infoDesc3;
    Label infoLabel1;
    Label infoLabel2;
    Label infoLabel3;
    
    VBox infoButtonBox;
    
    //Definitions
    static final double menuButtonHeight = 50;
    static final double menuButtonWidth = 300;
    static final double circleRadius = 20;
    static final double buttonWidth = 300;
    static final double comboWidth = 325;
    static final String dijkstra = "Dijkstra";
    static final String bellmanford = "Bellman Ford";
    static final String warshall = "Floyd Warshall";
    static final String kruskal = "Kruskal";

    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Sistema de Transporte Público PTMS");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
        
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

        // Add the symbol pane and labels/buttons to the info pane
        infoPane.getChildren().add(createObjectInfoPane());
        
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
        primaryStage.setOnCloseRequest(event -> handleWindowClose(event));
        primaryStage.setScene(scene);
        primaryStage.show();
        remakeStops();
        remakeRoutes();
        updateGraph();
    }

    private VBox createMenuPane() {
        VBox menuPane = new VBox();
        menuPane.setPadding(new Insets(100,10,10,10));
        menuPane.setSpacing(5);
        menuPane.setPrefWidth(300);
        
        graphCombo = new ComboBox<>();
        graphCombo.setPrefWidth(comboWidth);
        graphCombo.getItems().add(new Graph("Añadir","Grafo"));
        graphCombo.getItems().addAll(PTMS.getInstance().getMaps());
        graphCombo.setValue(PTMS.getInstance().getGraph());
        
        graphCombo.valueProperty().addListener(new ChangeListener<Graph>() {
            @Override
            public void changed(ObservableValue<? extends Graph> observable, Graph oldValue, Graph newValue) {
                if (newValue != null) { // Ensure a new value is selected
                    int selectedIndex = graphCombo.getSelectionModel().getSelectedIndex();

                    // Check if the selected index is the first position
                    if (selectedIndex == 0) {
                    	graphDialog();
                    } else {
                    	PTMS.getInstance().setGraph(graphCombo.getValue());
                    	selectNode(null);
                    	selectEdge(null);
                    	remakeStops();
                        remakeRoutes();
                        updateGraph();
                        updateInfo();
                    }
                }
            }
            
        });
        
        editGraphButton = new Button("Editar");
        editGraphButton.setPrefHeight(menuButtonHeight-50);
        editGraphButton.setPrefWidth(menuButtonWidth);
        editGraphButton.setOnAction(event -> {
            new GraphDialog(this, 1).show();
        });
        
        deleteGraphButton = new Button("Eliminar");
        deleteGraphButton.setPrefHeight(menuButtonHeight-50);
        deleteGraphButton.setPrefWidth(menuButtonWidth);
        deleteGraphButton.setOnAction(event -> {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar " + PTMS.getInstance().getGraph().getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar " + PTMS.getInstance().getGraph().getLabel() + "?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.isPresent() && result.get() == yesButton) {
        		deleteGraph(PTMS.getInstance().getGraph());
        	}
        });
        
        HBox menuGraphButtonPane = new HBox();
        menuGraphButtonPane.setPrefWidth(menuButtonWidth);
        menuGraphButtonPane.getChildren().addAll(editGraphButton, deleteGraphButton);
        menuGraphButtonPane.setSpacing(5);
        
        menuPane.getChildren().addAll(graphCombo, menuGraphButtonPane);
        
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
                    
                    selectNode(graphNodes.get(selectedStop));
                            
                    
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
        
        addNodeButton = new Button("Nueva Parada");
        addNodeButton.setPrefHeight(menuButtonHeight);
        addNodeButton.setPrefWidth(menuButtonWidth);
        addNodeButton.setOnAction(e -> waitForUserAction(0));
        
        searchPathButton = new Button("Buscar Camino");
        searchPathButton.setPrefHeight(menuButtonHeight);
        searchPathButton.setPrefWidth(menuButtonWidth);
        searchPathButton.setOnAction(e -> waitForUserAction(3));

        menuPane.getChildren().addAll(addNodeButton, searchPathButton);
        return menuPane;
    }
    
    private VBox createObjectInfoPane() {
    	VBox objectInfoPane = new VBox();
    	
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
        infoDesc1 = new Label("");
        infoLabel1 = new Label("Ninguna Parada/Ruta Seleccionada");
        infoDesc2 = new Label("");
        infoLabel2 = new Label("");
        infoDesc3 = new Label("");
        infoLabel3 = new Label("");
        
        infoDesc1.setPadding(new Insets(15, 0, 0, 0));
        infoDesc2.setPadding(new Insets(15, 0, 0, 0));
        infoDesc3.setPadding(new Insets(15, 0, 0, 0));
        infoLabel1.setPadding(new Insets(0, 0, 15, 0));
        infoLabel2.setPadding(new Insets(0, 0, 15, 0));
        infoLabel3.setPadding(new Insets(0, 0, 15, 0));

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
        labelsAndButtons.getChildren().addAll(infoDesc1, infoLabel1, infoDesc2, infoLabel2, infoDesc3, infoLabel3, infoButtonBox);
    	
        objectInfoPane.getChildren().addAll(symbolPane, labelsAndButtons);
        objectInfoPane.setAlignment(Pos.TOP_CENTER);
        
    	return objectInfoPane;
    }
    
    private VBox createPathFinderPane(Stop start, Stop end) {
    	VBox pathFinderPane = new VBox();
    	pathFinderPane.setPadding(new Insets(100,10,10,10));
    	pathFinderPane.setSpacing(10);
    	
    	Label titleLabel = new Label("Buscar camino");
    	
        algorythmsCombo = new ComboBox<>();
        initialStopCombo = new ComboBox<>();
        endStopCombo = new ComboBox<>();
        
        algorythmsCombo.setPrefWidth(comboWidth);
        initialStopCombo.setPrefWidth(comboWidth);
        endStopCombo.setPrefWidth(comboWidth);
        
    	Label distanceLabel = new Label("Distancia total recorrida:");
    	distanceDetail = new Label("");
        Label timeLabel = new Label("Tiempo total de viaje:");
        timeDetail = new Label("");
        
        terminalPane = createTerminal();
        
        trailblazeButton = new Button("Trazar camino");
        trailblazeButton.setPrefWidth(buttonWidth);
        
        algorythmsCombo.getItems().addAll(dijkstra, bellmanford, warshall, kruskal);

        initialStopCombo.getItems().addAll(PTMS.getInstance().getGraph().getStops());
        endStopCombo.getItems().addAll(PTMS.getInstance().getGraph().getStops());
        
        //ComboBox place-holders
        algorythmsCombo.setValue(dijkstra);
        initialStopCombo.setValue(start);
        endStopCombo.setValue(end);
        
        //Acción del botón
        trailblazeButton.setOnAction(event -> {
            searchPath(initialStopCombo.getValue(), endStopCombo.getValue(), algorythmsCombo.getValue());
            waitForUserAction(4);
        });
        
        pathFinderPane.getChildren().addAll(titleLabel, algorythmsCombo, initialStopCombo, endStopCombo, distanceLabel, distanceDetail, timeLabel, timeDetail, terminalPane, trailblazeButton);
        pathFinderPane.setAlignment(Pos.TOP_CENTER);
        
    	return pathFinderPane;
    }
    
    private Pane createTerminal() {
        // Crear un VBox para contener las líneas de texto
        textContainer = new VBox(5); // Espaciado de 5 entre líneas
            Text text = new Text("");
            textContainer.getChildren().add(text);

        // Envolver el VBox en un ScrollPane
        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setFitToWidth(true); // Asegurar que el ancho coincida con el contenedor
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Configurar el tamaño del ScrollPane
        scrollPane.setPrefSize(240, 300);

        // Crear el Pane principal y agregar el ScrollPane
        Pane terminalPane = new Pane(scrollPane);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(0);

        return terminalPane;
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
    	
    	if(arg == 4) {
    		instructionLabel.setText("Haz click en cualquier lugar o presiona ESC para continuar");
    		root.setTop(instructionBox);
    		
    		table.setDisable(true);
        	addNodeButton.setDisable(true);
        	searchPathButton.setDisable(true);
    		algorythmsCombo.setDisable(true);
    		initialStopCombo.setDisable(true);
    		endStopCombo.setDisable(true);
    		trailblazeButton.setDisable(true);
    		
    		graphPane.setOnMouseClicked(this::handleSearchedPath);
    		
    		graphPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                	resetTrailblaze();
                	selectNode(null);
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
        
        for (Circle node : graphNodes.values()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = getVisualStop(node);
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
        
        for (Circle node : graphNodes.values()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = getVisualStop(node);
            }
        }
        
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        }
        
        for(Line edge : graphEdges.values()) {
        	if(edge.contains(clickX, clickY)) {
        		if(selectedStop == null) {
        			selectEdge(edge);
        			selectedRoute = getVisualRoute(edge);
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
        
        for (Circle node : graphNodes.values()) {
            if (node.contains(clickX, clickY)) {
                selectNode(node);  // Select the clicked node
                selectedStop = getVisualStop(node);
            }
        }
        if(lastNode != null && lastNode.equals(selectedNode)) {
        	selectNode(null);
        	selectedStop = null;
        	endUserAction();
        }else {
            infoPane.getChildren().clear();
            infoPane.getChildren().add(createPathFinderPane(lastStop, selectedStop));
            endUserAction();
        }
    	
    };
    
    private void handleSearchedPath(MouseEvent event) {
    	resetTrailblaze();
    	selectNode(null);
    	endUserAction();
    };
    
    private void handleWindowClose(WindowEvent event) {
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Desea guardar antes de salir?");
        alert.setContentText("Los cambios sin guardar se perderán");

        // Show the alert and wait for user response
        ButtonType yesButton = new ButtonType("Sí");
    	ButtonType noButton = new ButtonType("No");
    	
    	alert.getButtonTypes().setAll(yesButton, noButton);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.isPresent() && result.get() == yesButton) {
    		PTMS.getInstance().savePTMS();
    	}
    	if(result.isPresent() && result.get() == noButton) {
    		
    	}
    };
    
    private void searchPath(Stop from, Stop to, String algorythm) {
    	
    	PathFinder pathfinder = new PathFinder(PTMS.getInstance().getGraph());
    	List<Stop> path;
    	
    	switch(algorythm) {
    	case dijkstra:
    		path = pathfinder.dijkstra(from, to);
    		break;
    	case bellmanford:
    		path = pathfinder.bellmanFord(from, to);
    		break;
    	case warshall:
    		path = pathfinder.floydWarshall(from, to);
    		break;
    	case kruskal:
    		path = pathfinder.kruskalMST(from, to);
    		break;
    	default:
    		path = pathfinder.dijkstra(from, to);
    		break;
    	}
    	
        textContainer.getChildren().clear();
		pathfinder.printPath(path);
        
    	if(path.isEmpty()) {
    		Text text = new Text("No se encontró un camino");
    		textContainer.getChildren().add(text);
    	}else {
        	int[] pathDetails = pathfinder.getPathDetails(path);
        	distanceDetail.setText(""+pathDetails[0]+" km");
            timeDetail.setText(""+pathDetails[1]+" min");
            
            for (String message : pathfinder.getRouteDetails(path)) {
                Text text = new Text(message);
                textContainer.getChildren().add(text);
            }

        	trailblaze(path);
    	}
    }
    
    private void trailblaze(List<Stop> path) {
    	
    	for(Stop s : path) {
    		if(graphNodes.get(s) != null) {
    			graphNodes.get(s).setStyle("-fx-fill: #f9b040;");
    			if(s.equals(path.getFirst())) graphNodes.get(s).setStyle("-fx-fill: #fec88e;");
    			if(s.equals(path.getLast())) graphNodes.get(s).setStyle("-fx-fill: #fc6355;");
    		}
    	}
    	
    	for(int i = 0; i < path.size() - 1; i++) {
    		Stop current = path.get(i);
    		Stop next = path.get(i+1);
    		for(Line l : graphEdges.values()) {
    			if(graphEdges.get(PTMS.getInstance().getGraph().getRoute(current, next)).equals(l)){
    				l.setStyle("-fx-stroke: #000000;");
    			}
    		}
    	}
    	
    	updateGraph();
    }
    
    private void resetTrailblaze() {
    	
    	table.setDisable(false);
    	addNodeButton.setDisable(false);
    	searchPathButton.setDisable(false);
    	algorythmsCombo.setDisable(false);
		initialStopCombo.setDisable(false);
		endStopCombo.setDisable(false);
		trailblazeButton.setDisable(false);
    	
    	for(Circle node : graphNodes.values()) {
    		node.setStyle("-fx-fill: #3498db;");
    	}
    	
    	for(Line l : graphEdges.values()) {
    		l.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
    	}
    	
    	updateGraph();
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
    	infoPane.getChildren().clear();
    	infoPane.getChildren().add(createObjectInfoPane());
		symbolPane.getChildren().clear();
		
    	// This Method updates the infoPane with the details of the selected stop
    	if((selectedStop != null && selectedNode != null) || (selectedRoute != null && selectedEdge != null)) {
    		
    		if(selectedStop != null && selectedNode != null) {
  
    			symbolPane.setCenter(nodeSymbol);
    			nodeSymbol.setStyle(selectedNode.getStyle());
        		infoButtonBox.getChildren().clear();
        		infoButtonBox.getChildren().addAll(actionButton1, actionButton2, actionButton3);
        		infoDesc1.setText("ID");
        		infoLabel1.setText(""+selectedStop.getId());
        		infoDesc2.setText("Parada");
                infoLabel2.setText(""+selectedStop.getLabel());
                infoDesc3.setText("Coordenadas");
                infoLabel3.setText("( "+selectedStop.getX()+" , "+selectedStop.getY()+" )");
    		}
    		
    		if(selectedRoute != null && selectedEdge != null) {
    			
    			Polygon arrowhead = createArrowhead(edgeSymbol.getStartX(), edgeSymbol.getStartY(), edgeSymbol.getEndX(), edgeSymbol.getEndY());
    			edgeSymbol.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 8;");
    			arrowhead.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 15;");
    			symbolPane.getChildren().addAll(edgeSymbol, arrowhead);
    			infoButtonBox.getChildren().clear();
    			infoButtonBox.getChildren().addAll(actionButton4, actionButton5);
    			infoDesc1.setText("ID");
        		infoLabel1.setText(""+selectedRoute.getId());
                infoDesc2.setText("Ruta");
        		infoLabel2.setText(""+selectedRoute.getLabel());
                infoDesc3.setText("Distancia");
        		infoLabel3.setText(""+selectedRoute.getDistance());
    		}
    		
            
    	}else {
    		symbolPane.setCenter(nodeSymbol);
    		nodeSymbol.setStyle("-fx-fill: #a3a8ab;");
    		infoButtonBox.getChildren().clear();
    		
    		infoDesc1 = new Label("");
            infoLabel1 = new Label("Ninguna Parada/Ruta Seleccionada");
            infoDesc2 = new Label("");
            infoLabel2 = new Label("");
            infoDesc3 = new Label("");
            infoLabel3 = new Label("");
    	}
    }
    
    private void graphDialog() {
    	new GraphDialog(this, 0).show();
    }
    
    public void addGraph(Graph graph) {
    	PTMS.getInstance().addGraph(graph);
    	PTMS.getInstance().setGraph(graph);
    	graphCombo.setValue(null);
    	graphCombo.getItems().clear();
    	graphCombo.getItems().add(new Graph("Añadir","Grafo"));
        graphCombo.getItems().addAll(PTMS.getInstance().getMaps());
        graphCombo.setValue(PTMS.getInstance().getGraph());
    	selectNode(null);
    	selectEdge(null);
    	remakeStops();
    	remakeRoutes();
    	updateInfo();
    	updateGraph();
    }
    
    public void addStop(Stop stop) {
    	Circle node = new Circle(stop.getX(), stop.getY(), circleRadius);
    	node.setStyle("-fx-fill: #3498db;");
       	PTMS.getInstance().getGraph().addStop(stop);
    	graphNodes.put(stop, node);
    	selectNode(null);
    	updateInfo();
    	updateGraph();
    }
    
    public void addRoute(Route route) {
    	Line visual = new Line(route.getSrc().getX(), route.getSrc().getY(), route.getDest().getX(), route.getDest().getY());
        visual.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
        PTMS.getInstance().getGraph().addRoute(route);
        graphEdges.put(route, visual);
        selectNode(null);
        updateInfo();
        updateGraph();
    }
    
    public void editGraph(Graph graph) {
    	PTMS.getInstance().editGraph(graph);
    	graphCombo.setValue(null);
    	graphCombo.getItems().clear();
    	graphCombo.getItems().add(new Graph("Añadir","Grafo"));
        graphCombo.getItems().addAll(PTMS.getInstance().getMaps());
        graphCombo.setValue(PTMS.getInstance().getGraph());
    	selectNode(null);
    	selectEdge(null);
    	remakeStops();
    	remakeRoutes();
    	updateInfo();
    	updateGraph();
    }
    
    public void editStop(Stop stop) {
    	graphNodes.remove(stop);
    	Circle node = new Circle(stop.getX(), stop.getY(), circleRadius);
    	node.setStyle("-fx-fill: #3498db;");
    	PTMS.getInstance().getGraph().modifyStop(stop);
    	graphNodes.put(stop, node);
    	selectNode(null);
    	remakeRoutes();
    	updateInfo();
    	updateGraph();
    }
    
    public void editRoute(Route route) {
    	PTMS.getInstance().getGraph().modifyRoute(route);
    	updateInfo();
    }
    
    public void deleteGraph(Graph graph) {
    	PTMS.getInstance().removeGraph(graph);
    	if(PTMS.getInstance().getMaps().isEmpty()) {
    		Graph newGraph = new Graph(PTMS.getInstance().generateGraphID(), "Nuevo Mapa");
    		PTMS.getInstance().addGraph(newGraph);
    		PTMS.getInstance().setGraph(newGraph);
    		graphCombo.setValue(null);
        	graphCombo.getItems().clear();
        	graphCombo.getItems().add(new Graph("Añadir","Grafo"));
            graphCombo.getItems().addAll(PTMS.getInstance().getMaps());
            graphCombo.setValue(PTMS.getInstance().getGraph());
            selectNode(null);
        	selectEdge(null);
        	remakeStops();
        	remakeRoutes();
        	updateInfo();
        	updateGraph();
    	}else {
    		PTMS.getInstance().setGraph(PTMS.getInstance().getMaps().getFirst());
    		graphCombo.setValue(null);
        	graphCombo.getItems().clear();
        	graphCombo.getItems().add(new Graph("Añadir","Grafo"));
            graphCombo.getItems().addAll(PTMS.getInstance().getMaps());
            graphCombo.setValue(PTMS.getInstance().getGraph());
            selectNode(null);
        	selectEdge(null);
        	remakeStops();
        	remakeRoutes();
        	updateInfo();
        	updateGraph();
    	}
    }
    
    public void deleteStop(Stop stop) {
    	 graphNodes.remove(stop);
    	 PTMS.getInstance().getGraph().deleteStop(stop);
    	 selectNode(null);
    	 remakeRoutes();
    	 updateInfo();
    	 updateGraph();
    	 updateEdges();
    }
    
    public void deleteRoute(Route route) {
    	graphEdges.remove(route);
    	PTMS.getInstance().getGraph().deleteRoute(route.getSrc(), route.getDest());
    	selectEdge(null);
    	updateEdges();
    	updateInfo();
    }
    
    private void remakeStops() {
    	graphNodes.clear();
    	Circle currentStop;
    	for(LinkedList<Stop> currentList : PTMS.getInstance().getGraph().getAdjList()) {
    		Stop stop = currentList.get(0);
    		currentStop = new Circle(stop.getX(), stop.getY(), circleRadius);
    		currentStop.setStyle("-fx-fill: #3498db;");
    		graphNodes.put(stop, currentStop);
    	}
    }
    
    private void remakeRoutes() {
    	graphEdges.clear();
    	Line currentRoute;
    	for(Route r : PTMS.getInstance().getGraph().getRoutes()) {
    		currentRoute = new Line(r.getSrc().getX(), r.getSrc().getY(), r.getDest().getX(), r.getDest().getY());
    		currentRoute.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
      	    graphEdges.put(r, currentRoute);
    	}
    }
    
    private void updateGraph() {
    	tableData = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStops());
    	table.setItems(tableData);
    	table.refresh();
    	graphPane.getChildren().clear();
    	
    	// Adding nodes to the graphPane
    	for(Entry<Stop, Circle> entry : graphNodes.entrySet()) {
        	graphPane.getChildren().add(entry.getValue());
    	}
    	// Adding edges to the graphPane
    	for(Line l : graphEdges.values()) {
   			Polygon arrowhead = createArrowhead(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
   			if(selectedEdge != null && selectedEdge.equals(l)) {
   				l.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 6;");
   				arrowhead.setStyle("-fx-stroke: #e66161; -fx-stroke-width: 6;");
   			}else
   			if(l.getStyle().contains("-fx-stroke: #000000;")) {
   	   			l.setStyle("-fx-stroke: #f9b040; -fx-stroke-width: 6;");
   	   			arrowhead.setStyle("-fx-stroke: #f9b040; -fx-stroke-width: 6;");   			
  			}else
   			{
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
    		graphEdges.put(entry.getValue(), new Line(entry.getValue().getSrc().getX(),entry.getValue().getSrc().getY(),entry.getValue().getDest().getX(),entry.getValue().getDest().getY()));
    	}
    }
    
    private Stop getVisualStop(Circle visual) {
    	for(Stop s : PTMS.getInstance().getGraph().getStops()) {
    		if(graphNodes.get(s).equals(visual)) return s;
    	}
    	return null;
    }
    
    private Route getVisualRoute(Line visual) {
    	for(Route r : PTMS.getInstance().getGraph().getRoutes()) {
    		if(graphEdges.get(r).equals(visual)) return r;
    	}
    	return null;
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
