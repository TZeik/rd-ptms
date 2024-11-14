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
import logic.PTMS;
import logic.Stop;

public class AddStopDialog extends Stage{
	private TextField idField;
	private TextField labelField;
    private Button addButton;
    private Button cancelButton;

    public AddStopDialog(MainScreen app) {
        setTitle("Agregar Parada");
        initModality(Modality.APPLICATION_MODAL);
        
        idField = new TextField();
        labelField = new TextField();
        addButton = new Button("Agregar");
        cancelButton = new Button("Cancelar");
        
        idField.setText(PTMS.getInstance().generateStopID());
        idField.setEditable(false);
        
        
        // Set action for the Add button
        addButton.setOnAction(e -> {
        	Stop stop = new Stop(idField.getText(), labelField.getText());
            app.selectedStop = stop;
            app.waitForUserAction();
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
        mybuttons.getChildren().addAll(addButton, cancelButton);

        Scene scene = new Scene(layout, 300, 180);
        setScene(scene);
    }
}
