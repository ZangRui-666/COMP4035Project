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


    public BPlusTree() {
        root = new LeafNode();
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

    public void DumpStatistics(){
        //0: total number of node, 1: total number of data entries, 2: total number of index entries
        //3: Avg fill-factor of nodes, 4: height of tree
        //todo: the length of the string array should be changed
        String[] dataSet = new String[2];
        dataSet[0] = root.TotalNodes() + "";
        dataSet[1] = root.TotalEntries() + "";
        PrintDumpStatistics(dataSet);
    }

    private void PrintDumpStatistics(String[] data){
        System.out.println("Total number of nodes in the tree: " + data[0]);
        System.out.println("Total number of entries in the tree: " + data[1]);

    }



}
