package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private ArrayList<LinkedList<Stop>> adjList;
    private Map<String, Route> routes; 
    private ArrayList<Stop> stops; 

    public Graph() {
        adjList = new ArrayList<>();
        routes = new HashMap<>();
        stops = new ArrayList<>();
    }

    public void addStop(Stop stop) {
        LinkedList<Stop> currentList = new LinkedList<>();
        currentList.add(stop);
        adjList.add(currentList);
        stops.add(stop);
    }

    public void addEdge(Route route) {
        LinkedList<Stop> currentList = adjList.get(route.getSrc());
        Stop dstStop = adjList.get(route.getDest()).get(0);
        currentList.add(dstStop);
        
        // Almacenar la ruta en el mapa
        String routeKey = route.getSrc() + "-" + route.getDest();
        routes.put(routeKey, route);
    }

    public Route getRoute(int src, int dest) {
        String routeKey = src + "-" + dest;
        return routes.get(routeKey);
    }

    public Stop getStop(int id) {
        return stops.get(id);
    }

    public ArrayList<LinkedList<Stop>> getAdjList() {
        return adjList;
    }

    public void print() {
        for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
                System.out.print(stop.getLabel() + " -> ");
            }
            System.out.println();
        }
    }
}