package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 *
 * @author tony
 */
public class Mapper extends GUI {
    public static final Color NODE_COLOUR = new Color(77, 113, 255);
    public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
    public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);
    public static final Color HIGHLIGHT_ONEWAY_COLOUR = new Color(0, 200, 0);
    public static final Color HIGHLIGHT_NODE_COLOUR = new Color(200, 0, 0);
    // these two constants define the size of the node squares at different zoom
    // levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
    // log(scale)
    public static final int NODE_INTERCEPT = 1;
    public static final double NODE_GRADIENT = 0.8;

    // defines how much you move per button press, and is dependent on scale.
    public static final double MOVE_AMOUNT = 100;
    // defines how much you zoom in/out per button press, and the maximum and
    // minimum zoom levels.
    public static final double ZOOM_FACTOR = 1.3;
    public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

    // how far away from a node you can click before it isn't counted.
    public static final double MAX_CLICKED_DISTANCE = 0.15;

    // these two define the 'view' of the program, ie. where you're looking and
    // how zoomed in you are.
    private Location origin;
    private double scale;

    // our data structures.
    private static Graph graph;
    //****************CHANGES************
    private boolean searchingPath = false;
    private Node start, goal;
    private int selectedPath = 0;
    List<Segment> shortestRoute = new ArrayList<Segment>();

    @Override
    protected void redraw(Graphics g) {
        if (getGraph() != null)
            getGraph().draw(g, getDrawingAreaDimension(), origin, scale);
    }

    @Override
    protected void onMinValueSet(boolean minValue) {
        searchingPath();
    }

    @Override
    protected void onClick(MouseEvent e) {
        Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
        // find the closest node.
        double bestDist = Double.MAX_VALUE;
        Node closest = null;

        for (Node node : getGraph().nodes.values()) {
            double distance = clicked.distance(node.location);
            if (distance < bestDist) {
                bestDist = distance;
                closest = node;
            }
        }

        // if it's close enough, highlight it and show some information.
        if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
            getGraph().setHighlight(closest);
            getTextOutputArea().setText(closest.toString());
        }
        //CHANGES HERE**************
        if (searchingPath) {
            if (this.selectedPath == 0) {
                start = closest;
                getTextOutputArea().setText("Starting point: " + start.toString() + "\n");
                this.selectedPath++;
            } else if (this.selectedPath == 1) {
                goal = closest;
                getTextOutputArea().append("Destination:  " + goal.toString() + "\n");
                shortestRoute = AStarRecord.listSearchPath(start, goal);
                graph.setHighlight((ArrayList<Segment>) shortestRoute);
                searchingPath = false;
                String duplicate = "";
                double totalLength = 0;
                double totalTime = 0;
                double time = 0;
                for (Segment s : shortestRoute) {
                    if (!s.road.name.equals(duplicate)) {
                        getTextOutputArea()
                                .append("Road name: " + s.road.name + "  Length: " + String.format("%.2f", s.road.countRoadLength()) + " km" + "  Time:" + String.format("%.2f", s.road.countRoadLength() / s.road.getSpeedLimit()) + "hr" + "\n");
                        totalLength += s.road.countRoadLength();
                        totalTime += s.road.countRoadLength() / s.road.getSpeedLimit();
                        duplicate = s.road.name;
                    }
                }
                getTextOutputArea().append("\nTotal length: " + String.format("%.2f", totalLength) + " km" + "\n" + "Total Time: " + String.format("%.2f", totalTime) + " hr");
            }
        }
    }

    //CHANGES HERE**************
    @Override
    protected void searchingPath() {
        selectedPath = 0;
        getTextOutputArea().setText("\nChoose the start point and destination point");
        searchingPath = true;
    }

    @Override
    protected void onSearch() {
        // Does nothing
    }

    @Override
    protected void onMove(Move m) {
        if (m == GUI.Move.NORTH) {
            origin = origin.moveBy(0, MOVE_AMOUNT / scale);
        } else if (m == GUI.Move.SOUTH) {
            origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
        } else if (m == GUI.Move.EAST) {
            origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
        } else if (m == GUI.Move.WEST) {
            origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
        } else if (m == GUI.Move.ZOOM_IN) {
            if (scale < MAX_ZOOM) {
                // yes, this does allow you to go slightly over/under the
                // max/min scale, but it means that we always zoom exactly to
                // the centre.
                scaleOrigin(true);
                scale *= ZOOM_FACTOR;
            }
        } else if (m == GUI.Move.ZOOM_OUT) {
            if (scale > MIN_ZOOM) {
                scaleOrigin(false);
                scale /= ZOOM_FACTOR;
            }
        }
        if (m == GUI.Move.RESTRICTIONS) {
            AStarRecord.off = false;
            searchingPath();

        }
        if (m == GUI.Move.FIND_FASTEST) {
            searchingPath();
        }
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons, File restrictions) {
        graph = new Graph(nodes, roads, segments, polygons, restrictions);
        origin = new Location(-250, 250); // close enough
        scale = 1;
    }

    /**
     * This method does the nasty logic of making sure we always zoom into/out
     * of the centre of the screen. It assumes that scale has just been updated
     * to be either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR
     * (zooming out). The passed boolean should correspond to this, ie. be true
     * if the scale was just increased.
     */
    private void scaleOrigin(boolean zoomIn) {
        Dimension area = getDrawingAreaDimension();
        double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

        int dx = (int) ((area.width - (area.width * zoom)) / 2);
        int dy = (int) ((area.height - (area.height * zoom)) / 2);

        origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void setGraph(Graph graph) {
        Mapper.graph = graph;
    }

    public static void main(String[] args) {
        new Mapper();
    }
}

// code for COMP261 assignments