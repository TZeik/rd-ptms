package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import exceptions.*;

public class PTMS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2749425870864089238L;
	public static PTMS soul = null;
	private ArrayList<Graph> transportMaps;
	private Graph publicTransportMap;
	private int graphIdGenerator;
	private int stopIdGenerator;
	private int routeIdGenerator;
	
	public PTMS() {
		super();
		this.transportMaps = new ArrayList<>();
		this.publicTransportMap = new Graph("G-1","Nuevo Mapa");
		this.transportMaps.add(publicTransportMap);
		this.graphIdGenerator = 2;
		this.stopIdGenerator = 1;
		this.routeIdGenerator = 1;
	}
	
	public static PTMS getInstance() {
		if(soul == null) {
			soul = new PTMS();
		}
		return soul;
	}

	public static void setSoul(PTMS soul) {
		PTMS.soul = soul;
	}

	public Graph getGraph() {
		return publicTransportMap;
	}
	
	public ArrayList<Graph> getMaps(){
		return transportMaps;
	}

	public void setGraph(Graph publicTransportMap) {
		this.publicTransportMap = publicTransportMap;
	}
	
	public int getGraphIdGenerator() {
		return graphIdGenerator;
	}

	public void setGraphIdGenerator(int graphIdGenerator) {
		this.graphIdGenerator = graphIdGenerator;
	}

	public int getStopIdGenerator() {
		return stopIdGenerator;
	}

	public void setStopIdGenerator(int stopIdGenerator) {
		this.stopIdGenerator = stopIdGenerator;
	}

	public int getRouteIdGenerator() {
		return routeIdGenerator;
	}

	public void setRouteIdGenerator(int routeIdGenerator) {
		this.routeIdGenerator = routeIdGenerator;
	}
	
	// Implementación de guardado de datos, persistencia de los datos del sistema
	public void savePTMS() {
		
		FileOutputStream file;
			try {
				file = new FileOutputStream("ptms.dat");
				ObjectOutputStream oos = new ObjectOutputStream(file);
				oos.writeObject(PTMS.getInstance());
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	// Implementación de carga de los datos, persistencia de los datos del sistema
	public void loadPTMS() {
		FileInputStream file;
		ObjectInputStream oos;

		try {
			file = new FileInputStream("ptms.dat");
			oos = new ObjectInputStream(file);
			PTMS.setSoul((PTMS)oos.readObject());
			oos.close();
		} catch (IOException | ClassNotFoundException e) {
			PTMS.getInstance().savePTMS();
		}
	}
	
	public void addGraph(Graph graph) {
		PTMS.getInstance().getMaps().add(graph);
		PTMS.getInstance().setGraphIdGenerator(PTMS.getInstance().getGraphIdGenerator() + 1);
	}
	
	public void editGraph(Graph graph) {
		for(Graph g : PTMS.getInstance().getMaps()) {
			if(graph.getId().equals(g)) {
				g = graph;
			}
		}
	}
	
	public void removeGraph(Graph graph) {
		PTMS.getInstance().getMaps().remove(graph);
	}

	
	// Genera un código ID único para cada grafo
	public String generateGraphID() {
		
		String id;
		id = "G-" + graphIdGenerator;
		return id;
	}
	
	// Genera un código ID único para cada parada
	public String generateStopID() {
		
		String id;
		id = "S-" + stopIdGenerator;
		return id;
	}
	
	// Genera un código ID único para cada ruta
	public String generateRouteID() {
		
		String id;
		id = "R-" + routeIdGenerator;
		return id;
	}
	
	public boolean checkPathFinderUsability() {
		
		if(PTMS.getInstance().getGraph().getStops().size() > 1 && PTMS.getInstance().getGraph().getRoutes().size() > 0) return true;
		else return false;
		
	}
	
	public void checkSameStop(Route route) throws SameStopException {
		for(Route r : PTMS.getInstance().getGraph().getRoutes()) {
			if(r.getSrc().equals(route.getSrc()) && r.getDest().equals(route.getDest()))
				throw new SameStopException("Ya existe una parada con la misma fuente y destino");
		}
	}
	
	public void checkVerifiedName(String name) throws BadNameException, EmptyNameException {
		if(name.isEmpty()) throw new EmptyNameException("El nombre no puede estar vacío");
		if(name.length() < 0 || name.length() > 30) throw new BadNameException("El nombre debe contener entre 1 a 30 caracteres");	
	}
	
	public void checkSameStopPath(Stop a, Stop b) throws SameStopException, NullStopException{
		if(a == null) throw new NullStopException("La parada fuente no existe");
		if(b == null) throw new NullStopException("La parada de destino no existe");
		if(a.equals(b)) throw new SameStopException("Las paradas no puedes ser iguales");
	}
	
}