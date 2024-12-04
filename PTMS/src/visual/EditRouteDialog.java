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
import javafx.stage.StageStyle;
import logic.Route;

public class EditRouteDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField labelField;
	private TextField distanceField;
    private Button saveButton;
    private Button cancelButton;
    
	public EditRouteDialog(MainScreen app) {
		setTitle("Editar Ruta");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        Route newRoute = app.selectedRoute;
        
        labelField = new TextField(""+newRoute.getLabel());
        labelField.getStyleClass().add("text-field");
        distanceField = new TextField(""+newRoute.getDistance());
        distanceField.getStyleClass().add("text-field");
        saveButton = new Button("Guardar");
        cancelButton = new Button("Cancelar");
        
        saveButton.setPrefHeight(buttonHeight);
        saveButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        // Add a listener to ensure valid input
        distanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                // Allow only digits and one optional dot
                distanceField.setText(oldValue);
            } else if (newValue.chars().filter(ch -> ch == '.').count() > 1) {
                // Prevent more than one dot
                distanceField.setText(oldValue);
            }
        });
        
        // Set action for the buttons
        saveButton.setOnAction(e -> {
        	
        	newRoute.setLabel(labelField.getText());
        	newRoute.setDistance(Double.parseDouble(distanceField.getText()));
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

        Scene scene = new Scene(layout, 300, 190);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        setScene(scene);
	}

}
