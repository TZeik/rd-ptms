package logic;

import java.io.Serializable;

public class Route implements Serializable {
    private static final long serialVersionUID = 852477390490216637L;
    private String id;
    private int distance;
    private int travelTime;
    private int tranship;
    private int src;
    private int dest;
    private String label;
    private WeatherTrafficEvent currentEvent;

    public Route(String id, int distance, int src, int dest, String label) {
        this.id = id;
        this.distance = distance;
        this.travelTime = 1;
        this.tranship = 1;
        this.src = src;
        this.dest = dest;
        this.label = label;
        this.currentEvent = new WeatherTrafficEvent();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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