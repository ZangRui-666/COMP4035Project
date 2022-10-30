import java.util.*;

/**
 * 一颗 M 阶的B+树满足以下性质:
 * (1)每个结点至多有M个孩子
 * (2)除根结点外，每个结点至少有[m/2]个孩子，非叶子节点的根结点至少有两个孩子
 * (3)有k个子女的结点必有k个关键字
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class BPlusTree<K extends Comparable<? super K>, V> {

    /**
     * 根节点
     */
    private Node root;

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
                if (node instanceof BPlusTree.InternalNode) {
                    queue.addAll(((InternalNode) node).children);
                }
            }
            str.append(s).append("\n");
        }
        return str.toString();
    }

    public V get(K key) {
        return root.getVal(key);
    }

    public void put(K key, V value) {
        root.putVal(key, value);
    }

    public V remove(K key) {
        return root.remove(key);
    }

    private abstract class Node {
        List<K> keys;

        public Node() {
            this.keys = new ArrayList<>(order + 1);
        }

        abstract V getVal(K key);

        abstract void putVal(K key, V value);

        abstract V remove(K key);

        abstract Node split();

        abstract void merge(Node node);

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

    private class InternalNode extends Node {
        List<Node> children;

        public InternalNode() {
            super();
            children = new ArrayList<>(order + 1);
        }

        public InternalNode(List<K> keys, List<Node> children) {
            this();
            this.keys.addAll(keys);
            this.children.addAll(children);
        }

        @Override
        V getVal(K key) {
            int pos = Collections.binarySearch(keys, key);
            int childPos = pos >= 0 ? pos : -pos - 1;
            if (childPos >= countKeys()) {
                return null;
            }
            Node child = getChild(childPos);
            return child.getVal(key);
        }

        @Override
        void putVal(K key, V value) {
            int pos = Collections.binarySearch(keys, key);
            int childPos = pos >= 0 ? pos : Math.min(-pos, countKeys()) - 1;
            Node child = getChild(childPos);
            child.putVal(key, value);
            if (child.isOverFlow()) {
                Node siblingNode = child.split();
                keys.set(childPos, child.getLastLeafKey());
                insertChild(siblingNode);
            }
            keys.set(childPos, child.getLastLeafKey());
            if (root.isOverFlow()) {
                Node siblingNode = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.addAll(Arrays.asList(this.getLastLeafKey(), siblingNode.getLastLeafKey()));
                newRoot.children.addAll(Arrays.asList(this, siblingNode));
                root = newRoot;
            }
        }

        @Override
        V remove(K key) {
            int pos = Collections.binarySearch(keys, key);
            int childPos = pos >= 0 ? pos : Math.min(-pos, countKeys()) - 1;
            Node child = getChild(childPos);
            V value = child.remove(key);
            keys.set(childPos, child.getLastLeafKey());
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
                keys.set(rightPos - 1, left.getLastLeafKey());
                if (left.isOverFlow()) {
                    Node siblingNode = left.split();
                    keys.set(rightPos - 1, left.getLastLeafKey());
                    insertChild(siblingNode);
                }
                if (root.countKeys() < 2) {
                    root = left;
                }
            }
            return value;
        }

        @Override
        Node split() {
            int from = countKeys() / 2, to = countKeys();
            List<K> subKeys = keys.subList(from, to);
            List<Node> subChildren = children.subList(from, to);
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
        K getLastLeafKey() {
            Node firstChild = children.get(countKeys() - 1);
            return firstChild.getLastLeafKey();
        }

        private void insertChild(Node node) {
            K key = node.getLastLeafKey();
            int pos = Collections.binarySearch(keys, key);
            int insertPos = pos >= 0 ? pos : -pos - 1;
            keys.add(insertPos, key);
            children.add(insertPos, node);
        }

        private Node getChild(int childPos) {
            return children.get(childPos);
        }
    }

    private class LeafNode extends Node {
        List<V> values;
        LeafNode next;

        public LeafNode() {
            super();
            values = new ArrayList<>(order + 1);
        }

        public LeafNode(List<K> keys, List<V> values) {
            this();
            this.keys.addAll(keys);
            this.values.addAll(values);
        }

        @Override
        V getVal(K key) {
            int pos = Collections.binarySearch(keys, key);
            return pos >= 0 ? values.get(pos) : null;
        }

        @Override
        void putVal(K key, V value) {
            int pos = Collections.binarySearch(keys, key);
            if (pos >= 0) {
                values.set(pos, value);
                return;
            }
            int insertPos = -pos - 1;
            keys.add(insertPos, key);
            values.add(insertPos, value);
            if (root.isOverFlow()) {
                Node siblingNode = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.addAll(Arrays.asList(this.getLastLeafKey(), siblingNode.getLastLeafKey()));
                newRoot.children.addAll(Arrays.asList(this, siblingNode));
                root = newRoot;
            }
        }

        @Override
        V remove(K key) {
            int pos = Collections.binarySearch(keys, key);
            if (pos >= 0) {
                keys.remove(pos);
                return values.remove(pos);
            }
            return null;
        }

        @Override
        Node split() {
            int from = countKeys() / 2, to = countKeys();
            List<K> subKeys = keys.subList(from, to);
            List<V> subValues = values.subList(from, to);
            LeafNode siblingNode = new LeafNode(subKeys, subValues);
            subKeys.clear();
            subValues.clear();
            siblingNode.next = next;
            next = siblingNode;
            return siblingNode;
        }

        @Override
        void merge(Node node) {
            LeafNode mergeNode = (LeafNode) node;
            this.keys.addAll(mergeNode.keys);
            this.values.addAll(mergeNode.values);
        }

        @Override
        K getLastLeafKey() {
            return keys.get(countKeys() - 1);
        }
    }
}
