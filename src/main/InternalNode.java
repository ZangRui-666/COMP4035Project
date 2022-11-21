package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternalNode extends Node {
    List<Node> children;

    public InternalNode() {
        super();
        children = new ArrayList<>(maxEntries + 1);
    }

    public InternalNode(List<String> keys, List<Node> children) {
        this();
        this.keys.addAll(keys);
        this.children.addAll(children);
    }

    @Override
    Node GetSearchNode(String key) {
        int pos = Collections.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos + 1 : -pos - 1;
        Node child = GetChild(childPos);
        return child.GetSearchNode(key);
    }

    @Override
    boolean PutVal(String key, int value, BPlusTree tree) {
        int pos = Utils.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos + 1 : -pos - 1;
        Node child = GetChild(childPos);
        if(!child.PutVal(key, value, tree))
            return false;
        if (child.IsOverFlow()) {
            Node siblingNode = child.Split();
            InsertChild(siblingNode);
            if (child instanceof InternalNode)
                child.keys.remove(child.Size() - 1);
        }
        if (tree.root.IsOverFlow()) {
            Node siblingNode = Split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(keys.get(Size() - 1));
            this.keys.remove(Size() - 1);
            newRoot.children.addAll(Arrays.asList(this, siblingNode));
            tree.setRoot(newRoot);
        }
        return true;
    }

    @Override
    boolean Remove(String key, BPlusTree tree) {
        int pos = Utils.binarySearch(keys, key);
        int childPos = pos >= 0 ? pos + 1 : -pos - 1;
        boolean success;
        Node child = GetChild(childPos);
        success = child.Remove(key, tree);
        if(!success) return false;
        if(child.IsUnderFlow()){
            Node leftNode = null, rightNode = null, brotherNode = null;
            int leRi = 0;
            //exist right node
            if(childPos >=0 && childPos != children.size()-1){
                rightNode = GetChild(childPos + 1);
                //right node have redundant node
                if(rightNode.IsRedundant()){
                    brotherNode = rightNode;
                    leRi = 1;   //brother node is right node
                }
            }
            //have no right node, check the left node
            if(childPos != 0 && childPos <=children.size()-1){
                leftNode = GetChild(childPos-1);
                //left node have redundant node
                if(leftNode.IsRedundant()){
                    brotherNode = leftNode;
                    leRi = -1;  //brother node is left node
                }
            }

            //if brother node have redundant element
            if(brotherNode != null){
                pos = pos >= 0 ? pos : Math.min(-pos, Size())-1;
                //borrow a node from brother node
                Borrow(brotherNode, child, this, childPos, leRi, pos);
            }else { //brother node have no redundant element
                if(rightNode != null) {
                    brotherNode = rightNode;
                    leRi = 1;
                }else {
                    brotherNode = leftNode;
                    leRi = -1;
                }
                if(this == tree.root && this.keys.size() < 2) {
                    //child.AddVal(this.keys.get(0), -1);
                    child.Merge(brotherNode);
                    tree.setRoot(child);
                }else {
                    child.Merge(brotherNode);
                   // keys.remove(childPos);
                    keys.remove(Math.min(childPos, Size()-1));
                    children.remove(childPos + leRi);
                }
            }
        }
        return true;
    }

    @Override
    Node Split() {
        int from = Size() / 2 + 1, to = Size();
        List<String> subKeys = keys.subList(from, to);
        List<Node> subChildren = children.subList(from, to + 1);
        InternalNode siblingNode = new InternalNode(subKeys, subChildren);
        subKeys.clear();
        subChildren.clear();
        return siblingNode;
    }

    @Override
    void Merge(Node node) {
        InternalNode mergeNode = (InternalNode) node;
        this.keys.addAll(mergeNode.keys);
        this.children.addAll(mergeNode.children);
    }

    @Override
    String GetFirstKey() {
        Node firstChild = children.get(0);
        return firstChild.GetFirstKey();
    }

    @Override
    int[] TotalEntries() {
        int[] sum = new int[2];
        int dataEntries = 0;
        int indexEntries = this.Size();
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

    @Override
    String[] RemoveKey(int pos){
        String[] removedData = new String[2];
        removedData[1] = "-1";
        removedData[0] = this.keys.remove(pos);
        return removedData;
    }

    @Override
    void AddVal(String key, int value){
        int pos = Utils.binarySearch(keys, key);
        if (pos >= 0) {
            return;
        }
        int insertPos = -pos - 1;
        keys.add(insertPos, key);
    }

    private void Borrow(Node brother, Node thisNode, Node fatherNode, int childPos, int leRi, int pos){
        String[] borrowData;
        if(leRi == -1){
            borrowData = brother.RemoveKey(brother.keys.size()-1);
        }else {
            borrowData = brother.RemoveKey(0);
        }
        if(thisNode instanceof LeafNode) {
            thisNode.AddVal(borrowData[0], Integer.parseInt(borrowData[1]));
            if(leRi == -1){
                fatherNode.keys.set(childPos+leRi, borrowData[0]);
            }else {
                fatherNode.keys.set(childPos, brother.keys.get(0));
            }
        }else {
            thisNode.AddVal(fatherNode.keys.get(pos), -1);
            fatherNode.keys.set(pos, borrowData[0]);
        }

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

    private void InsertChild(Node node) {
        String key = node.GetFirstKey();
        int pos = Collections.binarySearch(keys, key);
        int insertPos = pos >= 0 ? pos : -pos - 1;
        keys.add(insertPos, key);
        children.add(insertPos + 1, node);
    }

    private Node GetChild(int childPos) {
        return children.get(childPos);
    }
}