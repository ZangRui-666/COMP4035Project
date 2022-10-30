package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    List<String> keys;
    int order = 5;

    public Node() {
        this.keys = new ArrayList<>(order-1);
    }

    abstract int getVal(String key);

    abstract void putVal(String key, int value, BPlusTree tree);

    abstract int remove(String key, BPlusTree tree);

    abstract Node split();

    abstract void merge(Node node);

    abstract String getFirstKey();

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
