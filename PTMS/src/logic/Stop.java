package logic;

import java.util.List;

public class Stop {
	private int id;
	private String label;
	private List<Route> myRoutes;
	
	public Stop(int id, String label, List<Route> myRoutes) {
		super();
		this.id = id;
		this.label = label;
		this.myRoutes = myRoutes;
	}
}
