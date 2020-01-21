package ru.chupaYchups.otus;

import java.util.*;

/**
 * Имплементация индексированного списка
 * в которой реализована:
 * - добавление элементов
 * - перебор элементов
 * - замена элементов
 */
public class DiyArrayList<T> implements List<T> {

    public static int DEFAULT_CAPACITY = 10;

    private static final int DEFAULT_INCREMENT_SIZE = 5;

    private Object[] array;

    private int listSize;

    public DiyArrayList() {
        array = new Object[DEFAULT_CAPACITY];
        listSize = 0;
    }

    public DiyArrayList(Integer capacity) {
        array = new Object[capacity];
        listSize = capacity;
    }

    @Override
    public int size() {
       return listSize;
    }

    @Override
    public boolean isEmpty() {
        return listSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, listSize);
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i, T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (listSize < array.length) {
            array[listSize++] = t;
        } else {
            resizeArray();
            array[listSize++] = t;
        }
        return false;
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= listSize) {
            throw new IndexOutOfBoundsException(i);
        } else {
            return (T)array[i];
        }
    }

    @Override
    public T set(int i, T t) {
        if (i < 0 || i >= listSize) {
            throw new IndexOutOfBoundsException(i);
        } else {
            array[i] = t;
        }
        return t;
    }

    private void resizeArray() {
        array = Arrays.copyOf(array, array.length + DEFAULT_INCREMENT_SIZE);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int i, int i1) {
        throw new UnsupportedOperationException();
    }

    private class DIYIterator implements Iterator<T> {

        private final static int EMPTY_ITERATOR_CURRENT_EL_INDEX = -1;

        protected int currentElementIndex;

        public DIYIterator() {
            currentElementIndex = EMPTY_ITERATOR_CURRENT_EL_INDEX;
        }

        @Override
        public boolean hasNext() {
            return currentElementIndex < DiyArrayList.this.size() - 1;
        }

        @Override
        public T next() {
            if (currentElementIndex >= DiyArrayList.this.array.length - 1) {
                resizeArray();
            }
            return DiyArrayList.this.get(++currentElementIndex);
        }
    }

    private class DIYListIterator extends DIYIterator implements ListIterator<T> {

        @Override
        public boolean hasPrevious() {
            return currentElementIndex > 0;
        }

        @Override
        public T previous() {
            return DiyArrayList.this.get(--currentElementIndex);
        }

        @Override
        public int nextIndex() {
            return currentElementIndex + 1;
        }

        @Override
        public int previousIndex() {
            return currentElementIndex - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            DiyArrayList.this.set(currentElementIndex, t);
        }

        @Override
        public void add(T t) {
            DiyArrayList.this.set(DiyArrayList.this.size(), t);
        }
    }
}
