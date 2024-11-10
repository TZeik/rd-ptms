package logic;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {

	private ArrayList<LinkedList<Stop>> adjList;
	
	Graph(){
		adjList = new ArrayList<>();
	}
	
	public void addStop(Stop stop) {
		LinkedList<Stop> currentList = new LinkedList<>();
		currentList.add(stop);
		adjList.add(currentList);
	}
	
	public void addEdge(Route route) {
		LinkedList<Stop> currentList = adjList.get(route.getSrc());
		Stop dstStop = adjList.get(route.getDest()).get(0);
		currentList.add(dstStop);
	}
	
	public boolean checkEdge(Route route) {
		LinkedList<Stop> currentList = adjList.get(route.getSrc());
		Stop dstStop = adjList.get(route.getDest()).get(0);
		
		for(Stop s : currentList) {
			if(s == dstStop) {
				return true;
			}
		}
		return false;
	}
	
	// Para pruebas
	
	public void print() {
		for(LinkedList<Stop> currentList : adjList) {
			for(Stop stop : currentList) {
				System.out.print(stop.getLabel() + " -> ");
			}
			System.out.println();
		}
	}
	
}
