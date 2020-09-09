import java.util.*;

public class TrieNode {
    private Stop stopsNode = null;
    private Map<String, TrieNode> children;

    public TrieNode() {
        children = new HashMap<>();
    }

    public Collection<TrieNode> getChildren() {
        Collection<TrieNode> allChildrenList = children.values();
        return Collections.unmodifiableCollection(allChildrenList);
    }

    public boolean containsChild(String childNode) {
        if (children.containsKey(childNode)) {
            return true;
        }
        return false;
    }

    public Stop getStops() {
        return stopsNode;
    }

    public void setStops(Stop stop) {
        stopsNode = stop;
    }

    public void addChild(String childCharacter, TrieNode childNode) {
        children.put(childCharacter, childNode);
    }

    public TrieNode getChild(String childNode) {
        return children.get(childNode);
    }

    public boolean hasStopNode() {
        if (stopsNode != null) {
            return true;
        }
        return false;
    }


}

