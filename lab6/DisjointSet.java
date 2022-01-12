public class DisjointSet {
    private class Node {
        private int val;
        private int size;
        private int rank;
        private Node(int val, int size, int rank) {
            this.val = val;
            this.size = size;
            this.rank = rank;
        }
    }
    private Node[] nodes;

    /**
     * Creates a UnionFind data structure holding n vertices.
     * Initially, all vertices are in disjoint sets.
     * @param n
     */
    public DisjointSet(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; ++i) {
            nodes[i] = new Node(i, 1, 0);
        }
    }

    /**
     * Throws an exception if v1 is not a valid index.
     * @param v1
     */
    public void validate(int v1) {
        if (v1 < 0 || v1 >= nodes.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Returns the size of the set v1 belongs to.
     * @param v1
     * @return
     */
    public int sizeOf(int v1) {
        validate(v1);
        int root = find(v1);
        return nodes[root].size;
    }

    /**
     * Returns the parent of v1.
     * If v1 is the root of a tree, returns the negative size of the tree for which v1 is the root.
     * @param v1
     * @return
     */
    public int parent(int v1) {
        validate(v1);
        if (v1 == nodes[v1].val) {
            return -nodes[v1].rank;
        }
        return v1 == nodes[v1].val ? -nodes[v1].rank : nodes[v1].val;
    }

    /**
     * Returns true if nodes v1 and v2 are connected.
     * @param v1
     * @param v2
     * @return
     */
    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    /**
     * Connects two elements v1 and v2 together. v1 and v2 can be any valid elements,
     * and a union-by-size heuristic is used. If the sizes of the sets are equal,
     * tie-break by connecting v1’s root to v2’s root. Unioning a vertex with itself
     * or vertices that are already connected should not change the sets, but it may
     * alter the internal structure of the data structure.
     * @param v1
     * @param v2
     */
    public void union(int v1, int v2) {
        if (connected(v1, v2)) {
            return;
        }
        Node root1 = doFind(v1);
        Node root2 = doFind(v2);
        if (root1.rank > root2.rank) {
            root2.val = root1.val;
            root1.size += root2.size;
        } else {
            root1.val = root2.val;
            root2.size += root1.size;
            if (root1.rank == root2.rank) {
                root2.rank++;
            }
        }
    }

    private Node doFind(int v1) {
        if (v1 == nodes[v1].val) {
            return nodes[v1];
        }
        return doFind(nodes[v1].val);
    }

    /**
     * Returns the root of the set v1 belongs to. Path-compression is employed
     * allowing for fast search-time.
     * @param v1
     * @return
     */
    public int find(int v1) {
        validate(v1);
        return doFind(v1).val;
    }
}
