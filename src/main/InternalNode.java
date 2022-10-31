package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternalNode extends Node {
    List<Node> children;

    public InternalNode() {
        super();
        children = new ArrayList<>(order+1);
    }

    public InternalNode(List<String> keys, List<Node> children) {
        this();
        this.keys.addAll(keys);
        this.children.addAll(children);
    }

    @Override
    int getVal(String key) {
        int pos = Collections.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos : -pos - 1;
//        if (childPos >= size()) {
//            return null;
//        }
        Node child = getChild(childPos);
        return child.getVal(key);
    }

    @Override
    void putVal(String key, int value, BPlusTree tree) {
        int pos = Utils.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos : -pos - 1;
        Node child = getChild(childPos);
        child.putVal(key, value, tree);
        if (child.isOverFlow()) {
            Node siblingNode = child.split();
            insertChild(siblingNode);
        }
        if (tree.root.isOverFlow()) {
            Node siblingNode = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(keys.get(size()-1));
            this.keys.remove(size()-1);
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

    private void insertChild(Node node) {
        String key = node.getFirstKey();
        int pos = Collections.binarySearch(keys, key);
        int insertPos = pos >= 0 ? pos : -pos - 1;
        keys.add(insertPos, key);
        children.add(insertPos+1, node);
    }

    private Node getChild(int childPos) {
        return children.get(childPos);
    }
}