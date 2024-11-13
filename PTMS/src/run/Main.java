package run;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Graph;
import logic.PTMS;
import logic.PathFinder;
import logic.Stop;
import visual.MainScreen;

public class Main{
	
	
	public static void main(String[] args) {

		PTMS.getInstance().loadPTMS();
		Graph graph = PTMS.getInstance().getGraph();
		
		
		// El grafo guardado es el mismo de la siguiente prueba comentada:
		/*
		
		Graph graph = new Graph();
				
		graph.addStop(new Stop(PTMS.getInstance().generateStopID(), "La Vega"));
		graph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Santiago"));
		graph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Santo Domingo"));
		
		graph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 1, 2, 0, 1, "Ruta G"));
		graph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 1, 2, 0, 2, "Ruta M"));
		graph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 1, 2, 1, 2, "Ruta L"));
		graph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 1, 2, 2, 0, "Ruta T"));
		graph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 2, 1, 1, 0, "Ruta A"));
		graph.deleteRoute(0, 2);
		graph.modifyRoute(new Route("R-4", 1, 2, 3, 2, 1, "Ruta T"));
		
		PTMS.getInstance().setGraph(graph);
		PTMS.getInstance().savePTMS();
		*/
		
		/*
		PathFinder pathFinder = new PathFinder(graph, 3); // 3 es el número de vértices, se puede cambiar por cualquier numero positivo

		// Encontrar la ruta más corta por distancia desde el vértice 0
		int[] parentDistance = pathFinder.dijkstra(0, true);
		System.out.println("Ruta más corta por distancia desde La Vega:");
		pathFinder.printPath(parentDistance, 2); // 2 es el destino (Santo Domingo)

		// Encontrar la ruta más corta por tiempo de viaje desde el vértice 0
		int[] parentTime = pathFinder.dijkstra(0, false);
		System.out.println("Ruta más corta por tiempo desde La Vega:");
		pathFinder.printPath(parentTime, 2);
		
		 System.out.println("\nCalculando ruta más corta por tiempo (considerando condiciones):");
	     parentTime = pathFinder.dijkstra(0, false);
	     pathFinder.printPath(parentTime, 2);

	     System.out.println("\nCalculando ruta más corta por distancia (sin considerar condiciones):");
	     parentDistance = pathFinder.dijkstra(0, true);
	     pathFinder.printPath(parentDistance, 2);
		
		System.out.println("______________");
		System.out.println("Grafo completo");
		graph.print();
		*/
		
		MainScreen.launch(MainScreen.class,args);
	}
}
