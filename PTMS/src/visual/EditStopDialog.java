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
import logic.Stop;

public class EditStopDialog extends Stage{
	
	private TextField idField;
	private TextField labelField;
    private Button saveButton;
    private Button moveButton;
    private Button cancelButton;
    
    public EditStopDialog(MainScreen app) {
        setTitle("Parada " + app.selectedStop.getLabel());
        initModality(Modality.APPLICATION_MODAL);
        
        idField = new TextField(app.selectedStop.getId());
        labelField = new TextField(app.selectedStop.getLabel());
        saveButton = new Button("Guardar");
        moveButton = new Button("Mover");
        cancelButton = new Button("Cancelar");
        
        idField.setEditable(false);
        
        
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
        setScene(scene);
    }
	
}
