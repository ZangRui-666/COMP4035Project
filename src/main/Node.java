package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    List<String> keys;
    int order = 4;

    public Node() {
        this.keys = new ArrayList<>(order);
    }

    abstract Node getSearchNode(String key);

    String indexOfKey(int index){
        return keys.get(index);
    }


    abstract void putVal(String key, int value, BPlusTree tree);

    abstract int remove(String key, BPlusTree tree);

    abstract Node split();

    abstract void merge(Node node);

    abstract String getFirstKey();

    abstract int[] TotalEntries();

    abstract int TotalNodes();

    abstract int GetHeight();

    abstract float AvgFillFactor(int totalNodes);

    float GetFillFactor(){
        return (float) this.size()/(float) order;
    }

    int size() {
        return keys.size();
    }

    boolean isOverFlow() {
        return size() > order;
    }

    boolean isUnderFlow() {
        return size() <= order / 2;
    }

    @Override
    public String toString() {
        return keys.toString();
    }
}
