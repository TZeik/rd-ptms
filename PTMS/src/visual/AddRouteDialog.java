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
	private TextField labelField;
	private TextField distanceField;
    private Button addButton;
    private Button cancelButton;

    public AddRouteDialog(MainScreen app, Stop start, Stop end) {
        setTitle("Agregar Ruta");
        initModality(Modality.APPLICATION_MODAL);

        labelField = new TextField();
        distanceField = new TextField();
        addButton = new Button("Agregar");
        cancelButton = new Button("Cancelar");
        
        // Set action for the buttons
        addButton.setOnAction(e -> {
        	Route route = new Route(PTMS.getInstance().generateRouteID(), Integer.parseInt(distanceField.getText()), start, end, labelField.getText());
        	app.addRoute(route);
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
        	new Label("Nombre:"), labelField,
            new Label("Distancia: "), distanceField,
            mybuttons
        );
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 200);
        setScene(scene);

    }
}
