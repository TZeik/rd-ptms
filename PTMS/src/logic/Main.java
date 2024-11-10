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
		
		graph.print();

	}

}
