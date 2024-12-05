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
import logic.Graph;
import logic.PTMS;

public class GraphDialog extends Stage{
	
	static final int buttonWidth = 150;
	static final int buttonHeight = 28;
	
	private TextField labelField;
	private Button saveButton;
	private Button cancelButton;
	
	public GraphDialog(MainScreen app, int update) {
		if(update == 0) setTitle("Añadir Mapa");
		if(update == 1) setTitle("Editar "+ PTMS.getInstance().getGraph().getLabel());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        labelField = new TextField("");
        labelField.getStyleClass().add("text-field");
        if(update == 1) labelField.setText(PTMS.getInstance().getGraph().getLabel());
        if(update == 0) saveButton = new Button("Agregar");
        if(update == 1) saveButton = new Button("Guardar");
        cancelButton = new Button("Cancelar");
        
        saveButton.setPrefHeight(buttonHeight);
        saveButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setPrefWidth(buttonWidth);
        
        labelField.setPromptText("Digite un nombre");

        // Add a listener to restrict input to alphanumeric characters and spaces
        labelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 ñÑ]*")) {
                labelField.setText(oldValue);
            }
        });
        
        // Set action for the buttons
        saveButton.setOnAction(e -> {
        	if(update == 0) {
        		try {
					PTMS.getInstance().checkVerifiedName(labelField.getText());
					Graph newGraph = new Graph(PTMS.getInstance().generateGraphID(), labelField.getText());
	            	app.addGraph(newGraph);
	                close();
				} catch (BadNameException | EmptyNameException ex) {
					Alert info = new Alert(AlertType.INFORMATION);
	                info.setTitle("Error");
	                info.setHeaderText("No se pudo agregar el mapa");
	                info.setContentText(ex.getMessage());
	                
	                DialogPane dialogPane = info.getDialogPane();
	            	dialogPane.getStylesheets().add(
	            	   getClass().getResource("alert.css").toExternalForm());
	            	dialogPane.getStyleClass().add("dialog-pane");
	                
	                // Show the alert and wait for the user's response
	                info.showAndWait().ifPresent(response -> {
	                    if (response == ButtonType.OK) {
	                    	
	                    }
	                });
				}
        		
        	}
        	if(update == 1) {
        		try {
					PTMS.getInstance().checkVerifiedName(labelField.getText());
					Graph editGraph = PTMS.getInstance().getGraph();
	        		editGraph.setLabel(labelField.getText());
	        		app.editGraph(editGraph);
	        		close();
				} catch (BadNameException | EmptyNameException ex) {
					Alert info = new Alert(AlertType.INFORMATION);
	                info.setTitle("Error");
	                info.setHeaderText("No se pudo editar el mapa");
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
        		
        	}
        });
        
        cancelButton.setOnAction(e -> {
        	app.graphCombo.setValue(PTMS.getInstance().getGraph());
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
        mybuttons.getChildren().addAll(saveButton, cancelButton);

        Scene scene = new Scene(layout, 300, 120);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        layout.requestFocus();
        setScene(scene);
	}
	
}
