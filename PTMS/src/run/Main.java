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
import logic.Route;
import logic.Stop;
import visual.MainScreen;

public class Main{
	
	
	public static void main(String[] args) {

		PTMS.getInstance().loadPTMS();
		
		/*
		
		// Test Subjects
		Graph testGraph = new Graph();
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Santiago"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Espaillat"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "La Vega"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Santo Domingo"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Montecristi"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Jarabacoa"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "La Romana"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "Samana"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "A"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "B"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "C"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "D"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "E"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "F"));
		testGraph.addStop(new Stop(PTMS.getInstance().generateStopID(), "G"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 3, 0, 1, "A"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 7, 0, 2, "B"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 2, 0, 3, "C"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 6, 1, 4, "D"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 4, 1, 5, "E"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 5, 2, 6, "F"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 8, 2, 7, "G"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 9, 3, 8, "H"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 3, 4, 9, "I"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 1, 5, 10, "J"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 2, 6, 11, "K"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 10, 7, 12, "L"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 6, 8, 13, "M"));
		testGraph.addRoute(new Route(PTMS.getInstance().generateRouteID(), 4, 14, 4, "N"));
		
		PTMS.getInstance().setGraph(testGraph);
		
		PathFinder pathFinder = new PathFinder(PTMS.getInstance().getGraph(), 15); // 3 es el número de vértices, se puede cambiar por cualquier numero positivo

		// Encontrar la ruta más corta por distancia desde el vértice 0
		int[] parentDistance = pathFinder.dijkstra(0, true);
		System.out.println("Ruta más corta por distancia desde");
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
		PTMS.getInstance().getGraph().print();
		*/
		
		MainScreen.launch(MainScreen.class,args);
	}
}
