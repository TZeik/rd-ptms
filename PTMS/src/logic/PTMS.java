package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;

import javafx.scene.shape.Circle;

public class PTMS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2749425870864089238L;
	public static PTMS soul = null;
	private Graph publicTransportMap;
	private int stopIdGenerator;
	private int routeIdGenerator;
	//private Object selected;
	
	public PTMS() {
		super();
		
		this.publicTransportMap = new Graph();
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

	public void setGraph(Graph publicTransportMap) {
		this.publicTransportMap = publicTransportMap;
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
	
}