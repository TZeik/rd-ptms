package logic;

import java.io.Serializable;

public class Stop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3271700719054280445L;
	private String id;
	private String label;

	public Stop(String id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setI0(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
