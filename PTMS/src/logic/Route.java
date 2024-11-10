package logic;

public class Route {
	private int id;
	private int distance;
	private int travelTime;
	private int trans;
	private int src;
	private int dest;
	private String label;
	
	public Route(int id, int distance, int travelTime, int trans, int src, int dest, String label) {
		super();
		this.id = id;
		this.distance = distance;
		this.travelTime = travelTime;
		this.trans = trans;
		this.src = src;
		this.dest = dest;
		this.label = label;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}
	
	
}
