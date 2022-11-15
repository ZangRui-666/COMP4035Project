package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class BPlusTree {
    public Node root;

    public BPlusTree() {
        root = new LeafNode();
    }

    public BPlusTree(File inputFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(inputFile);
        root = new LeafNode();
        while (fileScanner.hasNext()) {
            String key = fileScanner.nextLine();
            //assume the value is always 0
            this.put(key, 0);
        }
        fileScanner.close();
    }


    public void setRoot(Node root) {
        this.root = root;
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

    public boolean Update(String key, String value){
        Node search = root.GetSearchNode(key);
        int pos = Utils.binarySearch(search.keys, key);
        if (pos < 0) return false;
        ((LeafNode) search).getValues().set(pos, Integer.valueOf(value));
        return true;
    }

    public List<SearchResult> Search(String key1, String key2) {
        Node left = root.GetSearchNode(key1);
        int pos = Utils.binarySearch(left.keys, key1);
        if (pos < 0) pos = -pos - 1;
        String search = left.keys.get(pos);
        List<SearchResult> results = new LinkedList<>();
        while (search.compareTo(key2) <= 0) {
            results.add(new SearchResult(search, ((LeafNode) left).getValues().get(pos)));
            if (pos < left.Size() - 1) {
                pos++;
            } else {
                left = ((LeafNode) left).next;
                pos = 0;
                if(left==null)
                    break;
            }
            search = left.IndexOfKey(pos);
        }
        return results;

    }

    public List<SearchResult> PrefixSearch(String prefix){
        Node left = root.GetSearchNode(prefix);
        int pos = Utils.binarySearch(left.keys, prefix);
        if (pos < 0) pos = -pos - 1;
        String search = left.keys.get(pos);
        List<SearchResult> results = new LinkedList<>();
        while (search.startsWith(prefix)){
            results.add(new SearchResult(search, ((LeafNode) left).getValues().get(pos)));
            if (pos < left.Size() - 1) {
                pos++;
            } else {
                left = ((LeafNode) left).next;
                pos = 0;
                if(left==null)
                    break;
            }
            search = left.IndexOfKey(pos);
        }
        return results;
    }

    public boolean put(String key, int value) {
        return root.PutVal(key, value, this);
    }

    public int Remove(String key) {
        int success = root.Remove(key, this);
        if (success == -1) {
            System.out.println("The key: " + key + " is not in the B+-tree.");
            return success;
        }
        System.out.println("The key " + key + " has been deleted in the B+-tree.");
        return success;
    }

    public void DumpStatistics() {
        //0: total number of node, 1: total number of data entries, 2: total number of index entries
        //3: Avg fill-factor of nodes, 4: height of tree
        String[] dataSet = new String[5];
        dataSet[0] = root.TotalNodes() + "";
        int[] totalEntries = root.TotalEntries();
        dataSet[1] = totalEntries[0] + "";
        dataSet[2] = totalEntries[1] + "";
        dataSet[3] = root.AvgFillFactor(Integer.parseInt(dataSet[0])) + "%";
        dataSet[4] = root.GetHeight() + "";
        PrintDumpStatistics(dataSet);
    }

    private void PrintDumpStatistics(String[] data) {
        if (data.length != 5) return;
        System.out.println("Statistics of the B+-tree: ");
        System.out.println("Total number of nodes: " + data[0]);
        System.out.println("Total number of data entries: " + data[1]);
        System.out.println("Total number of index entries: " + data[2]);
        System.out.println("Average fill factor: " + data[3]);
        System.out.println("Height of tree: " + data[4]);
        System.out.println();
    }


}
