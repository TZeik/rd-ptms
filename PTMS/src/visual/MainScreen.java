package visual;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Stop;

public class MainScreen extends Application{
	private Pane graphPane;
    private VBox infoPane;
    private Node selectedNode;

    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Sistema de Transporte Público PTMS");

        // Left Panel: Menu
        VBox menuPane = createMenuPane();

        // Center Panel: Graph view
        graphPane = new Pane();
        graphPane.setStyle("-fx-background-color: lightgray;");

        // Right Panel: Stop info
        infoPane = new VBox();
        infoPane.setPadding(new Insets(10));
        
        
        // Setting up screen
        BorderPane root = new BorderPane();
        root.setLeft(menuPane);
        root.setCenter(graphPane);
        root.setRight(infoPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenuPane() {
        VBox menuPane = new VBox();
        menuPane.setPadding(new Insets(10));
        menuPane.setSpacing(5);

        Button addNodeButton = new Button("Agregar Parada");
        addNodeButton.setOnAction(e -> new AddStopDialog(this).show());

        Button deleteNodeButton = new Button("Eliminar Parada");
        deleteNodeButton.setOnAction(e -> deleteNode());

        Button addEdgeButton = new Button("Agregar Ruta");
        addEdgeButton.setOnAction(e -> new AddRouteDialog(this).show());

        Button deleteEdgeButton = new Button("Eliminar Ruta");
        deleteEdgeButton.setOnAction(e -> deleteEdge());

        menuPane.getChildren().addAll(addNodeButton, deleteNodeButton, addEdgeButton, deleteEdgeButton);
        return menuPane;
    }


    private void deleteNode() { /* Delete node logic */ }
    private void deleteEdge() { /* Delete edge logic */ }
    private void selectNode(Node node) { /* Select node logic */ }
}
