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
import logic.Graph;
import logic.PTMS;

public class GraphDialog extends Stage{
	
	private TextField labelField;
	
	private Button saveButton;
	private Button cancelButton;
	
	public GraphDialog(MainScreen app, int update) {
		if(update == 0) setTitle("Añadir Mapa");
		if(update == 1) setTitle("Editar "+ PTMS.getInstance().getGraph().getLabel());
        initModality(Modality.APPLICATION_MODAL);
  
        labelField = new TextField("");
        if(update == 1) labelField.setText(PTMS.getInstance().getGraph().getLabel());
        if(update == 0) saveButton = new Button("Agregar");
        if(update == 1) saveButton = new Button("Guardar");
        cancelButton = new Button("Cancelar");
        
        // Set action for the buttons
        saveButton.setOnAction(e -> {
        	if(update == 0) {
        		Graph newGraph = new Graph(PTMS.getInstance().generateGraphID(), labelField.getText());
            	app.addGraph(newGraph);
                close();
        	}
        	if(update == 1) {
        		Graph editGraph = PTMS.getInstance().getGraph();
        		editGraph.setLabel(labelField.getText());
        		app.editGraph(editGraph);
        		close();
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
        	new Label("Nombre:"), labelField,
            mybuttons
        );
        mybuttons.getChildren().addAll(saveButton, cancelButton);

        Scene scene = new Scene(layout, 300, 120);
        setScene(scene);
	}
	
}
