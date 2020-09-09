package code;

public class Restriction {


    public Node nodeId1;
    public Node interNode;
    public Node nodeId2;
    public Road roadId1;
    public Road roadId2;

    public Restriction(Graph graph, int node1ID, int road1ID, int intNodeID, int node2ID, int road2ID) {
        super();
        this.nodeId1 = graph.nodes.get(node1ID);
        this.interNode = graph.nodes.get(intNodeID);
        this.nodeId2 = graph.nodes.get(node2ID);
        this.roadId1 = graph.roads.get(road1ID);
        this.roadId2 = graph.roads.get(road2ID);
    }

}
