package deque;

import java.util.Iterator;

public interface Deque<T> {
    /**
     * Add an item of type T to the front of the deque. Assume that item is never null.
     * @param item
     */
    void addFirst(T item);

    /**
     * Add an item of type T to the back of the deque. Assume that item is never null.
     * @param item
     */
    void addLast(T item);

    /**
     * Returns true if deque is empty, false otherwise.
     * @return
     */
    boolean isEmpty();

    /**
     * Returns the number of items in the deque.
     * @return
     */
    int size();

    /**
     *  Prints the items in the deque from first to last, separated by a space.
     *  Once all the items have been printed, print out a new line.
     */
    void printDeque();

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     * @return
     */
    T removeFirst();

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * @return
     */
    T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * @param index
     * @return
     */
    T get(int index);

    /**
     * Updates the item at given index with the given item, if no such item exists, return.
     * @param index
     * @param item
     */
    void set(int index, T item);

    /**
     * The Deque objects we’ll make are iterable (i.e. Iterable<T>) so we must provide this method to
     * return an iterator.
     * @return
     */
    Iterator<T> iterator();

    /**
     * Returns whether or not the parameter o is equal to the Deque. o is considered equal if it is a Deque
     * and if it contains the same contents (as goverened by the generic T’s equals method) in the same order.
     * @param o
     * @return
     */
    boolean equals(Object o);
}
