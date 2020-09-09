package code;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.*;

/**
 * This represents the data structure storing all the roads, nodes, and
 * segments, as well as some information on which nodes and segments should be
 * highlighted.
 *
 * @author tony
 */
public class Graph {
    // map node IDs to Nodes.
    Map<Integer, Node> nodes = new HashMap<>();
    // map road IDs to Roads.
    Map<Integer, Road> roads;
    // just some collection of Segments.
    Collection<Segment> segments;
    Collection<Restriction> restrictions;
    //public static String condition="";


    Node highlightedNode;
    Collection<Road> highlightedRoads = new HashSet<>();
    ArrayList<Segment> highlightedSegments = new ArrayList<Segment>();
    Collection<Node> highlightedAllNodes = new ArrayList<Node>();
    //ArrayList<Segment> highlightedRestriction = new ArrayList<Segment>();


    public Graph(File nodes, File roads, File segments, File polygons, File restrictions) {
        this.nodes = Parser.parseNodes(nodes, this);
        this.roads = Parser.parseRoads(roads, this);
        this.segments = Parser.parseSegments(segments, this);
        if (restrictions != null) {
            this.restrictions = Parser.parseRestrictions(restrictions, this);
            ;
        }
    }

    public void draw(Graphics g, Dimension screen, Location origin, double scale) {
        // a compatibility wart on swing is that it has to give out Graphics
        // objects, but Graphics2D objects are nicer to work with. Luckily
        // they're a subclass, and swing always gives them out anyway, so we can
        // just do this.
        Graphics2D g2 = (Graphics2D) g;

        // draw all the segments.

        for (Segment s : segments) {
            if (s.getOneWay()) {
                g2.setColor(Mapper.HIGHLIGHT_ONEWAY_COLOUR);
            } else {
                g2.setColor(Mapper.SEGMENT_COLOUR);

            }
            s.draw(g2, origin, scale);
        }
        // draw the segments of all highlighted roads.
        g2.setColor(Mapper.HIGHLIGHT_COLOUR);
        g2.setStroke(new BasicStroke(3));
        for (Road road : highlightedRoads) {
            for (Segment seg : road.components) {
                seg.draw(g2, origin, scale);
            }
        }
        if (!highlightedSegments.isEmpty()) {
            g2.setColor(Mapper.HIGHLIGHT_COLOUR);
            for (Segment s : highlightedSegments) {
                s.draw(g2, origin, scale);
            }
        }


        // draw all the nodes.
        g2.setColor(Mapper.NODE_COLOUR);
        for (Node n : nodes.values()) {
            n.draw(g2, screen, origin, scale);
        }

        // draw the highlighted node, if it exists.
        if (highlightedNode != null) {
            g2.setColor(Mapper.HIGHLIGHT_NODE_COLOUR);
            highlightedNode.draw(g2, screen, origin, scale);
        }
        if (!highlightedAllNodes.isEmpty()) {
            g2.setColor(Mapper.HIGHLIGHT_NODE_COLOUR);
            for (Node n : highlightedAllNodes)
                n.draw(g2, screen, origin, scale);
        }

    }

    public void setHighlight(Node node) {

        this.highlightedNode = node;
    }

    public void setHighlight(Collection<Road> roads) {
        this.highlightedRoads = roads;
    }

    public void setHighlight(ArrayList<Segment> segments) {

        this.highlightedSegments = segments;
    }
}

// code for COMP261 assignments