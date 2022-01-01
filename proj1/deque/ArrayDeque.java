package deque;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {

    //use an object array to store elements
    private Object[] array;
    //the initial size is 8.
    private static final int DEFAULT_CAPACITY = 8;
    //the max array size is set to 2 ^ 20
    private static final int MAX_CAPACITY = 2 << 20;
    //the number of elements it contains.
    private int size;

    public ArrayDeque() {
    }

    //ensure the array has enough space to store the new element
    private boolean ensureCapacity(){
        return array == null || size() == array.length;
    }

    //resize the array size to store more elements
    private void resize(){
        if (array == null) {
            array = new Object[DEFAULT_CAPACITY];
        } else {
            int newCapacity = 2 * size();
            if (newCapacity < 0) {
                throw new OutOfMemoryError();
            }
            array = Arrays.copyOf(array, newCapacity);
        }
    }

    @Override
    public void addFirst(T item) {
        if (ensureCapacity()) {
            resize();
        }
        for (int i = size(); i > 0; --i) {
            array[i] = array[i - 1];
        }
        array[0] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (ensureCapacity()) {
            resize();
        }
        array[size()] = item;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return array == null || size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size(); ++i) {
            System.out.print((T) (array[i]) + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()){
            return null;
        }
        T item = (T) (array[0]);
        for (int i = 0; i < size() - 1; ++i) {
            array[i] = array[i + 1];
        }
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()){
            return null;
        }
        T item = (T) (array[size()-1]);
        array[size()-1] = null;
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        //If no such element exists, return null
        if (index < 0 || isEmpty() || index >= size()) {
            return null;
        }
        return (T) (array[index]);
    }

    public void set(int index, T item) {
        //If no such element exists, return null
        if (index < 0 || isEmpty() || index >= size()) {
            return;
        }
        array[index] = item;
    }

    public Iterator<T> iterator() {
        return new AllItemIterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Deque) {
           Deque<T> deque = (Deque<T>) (obj);
           if (this.size() != deque.size()){
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

    private class AllItemIterator implements Iterator<T>, Iterable<T>{
        private int index = 0;

        @Override
        public Iterator<T> iterator() {
            return this;
        }

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
