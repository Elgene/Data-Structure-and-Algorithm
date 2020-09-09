package code;

import java.util.*;
import java.util.stream.Collectors;


public class AStarRecord {
    static Set<BlockRoad> blockRoads = new HashSet<>();
    public static boolean off;
    public static String condition = "";

    public static List<Segment> listSearchPath(Node start, Node end) {

        HashSet<Integer> setVisitedNode = new HashSet<>();
        PriorityQueue<AStarNode> queueFringe = new PriorityQueue<>();
        // add the start node to the queue
        queueFringe.add(new AStarNode(start, null, end));

        while (!queueFringe.isEmpty()) {
            AStarNode currNode = queueFringe.poll();
            setVisitedNode.add(currNode.startNode.nodeID);
            //  if the end node is removed then we know the path
            if (currNode.startNode.nodeID == end.nodeID) {
                AStarNode prevPath = currNode;
                List<Segment> listPath = new ArrayList<>();
                while (prevPath.prevNode != null) {
                    listPath.add(prevPath.edgeCost);
                    prevPath = prevPath.prevNode;
                }
                Collections.reverse(listPath);
                return listPath;
            }

            Node updatedNode = currNode.startNode;
            Node goalNode = null;
            for (Segment segmentUpdatedNode : updatedNode.segments) {
                if (segmentUpdatedNode.start == updatedNode)
                    goalNode = segmentUpdatedNode.end;
                else if (segmentUpdatedNode.end == updatedNode && !segmentUpdatedNode.road.oneway)
                    goalNode = segmentUpdatedNode.start;
                //  if the updated segment is not for car the no need to add the queue
                if (segmentUpdatedNode.road.notforcar) {
                    continue;
                }
                if (goalNode != null && !setVisitedNode.contains(goalNode.nodeID) || off) { //  checks for visited nodes
                    if (condition.equals("Restriction")) {
                        blockRoads = blockList(updatedNode, segmentUpdatedNode, goalNode);
                    }
                    AStarNode nextNode = new AStarNode(goalNode, currNode, end);
                    boolean nodeExisted = false;
                    // check if it already in fringe, if so, see if its cost needs to be updated
                    for (AStarNode n : queueFringe) {
                        if (n.startNode.nodeID == goalNode.nodeID) {
                            if (nextNode.gCost < n.gCost) { // get the lowest cost
                                n.prevNode = nextNode.prevNode;
                                n.setCostEdge();
                            }
                            if (condition.equals("FindFastest")) {
                                if (nextNode.gCost < n.gCost) { // get the lowest and fastest cost
                                    n.prevNode = nextNode.prevNode;
                                    n.setCostEdge();
                                }
                            }
                            nodeExisted = true;
                            break;
                        }
                    }
                    if (!nodeExisted)
                        queueFringe.add(nextNode);

                }

            }

        }
        return new ArrayList<>();
    }

    private static Set<BlockRoad> blockList(Node currentNode, Segment segRoad, Node nextNode) {
        Set<BlockRoad> block = new HashSet<>();

        for (Restriction restriction : Mapper.getGraph().restrictions) {
            if (currentNode == restriction.nodeId1 && segRoad.road == restriction.roadId1 && nextNode == restriction.interNode)
                block.add(new BlockRoad(restriction.roadId2, restriction.nodeId2));
        }
        return block;
    }

}
