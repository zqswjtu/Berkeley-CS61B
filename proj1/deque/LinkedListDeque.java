package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    //the number of elements it contains
    private int size = 0;

    //a pointer points to the first element of the deque
    private Node<T> first = null;
    //a pointer points to the last element of the deque
    private Node<T> last = null;

    public LinkedListDeque() {

    }

    @Override
    public void addFirst(T item) {
        Node<T> f = first;
        Node<T> newNode = new Node<>(item, null, f);
        //set pointer first points to the new head element newNode.
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        ++size;
    }

    @Override
    public void addLast(T item) {
        Node<T> l = last;
        Node<T> newNode = new Node<>(item, l, null);
        //set pointer last points to the new tail element newNode.
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        ++size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> tmp = first;
        while (tmp != null) {
            System.out.print(tmp.item + " ");
            tmp = tmp.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        Node<T> f = first;
        if (f == null) {
            return null;
        }
        Node<T> next = f.next;
        T item = f.item;
        f.item = null;
        f.next = null;
        //set the pointer first points to the new head element after removeFirst
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        Node<T> l = last;
        if (l == null) {
            return null;
        }
        Node<T> prev = l.prev;
        T item = l.item;
        l.item = null;
        l.prev = null;
        //set the pointer last points to the new tail element after removeLast
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;
        return item;
    }

    private Node<T> getNode(int index) {
        //If no such element exists, return null
        if (index < 0 || isEmpty() || index >= size()) {
            return null;
        }
        int count = 0;
        Node<T> tmp = null;
        if (index < size() / 2) {
            tmp = first;
            while (count != index) {
                ++count;
                tmp = tmp.next;
            }
        } else {
            tmp = last;
            while (count != size() - 1 - index) {
                ++count;
                tmp = tmp.prev;
            }
        }
        return tmp;
    }

    @Override
    public T get(int index) {
        Node<T> node = getNode(index);
        return node == null ? null : node.item;
    }

    /*public void set(int index, T item) {
        Node<T> node = getNode(index);
        if (node != null) {
            node.item = item;
        }
    }*/

    private T getItemRecursive(int index, Node<T> head) {
        if (index == 0) {
            return head.item;
        }
        return getItemRecursive(index - 1, head.next);
    }

    /**
     * Get the element at the given index by recursion. If no such item exists, returns null.
     * @param index
     * @return
     */
    public T getRecursive(int index) {
        //If no such element exists, return null
        if (index < 0 || isEmpty() || index >= size()) {
            return null;
        }
        Node<T> f = first;
        return getItemRecursive(index, f);
    }

    public Iterator<T> iterator() {
        return new AllItemIterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Deque) {
            Deque<T> deque = (Deque<T>) (obj);
            if (this.size() != deque.size()) {
                return false;
            }
            for (int i = 0; i < size(); ++i) {
                if (!this.get(i).equals(deque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;
        private Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private class AllItemIterator implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public T next() {
            T item = get(index);
            index++;
            return item;
        }
    }
}
