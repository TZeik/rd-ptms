package logic;

public class Route {
	
	private int id;
	private int distance;
	private int travelTime;
	private int trans;
	private String label;
	private Stop next;
	private Stop prev;
	
	public Route(int id, int distance, int travelTime, int trans, String label, Stop next, Stop prev) {
		super();
		this.id = id;
		this.distance = distance;
		this.travelTime = travelTime;
		this.trans = trans;
		this.label = label;
		this.next = next;
		this.prev = prev;
	}
	
}
