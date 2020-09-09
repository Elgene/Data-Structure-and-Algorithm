package code;

import java.util.*;

public class Articulation {
    public static Set<Node> findAPs(Collection<Node> nodes) {
        //  all nodes is set to MAX_VALUE and initial neighbours
        for (Node n : nodes) {
            n.depth = Integer.MAX_VALUE;
            for (Segment seg : n.segments) {
                if (seg.start == n && seg.end != null)
                    n.neighbours.add(seg.end);
                else if (seg.end == n && seg.start != null)
                    n.neighbours.add(seg.start);
            }
        }

        Set<Node> unvisitedNodesSet = new HashSet<>(nodes);
        Set<Node> articulationPointsSet = new HashSet<>();

        while (!unvisitedNodesSet.isEmpty()) {
            int countSubtrees = 0;
            Node start = unvisitedNodesSet.iterator().next();
            start.depth = 0;
            for (Node neighbour : start.neighbours) {
                if (neighbour.depth == Integer.MAX_VALUE) {
                    iterativeAP(neighbour, start, articulationPointsSet, unvisitedNodesSet);
                    countSubtrees++;
                }
            }

            if (countSubtrees > 1)
                articulationPointsSet.add(start);

            unvisitedNodesSet.remove(start);
        }
        // could return empty set
        return articulationPointsSet;
    }

    private static void iterativeAP(Node firstNode, Node root, Set<Node> artPoints, Set<Node> unvisitedNodes) {
        Stack<iterativeApStack> stack = new Stack<>();
        stack.push(new iterativeApStack(firstNode, 1, new iterativeApStack(root, 0, null)));

        while (!stack.isEmpty()) {
            iterativeApStack elem = stack.peek();
            Node node = elem.node;
            if (elem.children == null) { // first time
                node.depth = elem.reachBack = elem.depth;
                elem.children = new ArrayList<>();
                for (Node neighbour : node.neighbours) {
                    if (neighbour.nodeID != elem.parent.node.nodeID) {
                        elem.children.add(neighbour);
                    }
                }
            } else if (!elem.children.isEmpty()) { // children to process
                Node child = elem.children.remove(0);
                if (child.depth < Integer.MAX_VALUE)
                    elem.reachBack = Math.min(elem.reachBack, child.depth);
                else
                    stack.push(new iterativeApStack(child, node.depth + 1, elem));
            } else { // last time
                if (node.nodeID != firstNode.nodeID) {
                    if (elem.reachBack >= elem.parent.depth)
                        artPoints.add(elem.parent.node);
                    elem.parent.reachBack = Math.min(elem.parent.reachBack, elem.reachBack);
                }
                stack.pop();
                unvisitedNodes.remove(elem.node);
            }
        }
    }

    private static class iterativeApStack {

        Node node;
        int reachBack;
        iterativeApStack parent;
        int depth;
        List<Node> children;

        public iterativeApStack(Node node, int depth, iterativeApStack parent) {
            this.node = node;
            this.reachBack = Integer.MAX_VALUE;
            this.parent = parent;
            this.depth = depth;
            this.children = null;
        }
    }

}
