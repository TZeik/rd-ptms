package logic;

import java.io.Serializable;

public class Stop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3271700719054280445L;
	private String id;
	private String label;
	private double x,y;

	public Stop(String id, String label) {
		super();
		this.id = id;
		this.label = label;
		this.x = 0;
		this.y = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getIndex() {
		return PTMS.getInstance().getGraph().getAdjList().get(0).indexOf(this);
	}
	
	@Override
    public String toString() {
        return id+" : "+label;
    }

}
