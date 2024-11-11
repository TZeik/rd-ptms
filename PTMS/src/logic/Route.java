package logic;

import java.io.Serializable;

public class Route implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 852477390490216637L;
	private String id;
    private int distance;
    private int travelTime;
    private int trans;
    private int src;
    private int dest;
    private String label;

    public Route(String id, int distance, int travelTime, int trans, int src, int dest, String label) {
        this.id = id;
        this.distance = distance;
        this.travelTime = travelTime;
        this.trans = trans;
        this.src = src;
        this.dest = dest;
        this.label = label;
    }

    public int getDistance() {
        return distance;
    }

    public int getTravelTime() {
        return travelTime;
    }
    
    public int getTrans() {
    	return trans;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public String getLabel() {
        return label;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}