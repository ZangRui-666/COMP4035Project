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

    public List<SearchResult> search(String key1, String key2) {
            Node left = root.getSearchNode(key1);
            int pos = Utils.binarySearch(left.keys, key1);
            if(pos<0) pos = -pos - 1;
            String search = left.keys.get(pos);
            List<SearchResult> results  = new LinkedList<>();
            while(search.compareTo(key2)>=0){
                results.add(new SearchResult(search, ((LeafNode) left).getValues().get(pos)));
                if(pos<left.size()-1){
                    pos++;
                    search = left.indexOfKey(pos);
                }else {
                    left = ((LeafNode) left).next;
                    pos = 0;
                    search = left.indexOfKey(pos);
                }
            }
            return results;

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
        String[] dataSet = new String[5];
        dataSet[0] = root.TotalNodes() + "";
        int[] totalEntries = root.TotalEntries();
        dataSet[1] = totalEntries[0] + "";
        dataSet[2] = totalEntries[1] + "";
        dataSet[3] = root.AvgFillFactor(Integer.parseInt(dataSet[0])) + "%";
        dataSet[4] = root.GetHeight() + "";
        PrintDumpStatistics(dataSet);
    }

    private void PrintDumpStatistics(String[] data){
        //if(data.length != 5) return;
        System.out.println("Total number of nodes in the tree: " + data[0]);
        System.out.println("Total number of data entries in the tree: " + data[1]);
        System.out.println("Total number of index entries in the tree: " + data[2]);
        System.out.println("Average fill-factor of the nodes is: " + data[3]);
        System.out.println("The height of the tree is: " + data[4]);
        System.out.println();

    }



}
