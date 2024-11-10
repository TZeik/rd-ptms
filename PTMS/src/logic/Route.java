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

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public String getLabel() {
        return label;
    }
}