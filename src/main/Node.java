package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    List<String> keys;
    int maxEntries = 4;

    public Node() {
        this.keys = new ArrayList<>(maxEntries);
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

    abstract String[] RemoveKey(int pos);

    abstract void AddVal(String key, int value);

    float GetFillFactor(){
        return (float) this.size()/(float) maxEntries;
    }

    int size() {
        return keys.size();
    }

    boolean isOverFlow() {
        return size() > maxEntries;
    }

    boolean IsUnderFlow() {
        return size() < maxEntries / 2;
    }

    boolean IsRedundant(){
        return this.size() >= ((maxEntries/2) + 1);
    }

    @Override
    public String toString() {
        return keys.toString();
    }
}
