package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K, V> {
    private static final class TreeNode<K, V> {
        K key;
        V value;
        TreeNode<K, V> left;
        TreeNode<K, V> right;

        public TreeNode(K key, V value, TreeNode<K, V> left, TreeNode<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    private int size;
    private TreeNode<K, V> root;

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    /*private int compare(Object k1, Object k2) {
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }*/

    private TreeNode<K, V> getNode(K key) {
        if (root != null) {
            TreeNode<K, V> tmp = root;
            while (tmp != null) {
                if (Objects.equals(key, tmp.key)) {
                    return tmp;
                } else if (key.compareTo(tmp.key) < 0) {
                    tmp = tmp.left;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public V get(K key) {
        TreeNode<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new TreeNode<>(key, value, null, null);
        } else {
            TreeNode<K, V> tmp = root;
            TreeNode<K, V> parent = null;
            while (tmp != null) {
                parent = tmp;
                if (key.compareTo(tmp.key) < 0) {
                    tmp = tmp.left;
                } else if (key.compareTo(tmp.key) > 0){
                    tmp = tmp.right;
                } else {
                    tmp.value = value;
                    return;
                }
            }
            if (key.compareTo(parent.key) < 0) {
                parent.left = new TreeNode<>(key, value, null, null);
            } else {
                parent.right = new TreeNode<>(key, value, null, null);
            }
        }
        ++size;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        LinkedList<TreeNode<K, V>> stack = new LinkedList<>();
        TreeNode<K, V> tmp = root;
        while (!stack.isEmpty() || tmp != null) {
            if (tmp != null) {
                stack.push(tmp);
                tmp = tmp.left;
            } else {
                tmp = stack.pop();
                keySet.add(tmp.key);
                tmp = tmp.right;
            }
        }
        return keySet;
    }

    private TreeNode<K, V> findLeftMost(TreeNode<K, V> treeNode) {
        while (treeNode.left != null) {
            treeNode = treeNode.left;
        }
        return treeNode;
    }

    private TreeNode<K, V> removeTreeNode(TreeNode<K, V> treeNode, TreeNode<K, V> tmp, K key) {
        if (treeNode == null) {
            return null;
        } else if (key.compareTo(treeNode.key) < 0) {
            treeNode.left = removeTreeNode(treeNode.left, tmp, key);
        } else if (key.compareTo(treeNode.key) > 0) {
            treeNode.right = removeTreeNode(treeNode.right, tmp, key);
        } else {
            tmp.value = treeNode.value;
            if (treeNode.left != null && treeNode.right != null) {
                TreeNode<K, V> node = findLeftMost(treeNode.right);
                treeNode.key = node.key;
                treeNode.value = node.value;
                treeNode.right = removeTreeNode(treeNode.right, tmp, treeNode.key);
            } else {
                treeNode = treeNode.left == null ? treeNode.right : treeNode.left;
            }
        }
        return treeNode;
    }

    @Override
    public V remove(K key) {
        TreeNode<K, V> tmp = new TreeNode<>(null, null, null, null);
        root = removeTreeNode(root, tmp, key);
        if (tmp.value != null) {
            --size;
            return tmp.value;
        }
        return null;
        /*TreeNode<K, V> node = getNode(key);
        if (node != null) {
            V value = node.value;
            root = removeTreeNode(root, key);
            --size;
            return value;
        }
        return null;*/
    }

    @Override
    public V remove(K key, V value) {
        TreeNode<K, V> node = getNode(key);
        if (node != null && Objects.equals(node.value, value)) {
            return remove(key);
        }
        return null;
    }

    public void printInOrder() {
        Iterator<K> iterator = iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            TreeNode<K, V> node = root;
            LinkedList<TreeNode<K, V>> stack = new LinkedList<>();
            @Override
            public boolean hasNext() {
                return !stack.isEmpty() || node != null;
            }

            @Override
            public K next() {
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
                node = stack.pop();
                K key = node.key;
                node = node.right;
                return key;
            }
        };
    }
}
