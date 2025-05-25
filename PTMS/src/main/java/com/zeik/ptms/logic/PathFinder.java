package com.zeik.ptms.logic;

import java.util.*;

public class PathFinder {
    private final Graph graph;
    
    public PathFinder(Graph graph) {
        this.graph = graph;
    }

    public List<Stop> dijkstra_shortest(Stop start, Stop end) {
        updateAllEvents();
        
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();
        
        // Min-Heap para procesar los nodos en orden de distancia
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        
        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);
        pq.add(start);
        
        // Procesar la cola de prioridad
        while (!pq.isEmpty()) {
            Stop current = pq.poll();

            // Si llegamos al destino, terminamos
            if (current.equals(end)) {
                break;
            }

            // Procesar vecinos del nodo actual
            for (Stop neighbor : graph.getNeighbors(current)) {
                Route route = graph.getRoute(current, neighbor);
                if (route != null) {
                    double newDist = distances.get(current) + route.getDistance();
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previous.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }
        }
        
        // Reconstruir el camino desde el mapa de predecesores
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        
        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }
        
        return path;
    }
    
    public List<Stop> dijkstra_fastest(Stop start, Stop end) {
        updateAllEvents();
        
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();
        
        // Min-Heap para procesar los nodos en orden de distancia
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        
        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);
        pq.add(start);
        
        // Procesar la cola de prioridad
        while (!pq.isEmpty()) {
            Stop current = pq.poll();

            // Si llegamos al destino, terminamos
            if (current.equals(end)) {
                break;
            }

            // Procesar vecinos del nodo actual
            for (Stop neighbor : graph.getNeighbors(current)) {
                Route route = graph.getRoute(current, neighbor);
                if (route != null) {
                    double newDist = distances.get(current) + route.getTravelTime();
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previous.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }
        }
        
        // Reconstruir el camino desde el mapa de predecesores
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        
        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }
        
        return path;
    }
    
    public List<Stop> dijkstra_transferless(Stop start, Stop end) {
        updateAllEvents();
        
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();
        
        // Min-Heap para procesar los nodos en orden de distancia
        PriorityQueue<Stop> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        
        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);
        pq.add(start);
        
        // Procesar la cola de prioridad
        while (!pq.isEmpty()) {
            Stop current = pq.poll();

            // Si llegamos al destino, terminamos
            if (current.equals(end)) {
                break;
            }

            // Procesar vecinos del nodo actual
            for (Stop neighbor : graph.getNeighbors(current)) {
                Route route = graph.getRoute(current, neighbor);
                if (route != null) {
                    double newDist = distances.get(current) + route.getTranship();
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previous.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }
        }
        
        // Reconstruir el camino desde el mapa de predecesores
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        
        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }
        
        return path;
    }
    
    public List<Stop> bellmanFord_shortest(Stop start, Stop end) {
    	updateAllEvents();
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();

        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);

        // Obtener todas las aristas del grafo
        List<Route> allRoutes = new ArrayList<>();
        for (LinkedList<Stop> stops : graph.getAdjList()) {
            Stop from = stops.get(0);
            for (int i = 1; i < stops.size(); i++) {
                Stop to = stops.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    allRoutes.add(route);
                }
            }
        }

        // Relajación de las aristas |V| - 1 veces
        for (int i = 1; i < distances.size(); i++) {
            for (Route route : allRoutes) {
                Stop from = route.getSrc(); // Obtener nodo origen
                Stop to = route.getDest();   // Obtener nodo destino

                if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getDistance() < distances.get(to)) {
                    distances.put(to, distances.get(from) + route.getDistance());
                    previous.put(to, from);
                }
            }
        }

        // Verificar ciclos negativos
        for (Route route : allRoutes) {
            Stop from = route.getSrc();
            Stop to = route.getDest();

            if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getDistance() < distances.get(to)) {
                System.out.println("El grafo contiene ciclos negativos.");
                return Collections.emptyList(); // Ciclo negativo detectado
            }
        }

        // Reconstruir el camino desde el mapa de predecessors
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }

        return path;
    }
    
    public List<Stop> bellmanFord_fastest(Stop start, Stop end) {
    	updateAllEvents();
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();

        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);

        // Obtener todas las aristas del grafo
        List<Route> allRoutes = new ArrayList<>();
        for (LinkedList<Stop> stops : graph.getAdjList()) {
            Stop from = stops.get(0);
            for (int i = 1; i < stops.size(); i++) {
                Stop to = stops.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    allRoutes.add(route);
                }
            }
        }

        // Relajación de las aristas |V| - 1 veces
        for (int i = 1; i < distances.size(); i++) {
            for (Route route : allRoutes) {
                Stop from = route.getSrc(); // Obtener nodo origen
                Stop to = route.getDest();   // Obtener nodo destino

                if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getTravelTime() < distances.get(to)) {
                    distances.put(to, distances.get(from) + route.getTravelTime());
                    previous.put(to, from);
                }
            }
        }

        // Verificar ciclos negativos
        for (Route route : allRoutes) {
            Stop from = route.getSrc();
            Stop to = route.getDest();

            if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getTravelTime() < distances.get(to)) {
                System.out.println("El grafo contiene ciclos negativos.");
                return Collections.emptyList(); // Ciclo negativo detectado
            }
        }

        // Reconstruir el camino desde el mapa de predecessors
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }

        return path;
    }
    
    public List<Stop> bellmanFord_transferless(Stop start, Stop end) {
    	updateAllEvents();
        // Distancia mínima de cada nodo desde el inicio
        Map<Stop, Double> distances = new HashMap<>();
        // Nodo previo para reconstruir el camino
        Map<Stop, Stop> previous = new HashMap<>();

        // Inicializar distancias
        for (LinkedList<Stop> list : graph.getAdjList()) {
            for (Stop stop : list) {
                distances.put(stop, Double.MAX_VALUE); // Infinito
                previous.put(stop, null);
            }
        }
        distances.put(start, 0.0);

        // Obtener todas las aristas del grafo
        List<Route> allRoutes = new ArrayList<>();
        for (LinkedList<Stop> stops : graph.getAdjList()) {
            Stop from = stops.get(0);
            for (int i = 1; i < stops.size(); i++) {
                Stop to = stops.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    allRoutes.add(route);
                }
            }
        }

        // Relajación de las aristas |V| - 1 veces
        for (int i = 1; i < distances.size(); i++) {
            for (Route route : allRoutes) {
                Stop from = route.getSrc(); // Obtener nodo origen
                Stop to = route.getDest();   // Obtener nodo destino

                if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getTranship() < distances.get(to)) {
                    distances.put(to, distances.get(from) + route.getTranship());
                    previous.put(to, from);
                }
            }
        }

        // Verificar ciclos negativos
        for (Route route : allRoutes) {
            Stop from = route.getSrc();
            Stop to = route.getDest();

            if (distances.get(from) != Double.MAX_VALUE && distances.get(from) + route.getTranship() < distances.get(to)) {
                System.out.println("El grafo contiene ciclos negativos.");
                return Collections.emptyList(); // Ciclo negativo detectado
            }
        }

        // Reconstruir el camino desde el mapa de predecessors
        List<Stop> path = new ArrayList<>();
        for (Stop at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        // Si el inicio no está conectado con el final
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList(); // Camino no encontrado
        }

        return path;
    }
    
    public List<Stop> floydWarshall_shortest(Stop start, Stop end) {
    	updateAllEvents();
        // Lista de nodos
        List<Stop> stops = graph.getStops();
        int n = stops.size();

        // Inicializar las matrices de distancias y predecesores
        double[][] dist = new double[n][n];
        Stop[][] pred = new Stop[n][n];

        // Mapear cada parada a un índice
        Map<Stop, Integer> stopIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            stopIndexMap.put(stops.get(i), i);
        }

        // Inicializar matrices de distancias y predecesores
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0.0; // Distancia de un nodo a sí mismo
                } else {
                    dist[i][j] = Double.MAX_VALUE; // Infinito
                }
                pred[i][j] = null; // Sin predecesor al inicio
            }
        }

        // Poblar las distancias iniciales basadas en las rutas
        for (LinkedList<Stop> adjacencyList : graph.getAdjList()) {
            Stop from = adjacencyList.get(0);
            for (int i = 1; i < adjacencyList.size(); i++) {
                Stop to = adjacencyList.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    int fromIndex = stopIndexMap.get(from);
                    int toIndex = stopIndexMap.get(to);
                    dist[fromIndex][toIndex] = route.getDistance();
                    pred[fromIndex][toIndex] = from;
                }
            }
        }

        // Aplicar Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pred[i][j] = pred[k][j];
                    }
                }
            }
        }

        // Reconstruir el camino desde la matriz de predecesores
        int startIdx = stopIndexMap.get(start);
        int endIdx = stopIndexMap.get(end);

        if (dist[startIdx][endIdx] == Double.MAX_VALUE) {
            return Collections.emptyList(); // No hay camino
        }

        List<Stop> path = new ArrayList<>();
        Stop current = end;
        while (current != null) {
            path.add(current);
            current = pred[stopIndexMap.get(start)][stopIndexMap.get(current)];
        }

        Collections.reverse(path);
        return path;
    }
    
    public List<Stop> floydWarshall_fastest(Stop start, Stop end) {
    	updateAllEvents();
        // Lista de nodos
        List<Stop> stops = graph.getStops();
        int n = stops.size();

        // Inicializar las matrices de distancias y predecesores
        double[][] dist = new double[n][n];
        Stop[][] pred = new Stop[n][n];

        // Mapear cada parada a un índice
        Map<Stop, Integer> stopIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            stopIndexMap.put(stops.get(i), i);
        }

        // Inicializar matrices de distancias y predecesores
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0.0; // Distancia de un nodo a sí mismo
                } else {
                    dist[i][j] = Double.MAX_VALUE; // Infinito
                }
                pred[i][j] = null; // Sin predecesor al inicio
            }
        }

        // Poblar las distancias iniciales basadas en las rutas
        for (LinkedList<Stop> adjacencyList : graph.getAdjList()) {
            Stop from = adjacencyList.get(0);
            for (int i = 1; i < adjacencyList.size(); i++) {
                Stop to = adjacencyList.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    int fromIndex = stopIndexMap.get(from);
                    int toIndex = stopIndexMap.get(to);
                    dist[fromIndex][toIndex] = route.getTravelTime();
                    pred[fromIndex][toIndex] = from;
                }
            }
        }

        // Aplicar Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pred[i][j] = pred[k][j];
                    }
                }
            }
        }

        // Reconstruir el camino desde la matriz de predecesores
        int startIdx = stopIndexMap.get(start);
        int endIdx = stopIndexMap.get(end);

        if (dist[startIdx][endIdx] == Double.MAX_VALUE) {
            return Collections.emptyList(); // No hay camino
        }

        List<Stop> path = new ArrayList<>();
        Stop current = end;
        while (current != null) {
            path.add(current);
            current = pred[stopIndexMap.get(start)][stopIndexMap.get(current)];
        }

        Collections.reverse(path);
        return path;
    }
    
    public List<Stop> floydWarshall_transferless(Stop start, Stop end) {
    	updateAllEvents();
        // Lista de nodos
        List<Stop> stops = graph.getStops();
        int n = stops.size();

        // Inicializar las matrices de distancias y predecesores
        double[][] dist = new double[n][n];
        Stop[][] pred = new Stop[n][n];

        // Mapear cada parada a un índice
        Map<Stop, Integer> stopIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            stopIndexMap.put(stops.get(i), i);
        }

        // Inicializar matrices de distancias y predecesores
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0.0; // Distancia de un nodo a sí mismo
                } else {
                    dist[i][j] = Double.MAX_VALUE; // Infinito
                }
                pred[i][j] = null; // Sin predecesor al inicio
            }
        }

        // Poblar las distancias iniciales basadas en las rutas
        for (LinkedList<Stop> adjacencyList : graph.getAdjList()) {
            Stop from = adjacencyList.get(0);
            for (int i = 1; i < adjacencyList.size(); i++) {
                Stop to = adjacencyList.get(i);
                Route route = graph.getRoute(from, to);
                if (route != null) {
                    int fromIndex = stopIndexMap.get(from);
                    int toIndex = stopIndexMap.get(to);
                    dist[fromIndex][toIndex] = route.getTranship();
                    pred[fromIndex][toIndex] = from;
                }
            }
        }

        // Aplicar Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pred[i][j] = pred[k][j];
                    }
                }
            }
        }

        // Reconstruir el camino desde la matriz de predecesores
        int startIdx = stopIndexMap.get(start);
        int endIdx = stopIndexMap.get(end);

        if (dist[startIdx][endIdx] == Double.MAX_VALUE) {
            return Collections.emptyList(); // No hay camino
        }

        List<Stop> path = new ArrayList<>();
        Stop current = end;
        while (current != null) {
            path.add(current);
            current = pred[stopIndexMap.get(start)][stopIndexMap.get(current)];
        }

        Collections.reverse(path);
        return path;
    }
    
    public Map<Stop, List<Route>> kruskal_shortest() {
        // Lista de todos los nodos y aristas
        List<Stop> nodes = graph.getStops();
        List<Route> edges = graph.getRoutes();

        // Ordenar las aristas por peso
        edges.sort(Comparator.comparingDouble(Route::getDistance));

        // Inicializar estructuras para el MST y el conjunto disjunto
        DisjointSet ds = new DisjointSet(nodes);
        Map<Stop, List<Route>> mst = new HashMap<>();
        for (Stop node : nodes) {
            mst.put(node, new ArrayList<>());
        }

        // Construir el MST
        for (Route edge : edges) {
            Stop from = edge.getSrc();
            Stop to = edge.getDest();

            if (ds.find(from) != ds.find(to)) {
                ds.union(from, to);
                mst.get(from).add(edge);
                mst.get(to).add(edge); // Para grafos dirigidos
            }
        }

        return mst;
    }
    
    public Map<Stop, List<Route>> kruskal_fastest() {
        // Lista de todos los nodos y aristas
        List<Stop> nodes = graph.getStops();
        List<Route> edges = graph.getRoutes();

        // Ordenar las aristas por peso
        edges.sort(Comparator.comparingInt(Route::getTravelTime));

        // Inicializar estructuras para el MST y el conjunto disjunto
        DisjointSet ds = new DisjointSet(nodes);
        Map<Stop, List<Route>> mst = new HashMap<>();
        for (Stop node : nodes) {
            mst.put(node, new ArrayList<>());
        }

        // Construir el MST
        for (Route edge : edges) {
            Stop from = edge.getSrc();
            Stop to = edge.getDest();

            if (ds.find(from) != ds.find(to)) {
                ds.union(from, to);
                mst.get(from).add(edge);
                mst.get(to).add(edge); // Para grafos dirigidos
            }
        }

        return mst;
    }
    
    public Map<Stop, List<Route>> kruskal_transferless() {
        // Lista de todos los nodos y aristas
        List<Stop> nodes = graph.getStops();
        List<Route> edges = graph.getRoutes();

        // Ordenar las aristas por peso
        edges.sort(Comparator.comparingInt(Route::getTranship));

        // Inicializar estructuras para el MST y el conjunto disjunto
        DisjointSet ds = new DisjointSet(nodes);
        Map<Stop, List<Route>> mst = new HashMap<>();
        for (Stop node : nodes) {
            mst.put(node, new ArrayList<>());
        }

        // Construir el MST
        for (Route edge : edges) {
            Stop from = edge.getSrc();
            Stop to = edge.getDest();

            if (ds.find(from) != ds.find(to)) {
                ds.union(from, to);
                mst.get(from).add(edge);
                mst.get(to).add(edge); // Para grafos dirigidos
            }
        }

        return mst;
    }
    
    public List<Stop> kruskalMST_shortest(Stop start, Stop end) {
    	updateAllEvents();
    	Map<Stop, List<Route>> mst = kruskal_shortest();
        List<Stop> path = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();
        if (dfsMST(start, end, mst, path, visited)) {
            return path;
        }
        return Collections.emptyList(); // No se encontró un camino
    }
    
    public List<Stop> kruskalMST_fastest(Stop start, Stop end) {
    	updateAllEvents();
    	Map<Stop, List<Route>> mst = kruskal_fastest();
        List<Stop> path = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();
        if (dfsMST(start, end, mst, path, visited)) {
            return path;
        }
        return Collections.emptyList(); // No se encontró un camino
    }
    
    public List<Stop> kruskalMST_transferless(Stop start, Stop end) {
    	updateAllEvents();
    	Map<Stop, List<Route>> mst = kruskal_transferless();
        List<Stop> path = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();
        if (dfsMST(start, end, mst, path, visited)) {
            return path;
        }
        return Collections.emptyList(); // No se encontró un camino
    }

    private boolean dfsMST(Stop current, Stop end, Map<Stop, List<Route>> mst, List<Stop> path, Set<Stop> visited) {
        path.add(current);
        visited.add(current);

        if (current.equals(end)) {
            return true;
        }

        for (Route route : mst.get(current)) {
            Stop neighbor = route.getDest();
            if (!visited.contains(neighbor)) {
                if (dfsMST(neighbor, end, mst, path, visited)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private void updateAllEvents() {
        for (Map.Entry<String, Route> entry : graph.getRoutesMap().entrySet()) {
            Route route = entry.getValue();
            route.updateEvent();
        }
    }

    public void printPath(List<Stop> path) {
        
    	if (path == null || path.isEmpty()) {
            System.out.println("No se encontró un camino.");
            return;
        }
    	
    	int totalTime = 0;
        int totalDistance = 0;
        int totalTranship = 0;
        
        System.out.print("Camino más corto: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getLabel());
            if (i < path.size() - 1) {
                System.out.print(" -> "); // Separador entre paradas

                // Obtener la ruta entre la parada actual y la siguiente
                Route route = graph.getRoute(path.get(i), path.get(i + 1));
                if (route != null) {
                    totalDistance += route.getDistance();
                    totalTime += route.getTravelTime();
                    totalTranship++;
                }
            }
        }

        // Imprimir la distancia y el tiempo total
        System.out.println();
        System.out.println("Distancia total recorrida: " + totalDistance + " unidades.");
        System.out.println("Tiempo total de viaje: " + totalTime + " minutos.");
        System.out.println("Transbordos totales: " + (totalTranship-1) + " transbordos.");

    }
        
    public int[] getPathDetails(List<Stop> path) {
    	int totalDistance = 0;
    	int totalTime = 0;
    	int totalTranship = 0;
    	
    	for (int i = 0; i < path.size(); i++) {
            if (i < path.size() - 1) {
                // Obtener la ruta entre la parada actual y la siguiente
                Route route = graph.getRoute(path.get(i), path.get(i + 1));
                if (route != null) {
                	totalDistance += route.getDistance();
                	totalTime += route.getTravelTime();
                	totalTranship++;
                }
            }
        }
    	
    	return new int[] {totalDistance, totalTime, totalTranship-1};
    }
    
    public List<String> getRouteDetails(List<Stop> path){
    	
    	List<String> routeDetails = new ArrayList<String>();
    	routeDetails.add("");
    	routeDetails.add("Eventos Ocurrentes (En todas las rutas):");
    	routeDetails.add("");
    	
    	for(Route r : PTMS.getInstance().getGraph().getRoutes()) {
    		if(r.getCurrentEvent().getType().getDescription() != "Normal") {
    			routeDetails.add("En la ruta "+r.getLabel());
        		routeDetails.add("Desde "+r.getSrc().getLabel()+" hasta "+r.getDest().getLabel());
            	routeDetails.add("Se produjo un evento: "+r.getCurrentEvent().getType().getDescription());
            	routeDetails.add("Factor de Retraso: "+r.getCurrentEvent().getType().getFactor());
            	routeDetails.add("");
            	System.out.println("En la ruta "+r.getLabel()+" que va desde "+r.getSrc().getLabel()+" hasta "+r.getDest().getLabel()+" se produjo un/a "+r.getCurrentEvent().getType().getDescription());
            	System.out.println("Factor de Retraso: "+r.getCurrentEvent().getType().getFactor());
    		}else {
    			routeDetails.add("En la ruta "+r.getLabel());
        		routeDetails.add("Desde "+r.getSrc().getLabel()+" hasta "+r.getDest().getLabel());
        		routeDetails.add("No se produjeron eventos");
        		routeDetails.add("Factor de Retraso: "+r.getCurrentEvent().getType().getFactor());
        		routeDetails.add("");
    		}
    	}
    	
    	routeDetails.add("Camino encontrado:");
    	String writedPath = "";
    	for (int i = 0; i < path.size(); i++) {
            writedPath += path.get(i).getLabel();
    		if (i < path.size() - 1) {
            	writedPath += " -> ";
            }
        }
    	routeDetails.add(writedPath);
    	routeDetails.add("");
    	return routeDetails;
    	
    }
    
    public List<String> getAlternativePath(List<Stop> path, String algorithm){
    	List<String> routeDetails = new ArrayList<String>();
    	
    	routeDetails.add("Camino Alternativo (utilizando "+ algorithm + ")");
    	String writedPath = "";
    	for (int i = 0; i < path.size(); i++) {
            writedPath += path.get(i).getLabel();
    		if (i < path.size() - 1) {
            	writedPath += " -> ";
            }
        }
    	routeDetails.add(writedPath);
    	routeDetails.add("");
    	return routeDetails;
    }
    
}