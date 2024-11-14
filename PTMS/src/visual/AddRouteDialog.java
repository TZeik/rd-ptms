package visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.PTMS;
import logic.Route;
import logic.Stop;

public class AddRouteDialog extends Stage{
	private TextField idField;
	private TextField labelField;
	private TextField distanceField;
	private ComboBox<String> startNodeField;
    private ComboBox<String> endNodeField;
    private Button addButton;
    private Button cancelButton;

    public AddRouteDialog(MainScreen app) {
        setTitle("Agregar Ruta");
        initModality(Modality.APPLICATION_MODAL);

        idField = new TextField();
        labelField = new TextField();
        distanceField = new TextField();
        startNodeField = new ComboBox<>();
        endNodeField = new ComboBox<>();
        addButton = new Button("Agregar");
        cancelButton = new Button("Cancelar");
        
        ObservableList<String> observableStops = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStopsName());
        ObservableList<String> observableId = FXCollections.observableArrayList(PTMS.getInstance().getGraph().getStopsId());
        startNodeField.setItems(observableStops);
        endNodeField.setItems(observableStops);
        startNodeField.setPromptText("Selecciona una Parada");
        endNodeField.setPromptText("Selecciona una Parada");
        
        
        idField.setText(PTMS.getInstance().generateRouteID());
        idField.setEditable(false);

        // Set action for the buttons
        addButton.setOnAction(e -> {
        	Route route = new Route(idField.getText(), Integer.parseInt(distanceField.getText()), observableStops.indexOf(startNodeField.getValue()), observableStops.indexOf(endNodeField.getValue()), labelField.getText());
        	PTMS.getInstance().getGraph().addRoute(route);
        	app.addRoute(PTMS.getInstance().getGraph().searchStop(observableId.get(startNodeField.getSelectionModel().getSelectedIndex())), PTMS.getInstance().getGraph().searchStop(observableId.get(endNodeField.getSelectionModel().getSelectedIndex())));
            close();
        });
        
        cancelButton.setOnAction(e -> {
            close();
        });

        // Layout
        VBox layout = new VBox(10);
        HBox mybuttons = new HBox(5);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
        	new Label("ID:"), idField,
        	new Label("Nombre:"), labelField,
            new Label("Salida:"), startNodeField,
            new Label("Destino: "), endNodeField,
            new Label("Distancia: "), distanceField,
            mybuttons
        );
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 500);
        setScene(scene);

    }
}
