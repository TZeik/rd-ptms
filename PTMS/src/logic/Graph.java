package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Graph implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7910866032838849710L;
	private ArrayList<LinkedList<Stop>> adjList;
    private Map<String, Route> routes;

    public Graph() {
        adjList = new ArrayList<>();
        routes = new HashMap<>();
    }

    public void addStop(Stop stop) {
        LinkedList<Stop> currentList = new LinkedList<>();
        currentList.add(stop);
        adjList.add(currentList);
        PTMS.getInstance().setStopIdGenerator(PTMS.getInstance().getStopIdGenerator()+1);
    }

    public void addRoute(Route route) {
        LinkedList<Stop> currentList = adjList.get(PTMS.getInstance().getGraph().getStopIndex(route.getSrc()));
        Stop dstStop = adjList.get(PTMS.getInstance().getGraph().getStopIndex(route.getDest())).get(0);
        currentList.add(dstStop);
        
        // Almacenar la ruta en el mapa
        String routeKey = route.getId();
        routes.put(routeKey, route);
        
        PTMS.getInstance().setRouteIdGenerator(PTMS.getInstance().getRouteIdGenerator()+1);
    }
    
    public void deleteStop(Stop stop) {
    	LinkedList<Stop> deletionList = null;
    	
    	for(LinkedList<Stop> currentList : adjList) {
    		if(currentList.indexOf(stop) == 0) {
				deletionList = currentList;
    		}
    		for(Stop s : currentList) {
    			if(s.equals(stop) && currentList.indexOf(stop) != 0) {
    					deleteRoute(currentList.get(0), stop);
    				}
    			}
    		}
    	
    	while(deletionList.isEmpty() == false && deletionList != null) {
    		if(deletionList.getLast() == stop) {
    			break;
    		}
    		String routeKey = searchRouteId(stop, deletionList.getLast());
    		routes.remove(routeKey);
    		deletionList.removeLast();
    	}
    	adjList.remove(deletionList);
    }
    
    // Eliminar una ruta en la lista de adyacencia y en el mapa
    public void deleteRoute(Stop src, Stop dest) {
    	LinkedList<Stop> currentList = adjList.get(getStopIndex(src));
    	currentList.remove(dest);
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
    	}
    }
    
    public Route getRoute(Stop src, Stop dest) {
        String routeKey = searchRouteId(src, dest);
        return routes.get(routeKey);
    }
    
    public Map<String, Route> getRoutesMap() {
    	return routes;
    }
    
    public List<Route> getRoutes() {
        
    	List<Route> myRoutes = new ArrayList<>();
    	for(Entry<String, Route> entry : routes.entrySet()) {
    		myRoutes.add(entry.getValue());
    	}
    	return myRoutes;
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
    
    public String searchRouteId(Stop src, Stop dest) {
    	for(Entry<String, Route> entry : routes.entrySet()) {
    		if(entry.getValue().getSrc() == src && entry.getValue().getDest() == dest) {
    			return entry.getKey();
    		}
    	}
    	return "0";
    }

	public ArrayList<Stop> getStops() {
		ArrayList<Stop> myStops = new ArrayList<>();
		
		for(LinkedList<Stop> currentList : adjList) {
			myStops.add(currentList.getFirst());
		}
		
		return myStops;
	}
	
	public int getStopIndex(Stop s) {
		int index = 0;	
		for(LinkedList<Stop> currentList : adjList) {
            if(s.equals(currentList.getFirst())) {
            	return index;
            }
            index++;
        }
		return -1;
	}
	
	public List<Stop> getNeighbors(Stop stop){
		
		ArrayList<Stop> neighbors = new ArrayList<>();
		
		for(LinkedList<Stop> currentList : adjList) {
			if(stop.equals(currentList.getFirst())) {
				for(Stop s : currentList) {
					if(s != currentList.getFirst()) {
						neighbors.add(s);
					}
				}
			}
		}
		
		return neighbors;
		
		
	}
	
    public void print() {
        System.out.println("Estado actual del sistema de transporte:");
        for(LinkedList<Stop> currentList : adjList) {
            for(Stop stop : currentList) {
                System.out.print(stop.getLabel());
                if(currentList.indexOf(stop) != currentList.size() - 1 && stop != currentList.get(0)) {
                    Route route = getRoute(currentList.get(0), stop);
                    if (route != null) {
                        System.out.print(" -> [" + route.getCurrentEvent() + "] -> ");
                    } else {
                        System.out.print(" -> ");
                    }
                }
            }
            System.out.println();
        }
    }

}