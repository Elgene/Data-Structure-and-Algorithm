import java.util.ArrayList;
import java.util.List;

public class Stop {
    private ArrayList<Connection> inConnections;
    private ArrayList<Connection> outConnections;
    private double lat;
    private double lon; // longitude
    private String stopId;
    private String stopName;
    private ArrayList<String> idOfTrips;
    private boolean isSelected = false;
    private boolean tripsofStops = false;
    private Location currentLoc;

    /**
     * Constructor for the Stop class
     */
    public Stop(String stopId, String stopName, double lat, double lon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.lat = lat;
        this.lon = lon;
        inConnections = new ArrayList<>();
        outConnections = new ArrayList<>();
        idOfTrips = new ArrayList<>();
    }

    public void addInConnection(Connection connect) {
        inConnections.add(connect);
    }

    public void addOutConnection(Connection connect) {
        outConnections.add(connect);
    }

    public boolean isTripsHasStop() {
        return tripsofStops;
    }

    public boolean isSelected() {
        return isSelected;
    }


    public void addTripsWithId(String s) {
        idOfTrips.add(s);
    }

    public double getLat() { //getter
        return lat;
    }

    public double getLon() {
        return lon;
    }  // getter

    public Location getCurrentLoc() {
        return currentLoc;
    }

    public String getStopName() {
        return stopName;
    }

    public List<String> getTripId() {
        return idOfTrips;
    }


    public void setIsSelected(boolean bool) {
        this.isSelected = bool;
    }


    public void setTripStop(boolean bool) {
        this.tripsofStops = bool;
    }

    public void setLoc(Location location) {
        currentLoc = location;
    }
    public double getX(){
        return currentLoc.x;
    }

    /**
     * @return y location of the node
     */
    public double getY(){
        return currentLoc.y;
    }

}
