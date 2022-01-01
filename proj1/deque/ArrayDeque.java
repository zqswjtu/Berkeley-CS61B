package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    //use an object array to store elements
    private Object[] array;
    //the initial size is 8.
    private static final int DEFAULT_CAPACITY = 8;
    //the number of elements it contains.
    private int size;
    private int firstItemIndex;
    private int lastItemIndex;

    public ArrayDeque() {

    }

    //ensure the array has enough space to store the new element
    private boolean ensureCapacity() {
        return array == null || size() >= array.length - 3;
    }

    //resize the array size to store more elements
    private void resize(int newCapacity) {
        if (array == null) {
            array = new Object[DEFAULT_CAPACITY];
            firstItemIndex = DEFAULT_CAPACITY - 1;
        } else {
            if (newCapacity < 0) {
                throw new OutOfMemoryError();
            }
            //rebuild the copy code with firstItemIndex and lastItemIndex
            //change firstItemIndex and lastItemIndex
            //array = Arrays.copyOf(array, newCapacity);
            int index = 0;
            Object[] newArray = new Object[newCapacity];
            int start = (firstItemIndex + 1) % array.length;
            int end = lastItemIndex;
            while (start != end) {
                newArray[index++] = array[start];
                start = (start + 1) % array.length;
            }
            array = newArray;
            //update firstItemIndex and lastItemIndex
            firstItemIndex = array.length - 1;
            lastItemIndex = index;
        }
    }

    @Override
    public void addFirst(T item) {
        if (ensureCapacity()) {
            int newCapacity = 2 * size();
            resize(newCapacity);
        }
        array[firstItemIndex] = item;
        firstItemIndex = (firstItemIndex - 1 + array.length) % array.length;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (ensureCapacity()) {
            int newCapacity = 2 * size();
            resize(newCapacity);
        }
        array[lastItemIndex] = item;
        lastItemIndex = (lastItemIndex + 1) % array.length;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        //rebuild the code with firstItemIndex and lastItemIndex
        int start = (firstItemIndex + 1) % array.length;
        int end = lastItemIndex;
        while (start != end) {
            System.out.print((T) (array[start]) + " ");
            start = (start + 1) % array.length;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if ((size < array.length / 4) && (size > 4)) {
            resize(array.length / 4);
        }
        firstItemIndex = (firstItemIndex + 1) % array.length;
        T item = (T) (array[firstItemIndex]);
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if ((size < array.length / 4) && (size > 4)) {
            resize(array.length / 4);
        }
        lastItemIndex = (lastItemIndex - 1 + array.length) % array.length;
        T item = (T) (array[lastItemIndex]);
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        //If no such element exists, return null
        if (index < 0 || isEmpty() || index >= size()) {
            return null;
        }
        //rebuild the code with firstItemIndex and lastItemIndex
        return (T) (array[(firstItemIndex + 1 + index) % array.length]);
        //return (T) (array[index]);
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

    private class AllItemIterator implements Iterator<T> {
        int index = 0;

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
