package hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    private int size;
    private int threshold;
    private final double loadFactor;

    /** Constructors */
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        if (initialSize < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialSize);
        if (initialSize > MAXIMUM_CAPACITY)
            initialSize = MAXIMUM_CAPACITY;
        if (maxLoad <= 0 || Double.isNaN(maxLoad))
            throw new IllegalArgumentException("Illegal load factor: " + maxLoad);
        this.loadFactor = maxLoad;
        this.threshold = tableSizeFor(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     * @return
     */
    private Collection<Node>[] createTable(int tableSize) {
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        if (buckets != null && size() > 0) {
            size = 0;
            buckets = null;
        }
    }

    private Node getNode(int hashValue, K key) {
        assert key != null;
        if (buckets != null && size() > 0) {
            int index = hashValue & (buckets.length - 1);
            if (buckets[index] != null) {
                for (Node node : buckets[index]) {
                    if (Objects.equals(node.key, key)) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new NullPointerException();
        return getNode(hash(key), key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) throw new NullPointerException();
        Node node = getNode(hash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        Collection<Node>[] oldBuckets = buckets;
        int newLength;
        int oldLength = oldBuckets == null ? 0 : buckets.length;
        if (oldLength == 0) {
            newLength = threshold > 0 ? threshold : DEFAULT_INITIAL_CAPACITY;
        } else if (oldLength >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        } else {
            newLength = oldLength << 1;
        }
        buckets = createTable(newLength);
        threshold = (int) (loadFactor * newLength);
        if (oldBuckets != null) {
            for (int i = 0; i < oldLength; ++i) {
                Collection<Node> bucket = oldBuckets[i];
                if (bucket != null) {
                    oldBuckets[i] = null;
                    Collection<Node> lowBucket = null, highBucket = null;
                    for (Node node : bucket) {
                        if ((hash(node.key) & oldLength) == 0) {
                            if (lowBucket == null) lowBucket = createBucket();
                            lowBucket.add(node);
                        } else {
                            if (highBucket == null) highBucket = createBucket();
                            highBucket.add(node);
                        }
                    }
                    if (lowBucket != null) buckets[i] = lowBucket;
                    if (highBucket != null) buckets[i + oldLength] = highBucket;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new NullPointerException();
        if (buckets == null || buckets.length == 0) resize();
        int index = hash(key) & (buckets.length - 1);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            buckets[index] = createBucket();
        } else {
            for (Node node : bucket) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
            }
        }
        buckets[index].add(createNode(key, value));
        if (++size > threshold) resize();
    }

    @Override
    public Set<K> keySet() {
        if (buckets == null || size() == 0) return null;
        Set<K> set = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    set.add(node.key);
                }
            }
        }
        return set;
    }

    @Override
    public V remove(K key) {
        if (key == null) throw new NullPointerException();
        if (buckets == null || size() == 0) return null;
        int index = hash(key) & (buckets.length - 1);
        Collection<Node> bucket = buckets[index];
        if (bucket != null) {
            Iterator<Node> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (Objects.equals(node.key, key)) {
                    V value = node.value;
                    iterator.remove();
                    --size;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) throw new NullPointerException();
        if (buckets == null || size() == 0) return null;
        int index = hash(key) & (buckets.length - 1);
        Collection<Node> bucket = buckets[index];
        if (bucket != null) {
            Iterator<Node> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (Objects.equals(node.key, key) && Objects.equals(node.value, value)) {
                    iterator.remove();
                    --size;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    /*private class MyHashMapIterator implements Iterator<K> {
        private Set<K> keys;
        private Iterator<K> iterator;

        public MyHashMapIterator() {
            keys= keySet();
            if (keys != null) iterator = keys.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public K next() {
            return iterator.next();
        }
    }*/

    private class MyHashMapIterator implements Iterator<K> {
        // table index
        private int index, len;
        // the bucket at table index
        private Iterator<Node> iterator;

        MyHashMapIterator() {
            if (buckets != null && size() > 0) {
                len = buckets.length;
                for (int i = 0; i < len; ++i) {
                    if (buckets[i] != null) {
                        index = i;
                        iterator = buckets[i].iterator();
                        break;
                    }
                }
            }
        }

        @Override
        public boolean hasNext() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public K next() {
            K key = iterator.next().key;
            if (!iterator.hasNext()) {
                boolean flag = true;
                for (int i = index; i < len; ++i) {
                    if (buckets[i] != null) {
                        index = i;
                        iterator = buckets[i].iterator();
                        flag = false;
                        break;
                    }
                }
                if (flag) iterator = null;
            }
            return key;
        }
    }
}
