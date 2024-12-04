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
import logic.Stop;

public class EditStopDialog extends Stage{
	
	static final int buttonWidth = 100;
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
        
        // Set action for save an updated stop
        saveButton.setOnAction(e -> {
        	Stop stop = new Stop(idField.getText(), labelField.getText());
            app.selectedStop = stop;
            close();
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
        	new Label("ID:"), idField,
        	new Label("Nombre:"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(saveButton, moveButton, cancelButton);

        Scene scene = new Scene(layout, 300, 180);
        scene.getStylesheets().add(getClass().getResource("modal.css").toExternalForm());
        setScene(scene);
    }
	
}
