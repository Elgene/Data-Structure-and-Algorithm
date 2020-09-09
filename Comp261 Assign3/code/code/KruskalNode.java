package code;

public class KruskalNode implements Comparable<KruskalNode>{
    Node startNode;
    Segment segment;
    double costToTree;
    public KruskalNode(Node start, Segment edge,double cost){
        this.startNode =start;
        this.segment = edge;
        this.costToTree =cost;


    }
    @Override
    public int compareTo(KruskalNode other){
        if(this.costToTree < other.costToTree)
            return -1;
        else if(other.costToTree < this.costToTree)
            return 1;
        return 0;
    }
}
