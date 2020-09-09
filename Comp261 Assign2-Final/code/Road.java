package code;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Road represents ... a road ... in our graph, which is some metadata and a
 * collection of Segments. We have lots of information about Roads, but don't
 * use much of it.
 *
 * @author tony
 */
public class Road {
    public final int roadID;
    public final String name, city;
    public final Collection<Segment> components;
    private final Integer speedLimit;
    public boolean oneway = false;
    public boolean notforcar = false;
    public double length;
    private int roadClass;
    private static Map<Integer, Integer> speedLimits = new HashMap<>();

    static {
        speedLimits.put(0, 5); //0 to 5km/h
        speedLimits.put(1, 20); //1 to 20km/h
        speedLimits.put(2, 40); //2 to 40km/h
        speedLimits.put(3, 60); //3 to 60km/h
        speedLimits.put(4, 80); //4 to 80km/h
        speedLimits.put(5, 100); //5 to 100km/h
        speedLimits.put(6, 110); //6 to 120km/h
        speedLimits.put(7, 200); //7 to "no limit"
    }

    public Road(int roadID, int type, String label, String city, int oneway,
                int speed, int roadclass, int notforcar, int notforpede,
                int notforbicy) {
        this.notforcar = notforcar == 1 ? true : false;
        this.oneway = oneway == 1 ? true : false;
        this.roadID = roadID;
        this.city = city;
        this.name = label;
        this.components = new HashSet<Segment>();
        this.speedLimit = speedLimits.get(speed);
        this.roadClass = roadclass;
    }

    public void addSegment(Segment seg) {
        components.add(seg);
    }

    public double countRoadLength() {
        this.length = 0;
        for (Segment eachSeg : this.components)
            this.length += eachSeg.length;
        return this.length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public int getRoadClass() {
        return roadClass;
    }


}
// code for COMP261 assignments