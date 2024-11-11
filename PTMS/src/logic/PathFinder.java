package logic;

import java.io.Serializable;
import java.util.*;

public class PathFinder implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3412902586326110702L;
	private final Graph graph;
    private final int V;

    public PathFinder(Graph graph, int vertices) {
        this.graph = graph;
        this.V = vertices;
    }

    class Distance implements Comparable<Distance> {
        int vertex;
        int distance;

        public Distance(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Distance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    // Implementación del algoritmo de Dijkstra, Arreglo para almacenar las distancias mínimas, Arreglo para almacenar el camino más corto
    public int[] dijkstra(int source, boolean useDistance) {
    
        int[] distances = new int[V];
     
        int[] parent = new int[V];
    
        boolean[] visited = new boolean[V];

        
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        
        PriorityQueue<Distance> pq = new PriorityQueue<>();
        
        
        distances[source] = 0;
        pq.add(new Distance(source, 0));

        while (!pq.isEmpty()) {
            // Obtener el vértice con la menor distancia
            int u = pq.poll().vertex;

            // Si ya fue visitado, continuar
            if (visited[u]) continue;
            visited[u] = true;

            // Obtener la lista de adyacencia del vértice actual
            LinkedList<Stop> neighbors = graph.getAdjList().get(u);
            
            // Para cada vecino del vértice actual
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                // Obtener la ruta entre u y v
                Route route = graph.getRoute(u, v);
                if (route == null) continue;

                
                int weight = useDistance ? route.getDistance() : route.getTravelTime();

                // Si encontramos un camino más corto
                if (!visited[v] && distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    // Actualizar la distancia
                    distances[v] = distances[u] + weight;
                    // Actualizar el padre
                    parent[v] = u;
                    // Agregar a la cola de prioridad
                    pq.add(new Distance(v, distances[v]));
                }
            }
        }

        return parent;
    }

    // Método para imprimir el camino más corto
    public void printPath(int[] parent, int dest) {
        LinkedList<Integer> path = new LinkedList<>();
        int current = dest;

        // Reconstruir el camino desde el destino hasta la fuente
        while (current != -1) {
            path.addFirst(current);
            current = parent[current];
        }

        // Imprimir el camino
        System.out.println("Ruta más corta:");
        for (int i = 0; i < path.size(); i++) {
            Stop stop = graph.getStop(path.get(i));
            System.out.print(stop.getLabel());
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}