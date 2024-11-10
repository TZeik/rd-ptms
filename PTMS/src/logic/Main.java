package logic;

public class Main {

	public static void main(String[] args) {
		
		Graph graph = new Graph();
		
		graph.addStop(new Stop(0, "La Vega"));
		graph.addStop(new Stop(1, "Santiago"));
		graph.addStop(new Stop(2, "Santo Domingo"));
		
		graph.addEdge(new Route(1, 1, 1, 2, 0, 1, "Ruta G"));
		graph.addEdge(new Route(1, 1, 1, 2, 0, 2, "Ruta M"));
		graph.addEdge(new Route(1, 1, 1, 2, 1, 2, "Ruta L"));
		graph.addEdge(new Route(1, 1, 1, 2, 2, 0, "Ruta T"));
		
		PathFinder pathFinder = new PathFinder(graph, 3); // 3 es el número de vértices, se puede cambiar por cualquier numero positivo

		// Encontrar la ruta más corta por distancia desde el vértice 0
		int[] parentDistance = pathFinder.dijkstra(0, true);
		System.out.println("Ruta más corta por distancia desde La Vega:");
		pathFinder.printPath(parentDistance, 2); // 2 es el destino (Santo Domingo)

		// Encontrar la ruta más corta por tiempo de viaje desde el vértice 0
		int[] parentTime = pathFinder.dijkstra(0, false);
		System.out.println("Ruta más corta por tiempo desde La Vega:");
		pathFinder.printPath(parentTime, 2);
		
		graph.print();

	}

}
