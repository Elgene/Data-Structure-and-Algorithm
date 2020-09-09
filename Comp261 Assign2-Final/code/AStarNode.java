package code;


public class AStarNode implements Comparable<AStarNode> {

    public Node startNode;
    public AStarNode prevNode;
    public Segment edgeCost;
    public double gCost = 0;
    public double hCost = 0;

    public static String condition = "";

    public AStarNode(Node startNode, AStarNode prevNode, Node end) {
        this.startNode = startNode;
        this.prevNode = prevNode;
        this.setCostEdge();
        if (prevNode == null)
            this.gCost = 0;
        else if (condition.equals("FindFastest")) {
            this.gCost = this.edgeCost.length + this.prevNode.gCost / edgeCost.road.getSpeedLimit() + edgeCost.road.getRoadClass();
            this.hCost = this.startNode.location.distance(end.location) / Parser.averageSpeed;
        } else
            this.gCost = this.edgeCost.length + this.prevNode.gCost;
        this.hCost = this.startNode.location.distance(end.location);

    }

    public void setCostEdge() {
        if (this.startNode == null || this.prevNode == null)
            return;
        for (Segment segCost : this.startNode.segments) {
            if (this.prevNode.startNode.segments.contains(segCost)) {
                this.edgeCost = segCost;
                return;
            }
        }
    }

    @Override
    public int compareTo(AStarNode next) {
        double costFirstNode = this.gCost + this.hCost;
        double costNextNode = next.gCost + next.hCost;
        if (costFirstNode > costNextNode)
            return 1;
        else if (costFirstNode < costNextNode)
            return -1;
        return 0;
    }


}
