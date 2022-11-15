package main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeafNode extends Node {
    public List<Integer> getValues() {
        return values;
    }

    List<Integer> values;
    LeafNode next;
    LeafNode previous;

    public LeafNode() {
        super();
        values = new ArrayList<>(maxEntries + 1);
    }

    public LeafNode(List<String> keys, List<Integer> values) {
        this();
        this.keys.addAll(keys);
        this.values.addAll(values);
    }

    @Override
    Node GetSearchNode(String key) {
        return this;
    }

    @Override
    boolean PutVal(String key, int value, BPlusTree tree) {
        int pos = Utils.binarySearch(keys, key);
        if (pos >= 0) {
//            values.set(pos, value);
            return false;
        }
        int insertPos = -pos - 1;
        keys.add(insertPos, key);
        values.add(insertPos, value);
        if (this==tree.root && tree.root.IsOverFlow()) {
            Node siblingNode = Split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(siblingNode.GetFirstKey());
            newRoot.children.addAll(Arrays.asList(this, siblingNode));
            tree.root = newRoot;
        }
        return true;
    }

    @Override
    int Remove(String key, BPlusTree tree) {
        if(!this.keys.contains(key)) return -1;
        int pos = this.keys.indexOf(key);
        this.keys.remove(pos);
        this.values.remove(pos);
        return 1;
    }

    @Override
    Node Split() {
        int from = Size() / 2, to = Size();
        List<String> subKeys = keys.subList(from, to);
        List<Integer> subValues = values.subList(from, to);
        LeafNode siblingNode = new LeafNode(subKeys, subValues);
        subKeys.clear();
        subValues.clear();
        siblingNode.next = next;
        this.next = siblingNode;
        siblingNode.previous = this;
        return siblingNode;
    }

    @Override
    void Merge(Node node) {
        LeafNode mergeNode = (LeafNode) node;
        for (int i = 0; i < mergeNode.Size(); i++) {
            AddVal(mergeNode.keys.get(i), mergeNode.values.get(i));
        }
    }

    @Override
    String GetFirstKey() {
        return keys.get(0);
    }

    @Override
    int[] TotalEntries(){
        int[] entries = new int[2];
        entries[0] = this.Size();
        return entries;
    }

    @Override
    int GetHeight(){return 1;}

    @Override
    int TotalNodes(){ return 1; }

    @Override
    float AvgFillFactor(int totalNodes){return this.GetFillFactor()*100;}

    @Override
    String[] RemoveKey(int pos){
        String[] removedData = new String[2];
        removedData[1] = this.values.remove(pos) + "";
        removedData[0] = this.keys.remove(pos);
        return removedData;
    }

    @Override
    void AddVal(String key, int value){
        int pos = Utils.binarySearch(keys, key);
        if (pos >= 0) {
            values.set(pos, value);
            return;
        }
        int insertPos = -pos - 1;
        keys.add(insertPos, key);
        values.add(insertPos, value);
    }
}