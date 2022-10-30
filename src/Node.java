import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    List<String> keys;

    public Node() {
        this.keys = new ArrayList<>(4);
    }

    abstract V getVal(String key);

    abstract void putVal(K key, V value);

    abstract V remove(K key);

    abstract BPlusTree.Node split();

    abstract void merge(BPlusTree.Node node);

    abstract K getLastLeafKey();

    int countKeys() {
        return keys.size();
    }

    boolean isOverFlow() {
        return countKeys() > order;
    }

    boolean isUnderFlow() {
        return countKeys() <= order / 2;
    }

    @Override
    public String toString() {
        return keys.toString();
    }
}
