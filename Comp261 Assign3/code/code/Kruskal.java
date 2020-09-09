package code;

import java.util.*;

public class Kruskal {
    static Set<Node> forest;
    static int count=0;
    static List<String> reportInfo = new ArrayList<String>() ;
    static HashSet<Segment> minSpanningTreeFound ;

    public static Set<Segment> kruskalAlgorithm(Set<Node> unvisitedNodesSet, Set<Segment> foundMst){

        PriorityQueue<KruskalNode> fringePq = new PriorityQueue<KruskalNode>();
        forest= new HashSet<Node>();
        for(Node n: unvisitedNodesSet){
            MakeForestSet(n);
        }
        for(Segment segment : Graph.segments){
            KruskalNode fringeElem = new KruskalNode(null,segment,segment.length);
            fringePq.add(fringeElem);
        }
        while(!unvisitedNodesSet.isEmpty()) {
            minSpanningTreeFound=new HashSet<>();
            while (forest.size() > 1 && !fringePq.isEmpty() ){
                KruskalNode current = fringePq.poll();
                Node node1 = current.segment.start;
                Node node2 = current.segment.end;
                if(union(node1,node2)){
                    minSpanningTreeFound.add(current.segment);
                    unvisitedNodesSet.remove(node1);
                    unvisitedNodesSet.remove(node2);
                }
            }
            count++;
            String s = count+ " :  #edges in MST=  " +minSpanningTreeFound.size();
            reportInfo.add(s);
            foundMst.addAll(minSpanningTreeFound);
            minSpanningTreeFound=new HashSet<>();
        }
        return foundMst;
    }
    protected static void MakeForestSet(Node node){
        node.parent = node;
        forest.add(node);
    }
    protected static Node findRoot(Node node){
        if(node.parent==node) {
            return node;
        }
        else{
            Node root = findRoot(node.parent);
            return root;
        }
    }
    protected static boolean union(Node node1, Node node2){
        Node n1Root = findRoot(node1);
        Node n2Root = findRoot(node2);
        if(n1Root==n2Root){
            return false;
        }
        else{
            if(n1Root.depth < n2Root.depth){
                n1Root.parent = n2Root.parent;
                forest.remove(n1Root);
            }
            else{
                n2Root.parent = n1Root;
                forest.remove(n2Root);
                if(n1Root.depth == n2Root.depth){
                    n1Root.depth++;
                }
            }
            return true;
        }
    }
}
