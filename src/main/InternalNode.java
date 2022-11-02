package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternalNode extends Node {
    List<Node> children;

    public InternalNode() {
        super();
        children = new ArrayList<>(order + 1);
    }

    public InternalNode(List<String> keys, List<Node> children) {
        this();
        this.keys.addAll(keys);
        this.children.addAll(children);
    }

    @Override
    Node getSearchNode(String key) {
        int pos = Collections.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos + 1 : -pos - 1;
        Node child = getChild(childPos);
        return child.getSearchNode(key);
    }

    @Override
    void putVal(String key, int value, BPlusTree tree) {
        int pos = Utils.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos + 1 : -pos - 1;
        Node child = getChild(childPos);
        child.putVal(key, value, tree);
        if (child.isOverFlow()) {
            Node siblingNode = child.split();
            insertChild(siblingNode);
            if (child instanceof InternalNode)
                child.keys.remove(child.size() - 1);
        }
        if (tree.root.isOverFlow()) {
            Node siblingNode = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(keys.get(size() - 1));
            this.keys.remove(size() - 1);
            newRoot.children.addAll(Arrays.asList(this, siblingNode));
            tree.setRoot(newRoot);
        }
    }

    @Override
    int remove(String key, BPlusTree tree) {
        int pos = Collections.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos : Math.min(-pos, size()) - 1;
        Node child = getChild(childPos);
        int value = child.remove(key, tree);
        keys.set(childPos, child.getFirstKey());
        if (child.isUnderFlow()) {
            int rightPos;
            Node left, right;
            if (childPos > 0) {
                rightPos = childPos;
                left = getChild(childPos - 1);
                right = child;
            } else {
                rightPos = childPos + 1;
                left = child;
                right = getChild(childPos + 1);
            }
            left.merge(right);
            keys.remove(rightPos);
            children.remove(rightPos);
            keys.set(rightPos - 1, left.getFirstKey());
            if (left.isOverFlow()) {
                Node siblingNode = left.split();
                keys.set(rightPos - 1, left.getFirstKey());
                insertChild(siblingNode);
            }
            if (tree.root.size() < 2) {
                tree.setRoot(left);
            }
        }
        return value;
    }

    @Override
    Node split() {
        int from = size() / 2 + 1, to = size();
        List<String> subKeys = keys.subList(from, to);
        List<Node> subChildren = children.subList(from, to + 1);
        InternalNode siblingNode = new InternalNode(subKeys, subChildren);
        subKeys.clear();
        subChildren.clear();
        return siblingNode;
    }

    @Override
    void merge(Node node) {
        InternalNode mergeNode = (InternalNode) node;
        this.keys.addAll(mergeNode.keys);
        this.children.addAll(mergeNode.children);
    }

    @Override
    String getFirstKey() {
        Node firstChild = children.get(0);
        return firstChild.getFirstKey();
    }

    @Override
    int[] TotalEntries() {
        int[] sum = new int[2];
        int dataEntries = 0;
        int indexEntries = this.size();
        for (int i = 0; i < this.children.size(); i++) {
            Node n = this.children.get(i);
            if (n instanceof InternalNode) {
                dataEntries += n.TotalEntries()[0];
                indexEntries += n.TotalEntries()[1];
            } else {
                for (int j = 0; j < this.children.size(); j++) {
                    dataEntries += this.children.get(j).TotalEntries()[0];
                    indexEntries += this.children.get(j).TotalEntries()[1];
                }
                sum[0] = dataEntries;
                sum[1] = indexEntries;
                return sum;
            }
        }
        sum[0] = dataEntries;
        sum[1] = indexEntries;
        return sum;
    }

    @Override
    int TotalNodes() {
        int sum = 1;
        for (int i = 0; i < this.children.size(); i++) {
            Node n = this.children.get(i);
            if (n instanceof InternalNode) {
                sum += n.TotalNodes();
            } else {
                for (int j = 0; j < this.children.size(); j++) {
                    sum += this.children.get(j).TotalNodes();
                }
                return sum;
            }
        }
        return sum;
    }

    @Override
    int GetHeight() {
        int height = 1;
        Node n = this.children.get(0);
        if (n instanceof InternalNode) {
            height += n.GetHeight();
        } else {
            height += n.GetHeight();
            return height;
        }
        return height;
    }

    @Override
    float AvgFillFactor(int totalNodes) {
        return (this.TotalFillFactor() / (float) totalNodes) * 100;
    }

    private float TotalFillFactor() {
        float sumFillFactor = this.GetFillFactor();
        for (int i = 0; i < this.children.size(); i++) {
            Node n = this.children.get(i);
            if (n instanceof InternalNode) {
                sumFillFactor += ((InternalNode) n).TotalFillFactor();
            } else {
                for (int j = 0; j < this.children.size(); j++) {
                    sumFillFactor += this.children.get(j).GetFillFactor();
                }
                return sumFillFactor;
            }
        }
        return sumFillFactor;
    }

    private void insertChild(Node node) {
        String key = node.getFirstKey();
        int pos = Collections.binarySearch(keys, key);
        int insertPos = pos >= 0 ? pos : -pos - 1;
        keys.add(insertPos, key);
        children.add(insertPos + 1, node);
    }

    private Node getChild(int childPos) {
        return children.get(childPos);
    }
}