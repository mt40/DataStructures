package me.mthai;

import java.util.Arrays;

/**
 * Created by mt on 1/4/2016.
 */
public class MTArrayList<E> {
    static final int DEFAULT_CAPACITY = 10;
    static final Object[] EMPTY = {};

    Object[] data;
    int size;

    public MTArrayList() {
        data = EMPTY;
    }

    public void add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
    }

    public void add(int index, E e) {
        rangeCheck(index);

        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = e;
        size++;
    }

    public void remove(Object o) {
        if(o == null) {
            for(int i = 0; i < size; ++i) {
                if(null == data[i]) {
                    fastRemove(i);
                }
            }
        }
        else {
            for(int i = 0; i < size; ++i) {
                if(o == data[i] || o.equals(data[i])) {
                    fastRemove(i);
                }
            }
        }
    }

    private void fastRemove(int index) {
        int toMove = size - index - 1;
        if(toMove > 0)
            System.arraycopy(data, index + 1, data, index, toMove);
        data[--size] = null;
    }

    public void clear() {
        for(int i = 0; i < size; ++i)
            data[i] = null;
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if(data == EMPTY) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        if(minCapacity - data.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        int oldCap = data.length;
        int newCap = oldCap + (oldCap >> 1); // grows with the rate of 1.5
        if(newCap - minCapacity < 0)
            newCap = minCapacity;
        data = Arrays.copyOf(data, newCap);

        console.log("grows to " + newCap);
    }

    private void rangeCheck(int index) {
        if(index > size || index < 0)
            throw new IndexOutOfBoundsException("" + index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; ++i)
            sb.append(data[i]).append(i == size - 1 ? "" : ", ");
        return sb.toString();
    }
}


