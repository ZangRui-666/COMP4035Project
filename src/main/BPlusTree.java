package main;

import java.util.*;


public class BPlusTree {

    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * 根节点
     */
    public Node root;

    /**
     * B+树的阶数, 阶数影响每个节点能够存放的关键字的个数以及孩子节点的个数
     */
    private final int order;

    /**
     * 当构造方法中未指定阶数时, 使用的默认阶数
     */
    private static final int DEFAULT_ORDER = 3;

    public BPlusTree() {
        this(DEFAULT_ORDER);
    }

    public BPlusTree(int order) {
        root = new LeafNode();
        if (order >= 3) {
            this.order = order;
        } else {
            throw new IllegalArgumentException("order should be not less than 3");
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (i > 0) {
                    s.append(",");
                }
                s.append(node.keys.toString());
                if (node instanceof InternalNode) {
                    queue.addAll(((InternalNode) node).children);
                }
            }
            str.append(s).append("\n");
        }
        return str.toString();
    }

    public int get(String key) {
        return root.getVal(key);
    }

    public void put(String key, int value) {
        root.putVal(key, value, this);
    }

    public int remove(String key) {
        return root.remove(key, this);
    }




}
