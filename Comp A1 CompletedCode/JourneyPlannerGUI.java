import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class JourneyPlannerGUI extends GUI {

    private final Map<String, Stop> stopMap = new HashMap<>();
    private final Map<String, Trip> tripMap = new HashMap<>();
    private final ArrayList<Connection> stopSequence = new ArrayList<>();
    private final ArrayList<Connection> selectedConnections = new ArrayList<>();
    private final Location origin = Location.newFromLatLon(-12.499435, 130.94329);
    private double pointX = 0.0 ;
    private double pointY = 0.0;
    private double scale = 10.0;
    private TrieNode rootNode = new TrieNode();
    private Location dragStart;
    private Location originLoc;

    private JourneyPlannerGUI() {
    }

    @Override
    protected void onLoad(File stopFile, File tripFile) {
        loadAllStops(stopFile);
        loadAllTrips(tripFile);
        loadTrie();
    }
    private void loadAllStops(File stopFile) {
        BufferedReader stopReader = null;
        String stopLine;
        int stopCount = 0;
        try {
            stopReader = new BufferedReader(new FileReader(stopFile));
        } catch (IOException e) {
            e.printStackTrace();
            printText("This file does not exist"); // handle FileReader exceptions
            return;
        }
        try {
            while ((stopLine = stopReader.readLine()) != null) {
                if (stopCount > 0) {
                    String[] stopValue = stopLine.split("\t");
                    double stopLat = Double.parseDouble(stopValue[2]);
                    double stopLon = Double.parseDouble(stopValue[3]);
                    Stop newStop = new Stop(stopValue[0], stopValue[1], stopLat, stopLon);
                    stopMap.put(stopValue[0], newStop);
                }
                stopCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            printText("Failed to read stop.txt ");
        }
    }

    private void loadAllTrips(File tripFile) {
        BufferedReader incomingTrip = null;
        String tripLine;
        int tripCount = 0;
        try {
            incomingTrip = new BufferedReader(new FileReader(tripFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            printText("Failed to read Trip.txt.");
            return;
        }
        try {
            assert incomingTrip != null;
            while ((tripLine = incomingTrip.readLine()) != null) {
                if (tripCount > 0) {
                    String[] tripValues = tripLine.split("\t");
                    Trip trip = new Trip(tripValues[0]);
                    for (int i = 1; i < tripValues.length; i++) {
                        Stop stop = stopMap.get(tripValues[i]);
                        trip.addStop(stop);
                    }
                    tripMap.put(tripValues[0], trip);
                    for (int nextNode = 1; nextNode < tripValues.length; nextNode++) {
                        Stop firstStopId = stopMap.get(tripValues[nextNode]);
                        firstStopId.addTripsWithId(tripValues[0]);
                        if (nextNode > 1) {
                            Stop endStopId = stopMap.get(tripValues[nextNode - 1]);
                            Connection connect = new Connection(trip, endStopId, firstStopId);
                            firstStopId.addInConnection(connect);
                            trip.addConnections(connect);
                        }
                        if (nextNode < tripValues.length - 1) {
                            Stop endStopId = stopMap.get(tripValues[nextNode + 1]);
                            Connection connected = new Connection(trip, firstStopId, endStopId);
                            firstStopId.addOutConnection(connected);
                            stopSequence.add(connected);
                        }
                    }
                }
                tripCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            printText("Failed to read trip.txt.");
        }
    }
    private void add(char[] word, Stop stop) {
        TrieNode node = rootNode;
        for (char eachCharOfWord : word) {
            String valuesOfWord = String.valueOf(eachCharOfWord);
            valuesOfWord = valuesOfWord.toLowerCase();
            if (!node.containsChild(valuesOfWord)) {
                TrieNode n_Child = new TrieNode();
                node.addChild(valuesOfWord, n_Child);
            }
            node = node.getChild(valuesOfWord);
        }
        node.setStops(stop);
    }

    private List<Stop> getAll(char[] word) {
        List<Stop> stops = new ArrayList<>();
        TrieNode node = rootNode;
        for (char eachCharOfWord : word) {
            String valuesOfWord = String.valueOf(eachCharOfWord);
            valuesOfWord = valuesOfWord.toLowerCase();
            if (!node.containsChild(valuesOfWord)) {
                return null;
            }
            node = node.getChild(valuesOfWord);
        }
        getAllFrom(node, stops);
        return stops;
    }

    private void getAllFrom(TrieNode node, List<Stop> stops) {
        if (node.hasStopNode()) {
            Stop stop = node.getStops();
            stops.add(stop);
        }
        for (TrieNode child : node.getChildren()) {
            getAllFrom(child, stops);
        }
    }

    private void loadTrie() {
        for (Stop eachstop : stopMap.values()) {
            String name = eachstop.getStopName();
            char[] characterOfArray = name.toCharArray();
            add(characterOfArray, eachstop);
        }
    }

    public void drawAllConnections(Graphics g, Connection connect) {
        Stop inConnections = connect.getStartStop();
        Stop outConnections = connect.getEndStop();
        double inLatOfConnections = inConnections.getLat();
        double inLonOfConnections = inConnections.getLon();
        double outLatOfConnections = outConnections.getLat();
        double outLonOfConnections = outConnections.getLon();
        Location locationOfInConnections = Location.newFromLatLon(inLatOfConnections, inLonOfConnections);
        Location startLocation = locationOfInConnections.moveBy(pointX, pointY);
        Location locationOfOutConnections = Location.newFromLatLon(outLatOfConnections, outLonOfConnections);
        Location endLocation = locationOfOutConnections.moveBy(pointX, pointY);
        Point startConnection = startLocation.asPoint(origin, scale);
        Point endConnection = endLocation.asPoint(origin, scale);
        startConnection.translate(getDrawingAreaDimension().width / 2, getDrawingAreaDimension().height / 2);
        endConnection.translate(getDrawingAreaDimension().width / 2, getDrawingAreaDimension().height / 2);
        int startConnectX = (int) startConnection.getX();
        int startConnectY = (int) startConnection.getY();
        int endConnectX = (int) endConnection.getX();
        int endConnectY = (int) endConnection.getY();
        g.drawLine(startConnectX, startConnectY, endConnectX, endConnectY);
    }

    @Override
    protected void redraw(Graphics g) {
        for (Stop eachstop : stopMap.values()) {
            double lat = eachstop.getLat();
            double lon = eachstop.getLon();
            Location location = Location.newFromLatLon(lat, lon);
            Location currLocation = location.moveBy(pointX, pointY);
            eachstop.setLoc(currLocation);
            Point newPoint = currLocation.asPoint(origin, scale);
            newPoint.translate(getDrawingAreaDimension().width / 2, getDrawingAreaDimension().height / 2);
            int x = (int) newPoint.getX();
            int y = (int) newPoint.getY();
            if (eachstop.isSelected()) {
                g.setColor(Color.BLACK); //all stops
                g.drawOval(x, y, 6, 6);
            }
            if (eachstop.isSelected()) {
                g.setColor(Color.GREEN);//selected one stop only
            } else if (eachstop.isTripsHasStop()) {
                g.setColor(Color.magenta); //selected region only
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillOval(x, y, 5, 5);


        }
        for (Connection connect : stopSequence) { //stopsequence
            g.setColor(Color.RED); //all trips
            drawAllConnections(g, connect);
        }
        for (Connection connect : selectedConnections) { //selectedstops
            g.setColor(Color.BLUE); //selected trips
            drawAllConnections(g, connect);
        }
    }

    @Override
    protected void onClick(MouseEvent e) {
        selectedConnections.clear();
        for (Stop stop : stopMap.values()) {
            stop.setTripStop(false);
            stop.setIsSelected(false);
        }
        Point newPoint = e.getPoint();
        newPoint.translate(-getDrawingAreaDimension().width / 2, -getDrawingAreaDimension().height / 2);
        Location clickedLoc = Location.newFromPoint(newPoint, origin, scale);

        Location closestLoc = null;
        Stop smallestStop = null;
        double smallestDist = Double.MAX_VALUE;
        for (Stop eachstop : stopMap.values()) {
            Location stopLocation = eachstop.getCurrentLoc();
            double newDist = stopLocation.distance(clickedLoc);
            if (newDist < smallestDist) {
                smallestDist = newDist;
                smallestStop = eachstop;
                closestLoc = stopLocation;
            }
        }
        if (smallestStop != null) {
            smallestStop.setIsSelected(true);
            JTextArea area = getTextOutputArea();
            String listOfStops = "You have selected: " + smallestStop.getStopName() + "    id trips of the stops:";
            List<String> stopId = smallestStop.getTripId();
            for (String eachId : stopId) {
                listOfStops = listOfStops + ",     " + eachId;
            }
            area.setText(listOfStops);

            //ArrayList<String> trip_ID = smallestStop.getTrips_Id();
            for (String eachTripId : smallestStop.getTripId()) {
                Trip t = tripMap.get(eachTripId);
                for (Stop eachstop : t.getStops()) {
                    eachstop.setTripStop(true);
                }

                for (Connection c : t.getTripConnections()) {
                    selectedConnections.add(c);

                }
            }
        }
    }

    @Override
    protected void onSearch() {
        selectedConnections.clear();
        for (Stop stop : stopMap.values()) {
            stop.setTripStop(false);
            stop.setIsSelected(false);
        }
        JTextField input = getSearchBox();
        String inputValue = input.getText();
        char[] c = inputValue.toCharArray();
        List<Stop> allStops = getAll(c);
        JTextArea area = getTextOutputArea();
        String stopName = "The possible matching stops: ";
        if (allStops != null) {
            for (Stop stop : allStops) {
                stop.setIsSelected(true);
                stopName = stopName + stop.getStopName() + ",  ";
            }
            if (allStops.size() == 1) {
                Stop firstStop = allStops.get(0);
                List<String> matchValues = firstStop.getTripId();
                for (String value : matchValues) {
                    Trip eachTrip = tripMap.get(value);
                    for (Stop s1 : eachTrip.getStops()) {
                        s1.setTripStop(true);
                    }
                    for (Connection con : eachTrip.getTripConnections()) {
                        selectedConnections.add(con);
                    }
                }
            }
        } else {
            stopName = stopName + " Input Error: These are not the stops you are looking for :) ";
        }
        area.setText(stopName);
    }

    @Override
    protected void onMove(Move moveStep) {

        if (moveStep == Move.NORTH) {
            pointY -= 1;
        } else if (moveStep == Move.SOUTH) {
            pointY += 1;
        } else if (moveStep == Move.EAST) {
            pointX -= 1;
        } else if (moveStep == Move.WEST) {
            pointX += 1;
        } else if (moveStep.equals(Move.ZOOM_IN)) {
            scale += 1;
        } else if (moveStep.equals(Move.ZOOM_OUT)) {
            scale -= 1;
        }
    }

    private void printText(String textOutput) {
        getTextOutputArea().append(textOutput + "\n");
    }
    protected void onDrag(MouseEvent e){

        Location loc = Location.newFromPoint(e.getPoint(), originLoc, scale);
        originLoc = originLoc.moveBy(dragStart.x - loc.x, dragStart.y - loc.y);
    }
    protected void onPress(MouseEvent e){
        dragStart = Location.newFromPoint(e.getPoint(), originLoc, scale);
    }

    protected void onScroll(MouseWheelEvent e){
        if(e.getWheelRotation() == -1){
            onMove(Move.ZOOM_IN);
        }else{
            onMove(Move.ZOOM_OUT);
        }
    }
    public static void main(String[] args) {
        new JourneyPlannerGUI();
    }
}
