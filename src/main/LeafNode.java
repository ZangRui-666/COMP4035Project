package main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LeafNode extends Node {
    List<Integer> values;
    LeafNode next;

    public LeafNode() {
        super();
        values = new ArrayList<>(order + 1);
    }

    public LeafNode(List<String> keys, List<Integer> values) {
        this();
        this.keys.addAll(keys);
        this.values.addAll(values);
    }

    @Override
    int getVal(String key) {
        int pos = Collections.binarySearch(keys, key);
        return pos >= 0 ? values.get(pos) : null;
    }

    @Override
    void putVal(String key, int value, BPlusTree tree) {
        int pos = Collections.binarySearch(keys, key);
        if (pos >= 0) {
            values.set(pos, value);
            return;
        }
        int insertPos = -pos - 1;
        keys.add(insertPos, key);
        values.add(insertPos, value);
        if (tree.root.isOverFlow()) {
            Node siblingNode = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.addAll(Arrays.asList(this.getFirstKey(), siblingNode.getFirstKey()));
            newRoot.children.addAll(Arrays.asList(this, siblingNode));
            tree.root = newRoot;
        }
    }

    @Override
    int remove(String key, BPlusTree tree) {
        int pos = Collections.binarySearch(keys, key);
        if (pos >= 0) {
            keys.remove(pos);
            return values.remove(pos);
        }
        return -1;
    }

    @Override
    Node split() {
        int from = size() / 2, to = size();
        List<String> subKeys = keys.subList(from, to);
        List<Integer> subValues = values.subList(from, to);
        LeafNode siblingNode = new LeafNode(subKeys, subValues);
        subKeys.clear();
        subValues.clear();
        siblingNode.next = next;
        next = siblingNode;
        return siblingNode;
    }

    @Override
    void merge(Node node) {
        LeafNode mergeNode = (LeafNode) node;
        this.keys.addAll(mergeNode.keys);
        this.values.addAll(mergeNode.values);
    }

    @Override
    String getFirstKey() {
        return keys.get(size() - 1);
    }
}