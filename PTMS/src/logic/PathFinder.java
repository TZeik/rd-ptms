package logic;

import java.util.*;

public class PathFinder {
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

    // Clase para representar aristas para Kruskal
    class Edge implements Comparable<Edge> {
        int src, dest, weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    // Clase para conjunto disjunto (usado en Kruskal)
    class DisjointSet {
        int[] parent, rank;

        public DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else if (rank[xRoot] > rank[yRoot]) {
                parent[yRoot] = xRoot;
            } else {
                parent[yRoot] = xRoot;
                rank[xRoot]++;
            }
        }
    }

    // Algoritmo de Dijkstra
    public int[] dijkstra(int source, boolean useDistance) {
        // Actualizar eventos aleatorios antes de calcular la ruta
        updateAllEvents();
        
        int[] distances = new int[V];
        int[] parent = new int[V];
        boolean[] visited = new boolean[V];

        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Distance> pq = new PriorityQueue<>();
        distances[source] = 0;
        pq.add(new Distance(source, 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().vertex;
            if (visited[u]) continue;
            visited[u] = true;

            LinkedList<Stop> neighbors = graph.getAdjList().get(u);
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                Route route = graph.getRoute(u, v);
                if (route == null) continue;

                int weight = useDistance ? route.getDistance() : route.getAdjustedTravelTime();

                if (!visited[v] && distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    parent[v] = u;
                    pq.add(new Distance(v, distances[v]));
                }
            }
        }
        return parent;
    }

    // Método para actualizar eventos de todas las rutas
    private void updateAllEvents() {
                for (Map.Entry<String, Route> entry : graph.getRoutes().entrySet()) {
                    Route route = entry.getValue();
                    route.updateEvent();
        }
    }

    // Método modificado para mostrar información sobre eventos
    public void printPath(int[] parent, int dest) {
        LinkedList<Integer> path = new LinkedList<>();
        int current = dest;
        int totalTime = 0;
        int totalDistance = 0;

        while (current != -1) {
            path.addFirst(current);
            if (parent[current] != -1) {
                Route route = graph.getRoute(parent[current], current);
                totalTime += route.getAdjustedTravelTime();
                totalDistance += route.getDistance();
            }
            current = parent[current];
        }

        System.out.println("Ruta más corta:");
        
        
        for (int i = 0; i < path.size(); i++) {
            Stop stop = graph.getStop(path.get(i));
            System.out.print(stop.getLabel());
            if (i < path.size() - 1) {
                Route route = graph.getRoute(path.get(i), path.get(i + 1));
                System.out.print(" -> [" + route.getCurrentEvent() + "] -> ");
            }
        }
        
        System.out.println("\nTiempo total de viaje (con eventos): " + totalTime + " minutos");
        System.out.println("Distancia total: " + totalDistance + " km");
    }

    // Algoritmo de Bellman-Ford
    public int[] bellmanFord(int source, boolean useDistance) {
        int[] distances = new int[V];
        int[] parent = new int[V];
        
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        distances[source] = 0;

        
        for (int i = 0; i < V - 1; i++) {
            for (int u = 0; u < V; u++) {
                LinkedList<Stop> neighbors = graph.getAdjList().get(u);
                for (Stop stop : neighbors) {
                    int v = neighbors.indexOf(stop);
                    Route route = graph.getRoute(u, v);
                    if (route == null) continue;

                    int weight = useDistance ? route.getDistance() : route.getTravelTime();

                    if (distances[u] != Integer.MAX_VALUE && 
                        distances[u] + weight < distances[v]) {
                        distances[v] = distances[u] + weight;
                        parent[v] = u;
                    }
                }
            }
        }

        // Verificar ciclos negativos
        for (int u = 0; u < V; u++) {
            LinkedList<Stop> neighbors = graph.getAdjList().get(u);
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                Route route = graph.getRoute(u, v);
                if (route == null) continue;

                int weight = useDistance ? route.getDistance() : route.getTravelTime();

                if (distances[u] != Integer.MAX_VALUE && 
                    distances[u] + weight < distances[v]) {
                    System.out.println("El grafo contiene un ciclo negativo");
                    return null;
                }
            }
        }

        return parent;
    }

    // Algoritmo de Floyd-Warshall
    public int[][] floydWarshall(boolean useDistance) {
        int[][] dist = new int[V][V];
        int[][] next = new int[V][V]; // Para reconstruir el camino

        // Inicializar matrices
        for (int i = 0; i < V; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
        }

        // Llenar con valores directos
        for (int u = 0; u < V; u++) {
            LinkedList<Stop> neighbors = graph.getAdjList().get(u);
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                Route route = graph.getRoute(u, v);
                if (route == null) continue;

                int weight = useDistance ? route.getDistance() : route.getTravelTime();
                dist[u][v] = weight;
                next[u][v] = v;
            }
        }

        // Algoritmo principal de busqueda
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && 
                        dist[k][j] != Integer.MAX_VALUE && 
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        return next;
    }

    // Algoritmo de Prim
    public List<Edge> prim(boolean useDistance) {
        List<Edge> mst = new ArrayList<>();
        boolean[] visited = new boolean[V];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        // Comenzar desde el vértice 0
        visited[0] = true;
        LinkedList<Stop> neighbors = graph.getAdjList().get(0);
        for (Stop stop : neighbors) {
            int v = neighbors.indexOf(stop);
            Route route = graph.getRoute(0, v);
            if (route == null) continue;

            int weight = useDistance ? route.getDistance() : route.getTravelTime();
            pq.add(new Edge(0, v, weight));
        }

        while (!pq.isEmpty() && mst.size() < V - 1) {
            Edge edge = pq.poll();
            if (visited[edge.dest]) continue;

            visited[edge.dest] = true;
            mst.add(edge);

            neighbors = graph.getAdjList().get(edge.dest);
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                if (!visited[v]) {
                    Route route = graph.getRoute(edge.dest, v);
                    if (route == null) continue;

                    int weight = useDistance ? route.getDistance() : route.getTravelTime();
                    pq.add(new Edge(edge.dest, v, weight));
                }
            }
        }

        return mst;
    }

    // Algoritmo de Kruskal
    public List<Edge> kruskal(boolean useDistance) {
        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        DisjointSet ds = new DisjointSet(V);

        // Agregar todas las aristas a la cola de prioridad
        for (int u = 0; u < V; u++) {
            LinkedList<Stop> neighbors = graph.getAdjList().get(u);
            for (Stop stop : neighbors) {
                int v = neighbors.indexOf(stop);
                Route route = graph.getRoute(u, v);
                if (route == null) continue;

                int weight = useDistance ? route.getDistance() : route.getTravelTime();
                pq.add(new Edge(u, v, weight));
            }
        }

        while (!pq.isEmpty() && mst.size() < V - 1) {
            Edge edge = pq.poll();
            
            int x = ds.find(edge.src);
            int y = ds.find(edge.dest);

            if (x != y) {
                mst.add(edge);
                ds.union(x, y);
            }
        }

        return mst;
    }

    // Método para imprimir el camino de Floyd-Warshall
    public void printFloydWarshallPath(int[][] next, int src, int dest) {
        if (next[src][dest] == -1) {
            System.out.println("No existe camino entre estos vértices");
            return;
        }

        List<Integer> path = new ArrayList<>();
        path.add(src);
        
        while (src != dest) {
            src = next[src][dest];
            path.add(src);
        }

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

    // Método para imprimir el árbol de expansión mínima
    public void printMST(List<Edge> mst) {
        System.out.println("Árbol de expansión mínima:");
        int totalWeight = 0;
        for (Edge edge : mst) {
            Stop srcStop = graph.getStop(edge.src);
            Stop destStop = graph.getStop(edge.dest);
            System.out.println(srcStop.getLabel() + " -> " + destStop.getLabel() + 
                             " (peso: " + edge.weight + ")");
            totalWeight += edge.weight;
        }
        System.out.println("Peso total: " + totalWeight);
    }
}