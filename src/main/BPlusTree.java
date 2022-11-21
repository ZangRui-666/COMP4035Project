package main;

import java.io.*;
import java.util.*;


public class BPlusTree implements Serializable {

    public Node root;

    public BPlusTree() {
        root = new LeafNode();
    }

    /**
     * Use level order traversal of the tree,
     * to clear all the nodes in the tree and set the mutual reference to null,
     * so that the memory will be cleaned.
     */
    public void ClearTree() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (node instanceof InternalNode) {
                    queue.addAll(((InternalNode) node).children);
                    ((InternalNode) node).children.clear();
                } else {
                    assert node != null;
                    ((LeafNode) node).values.clear();
                    ((LeafNode) node).previous = null;
                    ((LeafNode) node).next = null;
                }
                node.keys.clear();
            }
        }
        setRoot(new LeafNode());
    }

    /**
     * Construct the tree according to the input file
     *
     * @param inputFile the file of data
     * @throws FileNotFoundException if no such file is found
     */
    public BPlusTree(File inputFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(inputFile);
        root = new LeafNode();
        while (fileScanner.hasNext()) {
            String key = fileScanner.nextLine();
            //assume the value is always 0
            this.Insert(key, 0);
        }
        fileScanner.close();
    }



    public Object ReadTree(String fName) throws IOException, ClassNotFoundException {
        Object temp;
        File file =new File(fName + ".dat");
        FileInputStream in;
        in = new FileInputStream(file);
        ObjectInputStream objIn=new ObjectInputStream(in);
        temp=objIn.readObject();
        objIn.close();
        System.out.println("read object success!");
        return temp;
    }

    public void Save(String fName) throws IOException {
        File file =new File(fName+".dat");
        FileOutputStream out;
        out = new FileOutputStream(file);
        ObjectOutputStream objOut=new ObjectOutputStream(out);
        objOut.writeObject(this);
        objOut.flush();
        objOut.close();
        System.out.println("write object success!");
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Use level order traversal of the tree to print the structure of the tree
     *
     * @return String representation of the tree
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (i > 0) {
                    s.append(",");
                }
                assert node != null;
                s.append(node.keys.toString());
                if (node instanceof InternalNode) {
                    queue.addAll(((InternalNode) node).children);
                }
            }
            str.append(s).append("\n");
        }
        return str.toString();
    }

    /**
     * Update the value of the specific key
     *
     * @param key   the key to search
     * @param value the value after update
     * @return true, if the key was found. false, if the key was not found.
     */
    public boolean Update(String key, String value) {
        Node search = root.GetSearchNode(key);
        int pos = Utils.binarySearch(search.keys, key);
        if (pos < 0) return false;
        ((LeafNode) search).getValues().set(pos, Integer.valueOf(value));
        return true;
    }

    /**
     * Search the result of a specific range
     *
     * @param key1 the left boundary
     * @param key2 the left boundary
     * @return List of SearchResult
     */
    public List<SearchResult> Search(String key1, String key2) {
        Node left = root.GetSearchNode(key1);
        int pos = Utils.binarySearch(left.keys, key1);
        if (pos < 0) pos = -pos - 1;
        String search = left.keys.get(pos); // get the first result
        List<SearchResult> results = new LinkedList<>();
        // linear search to the right
        while (search.compareTo(key2) <= 0) {
            results.add(new SearchResult(search, ((LeafNode) left).getValues().get(pos)));
            if (pos < left.Size() - 1) {
                pos++;
            } else {
                left = ((LeafNode) left).next;
                pos = 0;
                if (left == null)
                    break;
            }
            search = left.IndexOfKey(pos);
        }
        return results;

    }

    /**
     * Search by the prefix of a String
     *
     * @param prefix the prefix for search
     * @return List of SearchResult
     */
    public List<SearchResult> PrefixSearch(String prefix) {
        Node left = root.GetSearchNode(prefix);
        int pos = Utils.binarySearch(left.keys, prefix);
        if (pos < 0) pos = -pos - 1;
        String search = left.keys.get(pos); // get the first search result
        List<SearchResult> results = new LinkedList<>();
        // linear search to the right
        while (search.startsWith(prefix)) {
            results.add(new SearchResult(search, ((LeafNode) left).getValues().get(pos)));
            if (pos < left.Size() - 1) {
                pos++;
            } else {
                left = ((LeafNode) left).next;
                pos = 0;
                if (left == null)
                    break;
            }
            search = left.IndexOfKey(pos);
        }
        return results;
    }

    /**
     * Insert the key-value pair to the tree
     *
     * @return true, if inserted successfully. false, if duplicate.
     */
    public boolean Insert(String key, int value) {
        return root.PutVal(key, value, this);
    }

    /**
     * Remove a specific key from the tree
     * @return true, if deleted successfully. false, if the key nonexistent.
     */
    public boolean Remove(String key) {
        return root.Remove(key, this);
    }

    /**
     * Get the statistics of the tree
     */
    public void DumpStatistics() {
        String[] dataSet = new String[5];
        dataSet[0] = root.TotalNodes() + "";    //0: total number of node
        int[] totalEntries = root.TotalEntries();
        dataSet[1] = totalEntries[0] + "";  //1: total number of data entries
        dataSet[2] = totalEntries[1] + "";  //2: total number of index entries
        dataSet[3] = root.AvgFillFactor(Integer.parseInt(dataSet[0])) + "%";    //3: Avg fill-factor of nodes
        dataSet[4] = root.GetHeight() + "";     //4: height of tree
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
