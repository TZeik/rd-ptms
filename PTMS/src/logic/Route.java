package logic;

import java.io.Serializable;

import javafx.scene.shape.Line;

public class Route implements Serializable {
    private static final long serialVersionUID = 852477390490216637L;
    private String id;
    private double distance;
    private int travelTime;
    private int tranship;
    private Stop src;
    private Stop dest;
    private String label;
    private WeatherTrafficEvent currentEvent;

    public Route(String id, double distance, Stop src, Stop dest, String label) {
        this.id = id;
        this.distance = distance;
        this.travelTime = 1;
        this.tranship = 1;
        this.src = src;
        this.dest = dest;
        this.label = label;
        this.currentEvent = new WeatherTrafficEvent();
    }

    public double getDistance() {
        return distance;
    }

    public double getAdjustedDistance() {
    	return distance * currentEvent.getDelayFactor();
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public int getAdjustedTravelTime() {
        return (int) Math.ceil(distance * currentEvent.getDelayFactor());
    }
    
    public int getTranship() {
        return tranship;
    }

    public void setTranship(int tranship) {
        this.tranship = tranship;
    }

    public Stop getSrc() {
        return src;
    }

    public void setSrc(Stop src) {
        this.src = src;
    }

    public Stop getDest() {
        return dest;
    }

    public void setDest(Stop dest) {
        this.dest = dest;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WeatherTrafficEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(WeatherTrafficEvent event) {
        this.currentEvent = event;
    }

    public void updateEvent() {
        currentEvent.generateRandomEvent();
    }

    @Override
    public String toString() {
        return "Route [id=" + id + 
               ", distance=" + distance + 
               ", travelTime=" + travelTime + 
               ", adjustedTravelTime=" + getAdjustedTravelTime() +
               ", currentConditions=" + currentEvent.toString() +
               ", tranship=" + tranship + 
               ", src=" + src + 
               ", dest=" + dest + 
               ", label=" + label + "]";
    }

}