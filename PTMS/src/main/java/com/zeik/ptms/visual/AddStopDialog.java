package com.zeik.ptms.visual;

import com.zeik.ptms.exceptions.BadNameException;
import com.zeik.ptms.exceptions.EmptyNameException;
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
import com.zeik.ptms.logic.PTMS;
import com.zeik.ptms.logic.Stop;

public class AddStopDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField labelField;
    private Button addButton;
    private Button cancelButton;

    public AddStopDialog(MainScreen app, Stop selectedStop) {
        setTitle("Agregar Parada");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        labelField = new TextField();
        labelField.getStyleClass().add("text-field");
        addButton = new Button("Crear");
        cancelButton = new Button("Cancelar");
        
        addButton.setPrefHeight(buttonHeight);
        addButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        labelField.setPromptText("Digite un nombre");

        // Add a listener to restrict input to alphanumeric characters and spaces
        labelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 ñÑ]*")) {
                labelField.setText(oldValue);
            }
        });
        
        // Set action for the Add button
        addButton.setOnAction(e -> {
        	
        	try {
				PTMS.getInstance().checkVerifiedName(labelField.getText());
				selectedStop.setId(PTMS.getInstance().generateStopID());
	        	selectedStop.setLabel(labelField.getText());
	            app.addStop(selectedStop);
	            close();
        	} catch (BadNameException | EmptyNameException ex) {
        		Alert info = new Alert(AlertType.INFORMATION);
        		info.initStyle(StageStyle.UNDECORATED);
        		info.setTitle("Error");
                info.setHeaderText("No se pudo añadir la parada");
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
        
        cancelButton.setOnAction(e -> {
            close();
        });
        
        // Layout
        VBox layout = new VBox(10);
        HBox mybuttons = new HBox(5);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
        	new Label("Nombre"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 110);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        layout.requestFocus();
        setScene(scene);
    }
}
