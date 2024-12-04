package visual;

import exceptions.BadNameException;
import exceptions.EmptyNameException;
import exceptions.SameStopException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.PTMS;
import logic.Route;
import logic.Stop;

public class AddRouteDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField labelField;
	private TextField distanceField;
    private Button addButton;
    private Button cancelButton;

    public AddRouteDialog(MainScreen app, Stop start, Stop end) {
        setTitle("Agregar Ruta");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        labelField = new TextField();
        labelField.getStyleClass().add("text-field");
        distanceField = new TextField();
        distanceField.getStyleClass().add("text-field");
        addButton = new Button("Agregar");
        cancelButton = new Button("Cancelar");
        
        addButton.setPrefHeight(buttonHeight);
        addButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        labelField.setPromptText("Digite un nombre");

        // Add a listener to restrict input to alphanumeric characters and spaces
        labelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 ]*")) { // Includes spaces
                labelField.setText(oldValue);
            }
        });
        
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
        addButton.setOnAction(e -> {
        	try {
        		PTMS.getInstance().checkVerifiedName(labelField.getText());
        		Route route = new Route(PTMS.getInstance().generateRouteID(), Double.parseDouble(distanceField.getText()), start, end, labelField.getText());
        		PTMS.getInstance().checkSameStop(route);
        		app.addRoute(route);
        	}catch(SameStopException | BadNameException | EmptyNameException ex){
        		Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Error");
                info.setHeaderText("No se pudo añadir la ruta");
                info.setContentText(ex.getMessage());
                
                DialogPane dialogPane = info.getDialogPane();
            	dialogPane.getStylesheets().add(
            	   getClass().getResource("monoalert.css").toExternalForm());
            	dialogPane.getStyleClass().add("dialog-pane");
                
                // Show the alert and wait for the user's response
                info.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                    	
                    }
                });
        	}
        	
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
        	new Label("Nombre"), labelField,
            new Label("Distancia"), distanceField,
            mybuttons
        );
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 190);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        layout.requestFocus();
        setScene(scene);

    }
}
