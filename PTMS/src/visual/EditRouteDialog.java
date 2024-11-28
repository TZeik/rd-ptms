package visual;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Route;

public class EditRouteDialog extends Stage{
	
	private TextField labelField;
	private TextField distanceField;
    private Button saveButton;
    private Button cancelButton;
    
	public EditRouteDialog(MainScreen app) {
		setTitle("Editar Ruta");
        initModality(Modality.APPLICATION_MODAL);
        
        Route newRoute = app.selectedRoute;
        
        labelField = new TextField(""+newRoute.getLabel());
        distanceField = new TextField(""+newRoute.getDistance());
        saveButton = new Button("Guardar");
        cancelButton = new Button("Cancelar");
        
        // Set action for the buttons
        saveButton.setOnAction(e -> {
        	
        	newRoute.setLabel(labelField.getText());
        	newRoute.setDistance(Integer.parseInt(distanceField.getText()));
        	app.editRoute(newRoute);
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
        mybuttons.getChildren().addAll(saveButton, cancelButton);

        Scene scene = new Scene(layout, 300, 200);
        setScene(scene);
	}

}
