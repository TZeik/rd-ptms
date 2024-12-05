package visual;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import exceptions.NullStopException;
import exceptions.SameStopException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private Alert alert;
    private VBox objectInfoPane;
    private VBox pathFinderPane;
    
    ComboBox<Graph> graphCombo;
    
    private ComboBox<String> algorythmsCombo;
    private ComboBox<String> priorityCombo;
    private ComboBox<Stop> initialStopCombo;
    private ComboBox<Stop> endStopCombo;
    
	private Label distanceDetail;
    private Label timeDetail;
    private Label transhipDetail;
    
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
    
    private CheckBox showStopNamesCB;
    private CheckBox showEdgeNamesCB;
    private CheckBox showEdgeDistancesCB;
    private TextFlow terminalText; 
    private Text text;
    
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
    static final int gridSize = 20; // Grid cell size
    static final int width = 1320; // Graph Pane width
    static final int height = 1020; // Graph Pane height
    static final double menuButtonHeight = 50;
    static final double menuButtonWidth = 300;
    static final double circleRadius = 20;
    static final double buttonWidth = 300;
    static final double comboWidth = 325;
    static final String dijkstra = "Dijkstra";
    static final String bellmanford = "Bellman Ford";
    static final String warshall = "Floyd Warshall";
    static final String kruskal = "Kruskal";
    static final String fastest = "Más rápido";
    static final String shortest = "Más corto";
    static final String transferless = "Menos transbordos";
    
    
    @Override
    public void start(Stage primaryStage) {
    	
    	primaryStage.setTitle("Sistema de Transporte Público (PTMS)");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
        
        // Left Panel: Menu
        VBox menuPane = createMenuPane();
        menuPane.getStyleClass().add("pane");
        menuPane.setSpacing(20);

        // Center Panel: Graph view
        graphPane = new Pane();
        graphPane.getStyleClass().add("diagram-pane");
        graphPane.setOnMouseClicked(this::handleObjectClick);
       

        // Right Panel: Stop info/details
        infoPane = new VBox();
        infoPane.setPadding(new Insets(100,20,20,20));
        infoPane.getStyleClass().add("pane");
        infoPane.setPrefWidth(300); // Set a fixed width for the info pane

        // Add the symbol pane and labels/buttons to the info pane
        objectInfoPane = createObjectInfoPane();
        pathFinderPane = createPathFinderPane(null, null);
        infoPane.getChildren().add(objectInfoPane);
        
        StackPane bottomPane = new StackPane();
        bottomPane.getStyleClass().add("pane");
        bottomPane.setPadding(new Insets(8.5));
        
        // Creating Instruction Box Label
        blankBox = new StackPane();
        blankBox.getStyleClass().add("blankpane");
        blankBox.setPrefHeight(20);
        instructionBox = new StackPane();
        instructionBox.getStyleClass().add("infopane");
        instructionLabel = new Label("");
        instructionLabel.getStyleClass().add("label");
        instructionBox.getChildren().add(instructionLabel);

        // Setting up screen
        root = new BorderPane();
        root.setLeft(menuPane);
        root.setCenter(graphPane);
        root.setRight(infoPane);
        root.setTop(blankBox);
        root.setBottom(bottomPane);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setOnCloseRequest(event -> handleWindowClose(event));
        primaryStage.setScene(scene);
        primaryStage.show();
        
        if(PTMS.getInstance().checkPathFinderUsability()) searchPathButton.setDisable(false); else searchPathButton.setDisable(true);
        
        updateCheckBoxes();
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
        graphCombo.getItems().add(new Graph("Añadir","Mapa"));
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
        	alert = new Alert(AlertType.CONFIRMATION);
        	alert.initStyle(StageStyle.UNDECORATED);
        	alert.setTitle("Eliminar " + PTMS.getInstance().getGraph().getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar \"" + PTMS.getInstance().getGraph().getLabel() + "\"?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	DialogPane dialogPane = alert.getDialogPane();
        	dialogPane.getStylesheets().add(
        	   getClass().getResource("alert.css").toExternalForm());
        	dialogPane.getStyleClass().add("dialog-pane");
        	
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
        table.getStyleClass().add("table-view");
        
     // Create columns
        TableColumn<Stop, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setResizable(false);
        idColumn.setReorderable(false);
        idColumn.setPrefWidth(55);

        TableColumn<Stop, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        nameColumn.setResizable(false);
        nameColumn.setReorderable(false);
        nameColumn.setPrefWidth(125);
        
        TableColumn<Stop, Double> xColumn = new TableColumn<>("X");
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        xColumn.setResizable(false);
        xColumn.setReorderable(false);
        xColumn.setPrefWidth(45);
        
        TableColumn<Stop, Double> yColumn = new TableColumn<>("Y");
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        yColumn.setResizable(false);
        yColumn.setReorderable(false);
        yColumn.setPrefWidth(45);
        
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
                    
                    selectEdge(null);
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
        
        showStopNamesCB = new CheckBox("Nombres de Parada");
        showEdgeNamesCB = new CheckBox("Nombres de Rutas");
        showEdgeDistancesCB = new CheckBox("Distancias de Rutas");
        
        showStopNamesCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                PTMS.getInstance().setIsStopNames(true);
            } else {
                PTMS.getInstance().setIsStopNames(false);
            }
            updateGraph();
        });
        
        showEdgeNamesCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                PTMS.getInstance().setIsEdgeNames(true);
            } else {
                PTMS.getInstance().setIsEdgeNames(false);
            }
            updateGraph();
        });
        
        showEdgeDistancesCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                PTMS.getInstance().setIsEdgeDistances(true);
            } else {
                PTMS.getInstance().setIsEdgeDistances(false);
            }
            updateGraph();
        });
        

        menuPane.getChildren().addAll(addNodeButton, searchPathButton);
        menuPane.getChildren().addAll(showStopNamesCB, showEdgeNamesCB, showEdgeDistancesCB);
        menuPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        return menuPane;
    }
    
    private VBox createObjectInfoPane() {
    	VBox objectInfoPane = new VBox();
    	
    	// Add a BorderPane for the symbol display
        symbolPane = new BorderPane();
        symbolPane.setPrefSize(100, 200);
        symbolPane.getStyleClass().add("pane");
        
        // Info Symbol
        nodeSymbol = new Circle(100, 100, 20);
        nodeSymbol.setStyle("-fx-fill: #a3a8ab;");
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
        	alert = new Alert(AlertType.CONFIRMATION);
        	alert.initStyle(StageStyle.UNDECORATED);
        	alert.setTitle("Eliminar " + selectedStop.getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar \"" + selectedStop.getLabel() + "\"?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	DialogPane dialogPane = alert.getDialogPane();
        	dialogPane.getStylesheets().add(
        	   getClass().getResource("alert.css").toExternalForm());
        	dialogPane.getStyleClass().add("dialog-pane");
        	
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
        	alert = new Alert(AlertType.CONFIRMATION);
        	alert.initStyle(StageStyle.UNDECORATED);
        	alert.setTitle("Eliminar " + selectedRoute.getLabel());
        	alert.setHeaderText("¿Estás seguro de que quieres eliminar \"" + selectedRoute.getLabel() + "\"?");
        	alert.setContentText("Esta acción no puede deshacerse");
        	ButtonType yesButton = new ButtonType("Sí");
        	ButtonType noButton = new ButtonType("No");
        	alert.getButtonTypes().setAll(yesButton, noButton);
        	
        	DialogPane dialogPane = alert.getDialogPane();
        	dialogPane.getStylesheets().add(
        	   getClass().getResource("alert.css").toExternalForm());
        	dialogPane.getStyleClass().add("dialog-pane");
        	
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
    	
    	Label algorythmsLabel = new Label("Algoritmo a utilizar");
    	Label initialStopLabel = new Label("Inicio");
    	Label endStopLabel = new Label("Destino");
    	Label priorityLabel = new Label("Prioridad");
    	
        algorythmsCombo = new ComboBox<>();
        initialStopCombo = new ComboBox<>();
        endStopCombo = new ComboBox<>();
        priorityCombo = new ComboBox<>();
        
        algorythmsCombo.setPrefWidth(comboWidth);
        initialStopCombo.setPrefWidth(comboWidth);
        endStopCombo.setPrefWidth(comboWidth);
        priorityCombo.setPrefWidth(comboWidth);
                
        VBox pathInfoPane = new VBox();
        pathInfoPane.setPadding(new Insets(20,10,10,10));
    	pathInfoPane.setSpacing(4);
    	pathInfoPane.setAlignment(Pos.TOP_CENTER); 
    	
    	Label distanceLabel = new Label("Distancia total recorrida:");
    	distanceDetail = new Label("");
        Label timeLabel = new Label("Tiempo total de viaje:");
        timeDetail = new Label("");
        Label transhipLabel = new Label("Total de transbordos:");
        transhipDetail = new Label("");
        
        pathInfoPane.getChildren().addAll(distanceLabel, distanceDetail, timeLabel, timeDetail, transhipLabel, transhipDetail);
        
        terminalPane = createTerminal();
        
        trailblazeButton = new Button("Trazar camino");
        trailblazeButton.setPrefWidth(buttonWidth);
        
        algorythmsCombo.getItems().addAll(dijkstra, bellmanford, warshall, kruskal);
        priorityCombo.getItems().addAll(fastest, shortest, transferless);
        initialStopCombo.getItems().addAll(PTMS.getInstance().getGraph().getStops());
        endStopCombo.getItems().addAll(PTMS.getInstance().getGraph().getStops());
        
        //ComboBox place-holders
        algorythmsCombo.setValue(dijkstra);
        priorityCombo.setValue(shortest);
        if(start == null && end == null) {
        	initialStopCombo.setValue(null);
        }else if(start == null) {
        	initialStopCombo.setValue(PTMS.getInstance().getGraph().getStops().getFirst());
        }else initialStopCombo.setValue(start);
        	
        endStopCombo.setValue(end);
        
        if(initialStopCombo.getValue() != null) graphNodes.get(initialStopCombo.getValue()).setStyle("-fx-fill: #CBE982;");
        if(endStopCombo.getValue() != null) graphNodes.get(endStopCombo.getValue()).setStyle("-fx-fill: #E99E82;");
        
        initialStopCombo.valueProperty().addListener(new ChangeListener<Stop>() {
            @Override
            public void changed(ObservableValue<? extends Stop> observable, Stop oldValue, Stop newValue) {
                if (newValue != null) { // Ensure a new value is selected
                	
                	if(graphNodes.containsKey(newValue)) {
                		graphNodes.get(oldValue).setStyle("-fx-fill: #007bff;");
                		graphNodes.get(newValue).setStyle("-fx-fill: #CBE982;");
                	}
                }
            }
            
        });
        
        endStopCombo.valueProperty().addListener(new ChangeListener<Stop>() {
            @Override
            public void changed(ObservableValue<? extends Stop> observable, Stop oldValue, Stop newValue) {
                if (newValue != null) { // Ensure a new value is selected
                	if(graphNodes.containsKey(newValue)) {
                		graphNodes.get(oldValue).setStyle("-fx-fill: #007bff;");
                		graphNodes.get(newValue).setStyle("-fx-fill: #E99E82;");
                	}
                }
            }
            
        });
        
        //Acción del botón
        trailblazeButton.setOnAction(event -> {
        	try {
        		PTMS.getInstance().checkSameStopPath(initialStopCombo.getValue(), endStopCombo.getValue());
        		selectedNode = null;
        		selectedEdge = null;
        		searchPath(initialStopCombo.getValue(), endStopCombo.getValue(), algorythmsCombo.getValue(), priorityCombo.getValue());
                waitForUserAction(4);
        	}catch(SameStopException | NullStopException ex) {
        		alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No se buscó un camino");
                alert.setContentText(ex.getMessage());
                
                DialogPane dialogPane = alert.getDialogPane();
            	dialogPane.getStylesheets().add(
            	   getClass().getResource("monoalert.css").toExternalForm());
            	dialogPane.getStyleClass().add("dialog-pane");
                
                // Show the alert and wait for the user's response
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                    	
                    }
                });
        	}
        });
        
        pathFinderPane.getChildren().addAll(titleLabel, algorythmsLabel, algorythmsCombo, initialStopLabel, initialStopCombo, endStopLabel, endStopCombo, priorityLabel, priorityCombo, pathInfoPane,terminalPane, trailblazeButton);
        pathFinderPane.setAlignment(Pos.TOP_CENTER); 
        
    	return pathFinderPane;
    }
    
    private Pane createTerminal() {
    	// Create a VBox for containing the text lines
        textContainer = new VBox(5); // Spacing of 5 between lines
        textContainer.setPrefWidth(250);
        textContainer.getStyleClass().add("text-container-borderless");
        
        terminalText = new TextFlow();
        terminalText.setTextAlignment(TextAlignment.JUSTIFY);
        
        // Example of wrapped text
        text = new Text("Los detalles del camino se especificarán aquí");
        text.getStyleClass().add("terminal-text");
        addTextLine(terminalText, text);
        textContainer.getChildren().add(terminalText);

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setFitToWidth(true); // Ensure the ScrollPane fits to the width of the content
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Allow vertical scrolling
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrolling
        scrollPane.getStyleClass().add("text-container");

        // Configure the size of the ScrollPane to prevent width growth
        scrollPane.setPrefSize(260, 300); // Fixed width and height for the terminal window

        // Create the main Pane and add the ScrollPane
        Pane terminalPane = new Pane(scrollPane);
        terminalPane.getStyleClass().add("text-container");
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(0);

        return terminalPane;
    }
    
    public void waitForUserAction(int arg) {
    	
    	if(arg == 0) {
    		graphPane.setCursor(Cursor.CROSSHAIR);
    		instructionLabel.setText("Haz click para insertar la parada o presiona ESC para cancelar");
            root.setTop(instructionBox);
            
            disableAll();
            
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
            
            disableAll();
            
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
            
            disableAll();
            
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
            
            disableAll();
            
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

    		disableAll();

    		graphPane.setOnMouseClicked(this::handleSearchedPath);
    		
    		graphPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                	resetTrailblaze();
                	selectNode(null);
                	if(initialStopCombo.getValue() != null) graphNodes.get(initialStopCombo.getValue()).setStyle("-fx-fill: #CBE982;");
                    if(endStopCombo.getValue() != null) graphNodes.get(endStopCombo.getValue()).setStyle("-fx-fill: #E99E82;");
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
        enableAll();
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
            	selectedStop = getVisualStop(node);
                selectNode(node);  // Select the clicked node
                table.getSelectionModel().select(selectedStop);
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
                selectedStop = getVisualStop(node);
                selectNode(node);  // Select the clicked node
            }
        }
        if((lastNode != null && lastNode.equals(selectedNode)) || (lastNode == null && selectedNode == null)) {
        	selectNode(null);
        	selectedStop = null;
        	endUserAction();
        }else{
            infoPane.getChildren().clear();
            pathFinderPane = createPathFinderPane(lastStop, selectedStop);
            infoPane.getChildren().add(pathFinderPane);
            endUserAction();
        }
    	
    };
    
    private void handleSearchedPath(MouseEvent event) {
    	resetTrailblaze();
    	selectNode(null);
    	if(initialStopCombo.getValue() != null) graphNodes.get(initialStopCombo.getValue()).setStyle("-fx-fill: #CBE982;");
        if(endStopCombo.getValue() != null) graphNodes.get(endStopCombo.getValue()).setStyle("-fx-fill: #E99E82;");
    	endUserAction();
    	
    };
    
    private void handleWindowClose(WindowEvent event) {
    	
    	alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Desea guardar antes de salir?");
        alert.setContentText("Los cambios sin guardar se perderán");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setOnCloseRequest(e -> {
        	event.consume();
        });
 
        // Show the alert and wait for user response
        ButtonType yesButton = new ButtonType("Sí");
    	ButtonType noButton = new ButtonType("No");
    	
    	alert.getButtonTypes().setAll(yesButton, noButton);
    	
    	DialogPane dialogPane = alert.getDialogPane();
    	dialogPane.getStylesheets().add(
    	   getClass().getResource("alert.css").toExternalForm());
    	dialogPane.getStyleClass().add("dialog-pane");
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.isPresent() && result.get() == yesButton) {
    		PTMS.getInstance().savePTMS();
    	}
    	if(result.isPresent() && result.get() == noButton) {
    	}
    };
    
    private void searchPath(Stop from, Stop to, String algorythm, String priority) {
    	
    	PathFinder pathfinder = new PathFinder(PTMS.getInstance().getGraph());
    	List<Stop> path;
    	
    	switch(algorythm) {
    	case dijkstra:
    		switch(priority) {
    		case shortest:
        		path = pathfinder.dijkstra_shortest(from, to);
    			break;
    		case fastest:
    			path = pathfinder.dijkstra_fastest(from, to);
    			break;
    		case transferless:
    			path = pathfinder.dijkstra_transferless(from, to);
    			break;
    		default:
    			path = pathfinder.dijkstra_shortest(from, to);
    			break;
    		}
    		break;
    	case bellmanford:
    		switch(priority) {
    		case shortest:
    			path = pathfinder.bellmanFord_shortest(from, to);
    			break;
    		case fastest:
    			path = pathfinder.bellmanFord_fastest(from, to);
    			break;
    		case transferless:
    			path = pathfinder.bellmanFord_transferless(from, to);
    			break;
    		default:
    			path = pathfinder.bellmanFord_shortest(from, to);
    			break;
    		}
    		
    		break;
    	case warshall:
    		switch(priority) {
    		case shortest:
    			path = pathfinder.floydWarshall_shortest(from, to);
    			break;
    		case fastest:
    			path = pathfinder.floydWarshall_fastest(from, to);
    			break;
    		case transferless:
    			path = pathfinder.floydWarshall_transferless(from, to);
    			break;
    		default:
    			path = pathfinder.floydWarshall_shortest(from, to);
    			break;
    		}
    		break;
    	case kruskal:
    		switch(priority) {
    		case shortest:
    			path = pathfinder.kruskalMST_shortest(from, to);
    			break;
    		case fastest:
    			path = pathfinder.kruskalMST_fastest(from, to);
    			break;
    		case transferless:
    			path = pathfinder.kruskalMST_transferless(from, to);
    			break;
    		default:
    			path = pathfinder.kruskalMST_shortest(from, to);
    			break;
    		}	
    		break;
    	default:
    		path = pathfinder.dijkstra_shortest(from, to);
    		break;
    	}
    	
        terminalText.getChildren().clear();
		pathfinder.printPath(path);
        
		List<Stop> searched = path;
		
    	if(path.isEmpty()) {
    		text = new Text("No se encontró un camino");
    		addTextLine(terminalText, text);
    	}else {
        	int[] pathDetails = pathfinder.getPathDetails(path);
        	distanceDetail.setText(""+pathDetails[0]+" km");
            timeDetail.setText(""+pathDetails[1]+" min");
            transhipDetail.setText(""+pathDetails[2]+" transbordos");
            
        	trailblaze(path);
        	
            // Escribir por terminal las rutas alternativas
        	
        	
            switch(algorythm) {
        	case dijkstra:
        		switch(priority) {
        		case shortest:
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case fastest:
        			path = pathfinder.bellmanFord_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case transferless:
        			path = pathfinder.bellmanFord_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		default:
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		}
        		break;
        	case bellmanford:
        		switch(priority) {
        		case shortest:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case fastest:
        			path = pathfinder.dijkstra_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case transferless:
        			path = pathfinder.dijkstra_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		default:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		}
        		
        		break;
        	case warshall:
        		switch(priority) {
        		case shortest:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case fastest:
        			path = pathfinder.dijkstra_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case transferless:
        			path = pathfinder.dijkstra_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		default:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.kruskalMST_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, kruskal)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		}
        		break;
        	case kruskal:
        		switch(priority) {
        		case shortest:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case fastest:
        			path = pathfinder.dijkstra_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_fastest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		case transferless:
        			path = pathfinder.dijkstra_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_transferless(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		default:
        			path = pathfinder.dijkstra_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, dijkstra)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.bellmanFord_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, bellmanford)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			path = pathfinder.floydWarshall_shortest(from, to);
        			if(checkSamePaths(path, searched) == false) {
        				for (String message : pathfinder.getAlternativePath(path, warshall)) {
                            text = new Text(message);
                            addTextLine(terminalText, text);
                        }
        			}
        			
        			break;
        		}	
        		break;
        	default:
        		path = pathfinder.dijkstra_shortest(from, to);
        		break;
        	}
            
            for (String message : pathfinder.getRouteDetails(searched)) {
                text = new Text(message);
                addTextLine(terminalText, text);
            }
    	}
    }

    private void trailblaze(List<Stop> path) {
    	
    	for(Stop s : path) {
    		if(graphNodes.get(s) != null) {
    			graphNodes.get(s).setStyle("-fx-fill: #f9b040;");
    			if(s.equals(path.getFirst())) graphNodes.get(s).setStyle("-fx-fill: #CBE982;");
    			if(s.equals(path.getLast())) graphNodes.get(s).setStyle("-fx-fill: #E99E82;");
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
    	
    	enableAll();
    
    	
    	for(Circle node : graphNodes.values()) {
    		node.setStyle("-fx-fill: #3498db;");
    	}
    	
    	for(Line l : graphEdges.values()) {
    		l.setStyle("-fx-stroke: #2c3e50; -fx-stroke-width: 6;");
    	}
    	
    	updateGraph();
    }
    
    private void selectNode(Circle node) {
    	if(node != null) selectedNode = node; else selectedNode = null;
    	updateGraph();
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
    	graphCombo.getItems().add(new Graph("Añadir","Mapa"));
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
    	node.getStyleClass().add("stop-circle");
    	node.setStyle("-fx-fill: #007bff;");
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
    	graphCombo.getItems().add(new Graph("Añadir","Mapa"));
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
    	node.getStyleClass().add("stop-circle");
    	node.setStyle("-fx-fill: #007bff;");
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
        	graphCombo.getItems().add(new Graph("Añadir","Mapa"));
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
        	graphCombo.getItems().add(new Graph("Añadir","Mapa"));
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
    		currentStop.getStyleClass().add("stop-circle");
    		currentStop.setStyle("-fx-fill: #007bff;");
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
    	
    	 for (int x = 0; x < width; x += gridSize) {
             Line line = new Line(x, 0, x, height);
             line.getStyleClass().add("grid-lines");
             graphPane.getChildren().add(line);
         }

         for (int y = 0; y < height; y += gridSize) {
             Line line = new Line(0, y, width, y);
             line.getStyleClass().add("grid-lines");
             graphPane.getChildren().add(line);
         }

         // Add highlighted grid lines every 5 cells
         for (int x = 0; x < width; x += gridSize * 5) {
             Line line = new Line(x, 0, x, height);
             line.getStyleClass().add("highlighted-grid-lines");
             graphPane.getChildren().add(line);
         }

         for (int y = 0; y < height; y += gridSize * 5) {
             Line line = new Line(0, y, width, y);
             line.getStyleClass().add("highlighted-grid-lines");
             graphPane.getChildren().add(line);
         }
    	
    	if(PTMS.getInstance().checkPathFinderUsability()) searchPathButton.setDisable(false); else searchPathButton.setDisable(true);
    	
    	// Adding nodes to the graphPane
    	for(Entry<Stop, Circle> entry : graphNodes.entrySet()) {
    		if(selectedNode != null && selectedNode.equals(entry.getValue())) entry.getValue().setStyle("-fx-fill: #e66161;");
    		else entry.getValue().setStyle("-fx-fill: #007bff;");
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
    	
    	if(PTMS.getInstance().getIsStopNames()) {
    		for(Entry<Stop, Circle> entry : graphNodes.entrySet()) {
        		Label nodeLabel = new Label(entry.getKey().getLabel());
        		nodeLabel.getStyleClass().add("nodelabel");
        		nodeLabel.setLayoutX(entry.getValue().getCenterX() );
        		nodeLabel.setLayoutY(entry.getValue().getCenterY()+20);
               	graphPane.getChildren().add(nodeLabel);
        	}
		} 	
    	
    	if(PTMS.getInstance().getIsEdgeNames() || PTMS.getInstance().getIsEdgeDistances()) {
    		for(Entry<Route, Line> entry : graphEdges.entrySet()) {
        		if(PTMS.getInstance().getIsEdgeNames()) {
        			Label edgeLabel = new Label(entry.getKey().getLabel());
        			edgeLabel.getStyleClass().add("edgelabel");
        			edgeLabel.setLayoutX((entry.getValue().getStartX()+entry.getValue().getEndX())/2);
            		edgeLabel.setLayoutY(((entry.getValue().getStartY()+entry.getValue().getEndY())/2)-15);
            		graphPane.getChildren().add(edgeLabel);
        		}
        		if(PTMS.getInstance().getIsEdgeDistances()) {
        			Label edgeDistance = new Label(String.valueOf(entry.getKey().getDistance()));
            		edgeDistance.getStyleClass().add("edgelabel");
            		edgeDistance.setLayoutX((entry.getValue().getStartX()+entry.getValue().getEndX())/2);
            		edgeDistance.setLayoutY(((entry.getValue().getStartY()+entry.getValue().getEndY())/2)+5);
                   	graphPane.getChildren().add(edgeDistance);
        		}
        	}
    	}    	
    }

    private void updateEdges() {
    	graphEdges.clear();
    	for(Entry<String, Route> entry : PTMS.getInstance().getGraph().getRoutesMap().entrySet()) {
    		graphEdges.put(entry.getValue(), new Line(entry.getValue().getSrc().getX(),entry.getValue().getSrc().getY(),entry.getValue().getDest().getX(),entry.getValue().getDest().getY()));
    	}
    }
    

    private void updateCheckBoxes() {
    	if(PTMS.getInstance().getIsStopNames()) showStopNamesCB.setSelected(true); else showStopNamesCB.setSelected(false);
    	if(PTMS.getInstance().getIsEdgeNames()) showEdgeNamesCB.setSelected(true); else showEdgeNamesCB.setSelected(false);
    	if(PTMS.getInstance().getIsEdgeDistances()) showEdgeDistancesCB.setSelected(true); else showEdgeDistancesCB.setSelected(false);
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
    
    private void enableAll() {
    	graphCombo.setDisable(false);
    	editGraphButton.setDisable(false);
    	deleteGraphButton.setDisable(false);
    	table.setDisable(false);
    	addNodeButton.setDisable(false);
    	searchPathButton.setDisable(false);
    	algorythmsCombo.setDisable(false);
		initialStopCombo.setDisable(false);
		endStopCombo.setDisable(false);
		priorityCombo.setDisable(false);
		trailblazeButton.setDisable(false);
		actionButton1.setDisable(false);
		actionButton2.setDisable(false);
		actionButton3.setDisable(false);
		actionButton4.setDisable(false);
		actionButton5.setDisable(false);
		showStopNamesCB.setDisable(false);
		showEdgeNamesCB.setDisable(false);
		showEdgeDistancesCB.setDisable(false);
    }
    
    private void disableAll() {
    	graphCombo.setDisable(true);
    	editGraphButton.setDisable(true);
    	deleteGraphButton.setDisable(true);
		table.setDisable(true);
    	addNodeButton.setDisable(true);
    	searchPathButton.setDisable(true);
		algorythmsCombo.setDisable(true);
		initialStopCombo.setDisable(true);
		endStopCombo.setDisable(true);
		priorityCombo.setDisable(true);
		trailblazeButton.setDisable(true);
		actionButton1.setDisable(true);
		actionButton2.setDisable(true);
		actionButton3.setDisable(true);
		actionButton4.setDisable(true);
		actionButton5.setDisable(true);
		showStopNamesCB.setDisable(true);
		showEdgeNamesCB.setDisable(true);
		showEdgeDistancesCB.setDisable(true);
    }

    private void addTextLine(TextFlow textFlow, Text text) {
        text.setWrappingWidth(200); // Ensure wrapping at the same width
        text.getStyleClass().add("terminal-text"); // Add custom CSS for styling
        textFlow.getChildren().add(text); // Add the text node to the TextFlow
        Text lineBreak = new Text("\n");
        lineBreak.getStyleClass().add("terminal-text");
        // You can also add a LineBreak explicitly if needed
        textFlow.getChildren().add(lineBreak); // Add line break (simulating a new line)
    }

	public boolean checkSamePaths(List<Stop> a, List<Stop> b) {
		
		boolean isSame = true;
		
		for(Stop s : a) {
			if(b.contains(s)) continue; else isSame = false;
		}
		
		return isSame;
	}
    
}
