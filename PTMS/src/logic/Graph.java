package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Graph implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7910866032838849710L;
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
        PTMS.getInstance().setStopIdGenerator(PTMS.getInstance().getStopIdGenerator()+1);
    }

    public void addRoute(Route route) {
        LinkedList<Stop> currentList = adjList.get(route.getSrc());
        Stop dstStop = adjList.get(route.getDest()).get(0);
        currentList.add(dstStop);
        
        // Almacenar la ruta en el mapa
        String routeKey = route.getId();
        routes.put(routeKey, route);
        
        PTMS.getInstance().setRouteIdGenerator(PTMS.getInstance().getRouteIdGenerator()+1);
    }
    
    public void deleteStop(int index) {
    	adjList.remove(index);
    	this.stops.remove(index);
    }
    
    // Eliminar una ruta en la lista de adyacencia y en el mapa
    public void deleteRoute(int src, int dest) {
    	LinkedList<Stop> currentList = adjList.get(src);
    	Stop targetStop = adjList.get(dest).get(0);
    	currentList.remove(targetStop);
    	String routeKey = searchRouteId(src, dest);
    	routes.remove(routeKey);
    }
    
    // Se modifica una parada
    public void modifyStop(Stop update) {
    	for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
            	if(stop.getId().equals(update.getId())) {
            		stop = update;
            		return;
            	}
            }
    	}
    }
    
    public void modifyRoute(Route update) {
    	String routeKey = update.getId();
    	Route oldRoute = routes.get(routeKey);
    	if(oldRoute != null) {
    		routes.put(routeKey, update);
        	LinkedList<Stop> currentList = adjList.get(oldRoute.getSrc());
        	Stop targetStop = adjList.get(oldRoute.getDest()).get(0);
        	int index = currentList.indexOf(targetStop);
        	currentList.remove(targetStop);
        	currentList = adjList.get(update.getSrc());
        	targetStop = adjList.get(update.getDest()).get(0);
        	currentList.add(index, targetStop);
    	}	
    }
    
    public Route getRoute(int src, int dest) {
        String routeKey = searchRouteId(src, dest);
        return routes.get(routeKey);
    }

    public Stop getStop(int id) {
        return stops.get(id);
    }

    public ArrayList<LinkedList<Stop>> getAdjList() {
        return adjList;
    }
    
    public Stop searchStop(String id) {
    	for(LinkedList<Stop> currentList : adjList) {
    		if(currentList.getFirst().getId().equals(id)) {
    			return currentList.getFirst();
    		}
    	}
    	return null;
    }
    
    public String searchRouteId(int src, int dest) {
    	for(Entry<String, Route> entry : routes.entrySet()) {
    		if(entry.getValue().getSrc() == src && entry.getValue().getDest() == dest) {
    			return entry.getKey();
    		}
    	}
    	return "0";
    }

    public void print() {
        for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
            	System.out.print(stop.getLabel());
            	if(currentList.indexOf(stop) != currentList.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }
}