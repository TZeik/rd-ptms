package visual;

import exceptions.BadNameException;
import exceptions.EmptyNameException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.PTMS;
import logic.Stop;

public class EditStopDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField idField;
	private TextField labelField;
    private Button saveButton;
    private Button moveButton;
    private Button cancelButton;
    
    public EditStopDialog(MainScreen app) {
        setTitle("Parada " + app.selectedStop.getLabel());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        idField = new TextField(app.selectedStop.getId());
        idField.getStyleClass().add("text-field");
        labelField = new TextField(app.selectedStop.getLabel());
        labelField.getStyleClass().add("text-field");
        saveButton = new Button("Guardar");
        moveButton = new Button("Mover");
        cancelButton = new Button("Cancelar");
        
        idField.setEditable(false);
        
        saveButton.setPrefHeight(buttonHeight);
        saveButton.setPrefWidth(buttonWidth);
        moveButton.setPrefHeight(buttonHeight);
        moveButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        labelField.setPromptText("Digite un nombre");

        // Add a listener to restrict input to alphanumeric characters and spaces
        labelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 ñÑ]*")) {
                labelField.setText(oldValue);
            }
        });
        
        // Set action for save an updated stop
        saveButton.setOnAction(e -> {
        	try {
				PTMS.getInstance().checkVerifiedName(labelField.getText());
				Stop stop = app.selectedStop;
				stop.setLabel(labelField.getText());
	            app.editStop(stop);
	            close();
			} catch (BadNameException | EmptyNameException ex) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.initStyle(StageStyle.UNDECORATED);
				info.setTitle("Error");
                info.setHeaderText("No se pudo editar la parada");
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
        	
        });
        
        moveButton.setOnAction(e -> {
        	app.waitForUserAction(2);
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
        	new Label("ID"), idField,
        	new Label("Nombre"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(saveButton, moveButton, cancelButton);

        Scene scene = new Scene(layout, 300, 180);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        layout.requestFocus();
        setScene(scene);
    }
	
}
