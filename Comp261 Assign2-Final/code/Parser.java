package code;

import java.io.*;
import java.util.*;

/**
 * This utility class provides three static methods for parsing each of the
 * three files we're interested in, and returning the relevant data structure.
 * Internally it uses BufferedReaders instead of Scanners to read in the files,
 * as Scanners are pathetically slow.
 *
 * @author tony
 */
public class Parser {
    public static double averageSpeed;

    public static Map<Integer, Node> parseNodes(File nodes, Graph graph) {
        Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();

        try {
            // make a reader
            BufferedReader br = new BufferedReader(new FileReader(nodes));
            String line;

            // read in each line of the file
            while ((line = br.readLine()) != null) {
                // tokenise the line by splitting it at the tabs.
                String[] tokens = line.split("[\t]+");

                // process the tokens
                int nodeID = asInt(tokens[0]);
                double lat = asDouble(tokens[1]);
                double lon = asDouble(tokens[2]);

                Node node = new Node(nodeID, lat, lon);
                nodeMap.put(nodeID, node);
            }

            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }

        return nodeMap;
    }

    public static Map<Integer, Road> parseRoads(File roads, Graph graph) {
        Map<Integer, Road> map = new HashMap<Integer, Road>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(roads));
            br.readLine(); // throw away the top line of the file.
            String line;
            double totalSpeed = 0;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("[\t]+");

                int roadID = asInt(tokens[0]);
                int type = asInt(tokens[1]);
                String label = tokens[2];
                String city = tokens[3];
                int oneway = asInt(tokens[4]);
                int speed = asInt(tokens[5]);
                int roadclass = asInt(tokens[6]);
                int notforcar = asInt(tokens[7]);
                int notforpede = asInt(tokens[8]);
                int notforbicy = asInt(tokens[9]);

                Road road = new Road(roadID, type, label, city, oneway, speed,
                        roadclass, notforcar, notforpede, notforbicy);
                map.put(roadID, road);
                totalSpeed += road.getSpeedLimit();
            }
            averageSpeed = totalSpeed / map.size();

            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }

        return map;
    }

    public static Collection<Segment> parseSegments(File segments, Graph graph) {
        Set<Segment> set = new HashSet<Segment>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(segments));
            br.readLine(); // throw away the top line of the file.
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("[\t]+");

                int roadID = asInt(tokens[0]);
                double length = asDouble(tokens[1]);
                int node1ID = asInt(tokens[2]);
                int node2ID = asInt(tokens[3]);

                double[] coords = new double[tokens.length - 4];
                for (int i = 4; i < tokens.length; i++)
                    coords[i - 4] = asDouble(tokens[i]);

                Segment segment = new Segment(graph, roadID, length, node1ID,
                        node2ID, coords);
                set.add(segment);
            }

            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }

        return set;
    }

    public static Collection<Restriction> parseRestrictions(File restriction, Graph graph) {

        Set<Restriction> set = new HashSet<Restriction>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(restriction));
            br.readLine(); // throw away the top line of the file.
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("[\t]+");

                int node1ID = asInt(tokens[0]);
                int road1ID = asInt(tokens[1]);
                int intNodeID = asInt(tokens[2]);
                int Road2ID = asInt(tokens[3]);
                int node2ID = asInt(tokens[4]);

                Restriction res = new Restriction(graph, node1ID, road1ID, intNodeID,
                        Road2ID, node2ID);

                set.add(res);
            }

            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }
        return set;
    }


    private static int asInt(String str) {
        return Integer.parseInt(str);
    }

    private static double asDouble(String str) {
        return Double.parseDouble(str);
    }
}

// code for COMP261 assignments