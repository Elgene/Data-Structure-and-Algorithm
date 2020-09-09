import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String tripID;
    private ArrayList<Stop> stops;
    private ArrayList<Connection> tripConnect;

    public Trip(String id) {
        stops = new ArrayList<Stop>();
        tripConnect = new ArrayList<Connection>();
        this.tripID = id;
    }
    public ArrayList<Connection> getTripConnections() { return tripConnect; }

    public void addStop(Stop stop) { stops.add(stop); }

    public ArrayList<Stop> getStops() { return stops; }

    public void addConnections (Connection c) { tripConnect.add(c); }

}