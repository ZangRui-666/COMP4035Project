package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    List<String> keys;
    int maxEntries = 4;

    public Node() {
        this.keys = new ArrayList<>(maxEntries);
    }

    /**
     * Get the node which may contain the search key
     * @param key search key
     * @return the Node which may containing the key
     */
    abstract Node GetSearchNode(String key);

    String IndexOfKey(int index){
        return keys.get(index);
    }


    abstract boolean PutVal(String key, int value, BPlusTree tree);

    abstract boolean Remove(String key, BPlusTree tree);

    abstract Node Split();

    abstract void Merge(Node node);

    abstract String GetFirstKey();

    abstract int[] TotalEntries();

    abstract int TotalNodes();

    abstract int GetHeight();

    abstract float AvgFillFactor(int totalNodes);

    abstract String[] RemoveKey(int pos);

    abstract void AddVal(String key, int value);

    float GetFillFactor(){
        return (float) this.Size()/(float) maxEntries;
    }

    int Size() {
        return keys.size();
    }

    boolean IsOverFlow() {
        return Size() > maxEntries;
    }

    boolean IsUnderFlow() {
        return Size() < maxEntries / 2;
    }

    boolean IsRedundant(){
        return this.Size() >= ((maxEntries/2) + 1);
    }

    @Override
    public String toString() {
        return keys.toString();
    }
}
