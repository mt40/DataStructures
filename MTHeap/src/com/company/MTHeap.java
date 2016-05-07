package com.company;

import java.util.Arrays;
import java.util.Objects;

public class MTHeap<E extends Comparable<E>>{
    private final int DEFAULT_CAP = 8;
    Object []queue; // 0-indexed
    int size = 0;

    public MTHeap() {
        queue = new Object[DEFAULT_CAP];
    }

    public void add(E item) {
        int idx = size++;
        if(size == queue.length)
            grow();

        queue[idx] = item;
        upHeap(idx, item);
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        return (size > 0) ? (E)queue[0] : null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public E poll() {
        if(size == 0)
            return null;
        int leaf = --size;
        E root = (E)queue[0];
        queue[0] = queue[leaf];
        queue[leaf] = null;

        downHeap(0, (E)queue[0]);
        return root;
    }

    @SuppressWarnings("unchecked")
    public boolean remove(Object obj) {
        if(obj != null) {
            int idx = indexOf(obj);
            if (idx >= 0) {
                removeAt(idx, (E)obj);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void removeAt(int idx, E old) {
        int leaf = --size;
        swap(idx, leaf);
        queue[leaf] = null;

        E cur = (E)queue[idx];
        if(cur.compareTo(old) < 0)
            upHeap(idx, cur); // because this new value can be bigger than parent
        else if(cur.compareTo(old) > 0)
            downHeap(idx, cur); // the same, it can be bigger than children
    }

    private void grow() {
        int oldCap = queue.length;
        int newCap = oldCap + (oldCap >> 1); // rate = 1.5
        queue = Arrays.copyOf(queue, newCap);
    }

    private int indexOf(Object obj) {
        for(int i = 0; i < size; ++i)
            if(queue[i].equals(obj))
                return i;
        return -1;
    }

    @SuppressWarnings("unchecked")
    private void upHeap(int idx, E cur) {
        int p = parent(idx);
        if(p >= 0) {
            E elem = (E)queue[p];
            if(elem.compareTo(cur) > 0) {
                swap(p, idx);
                upHeap(p, cur);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void downHeap(int idx, E cur) {
        int left = leftChild(idx), right = rightChild(idx);
        if(left >= size) return;

        E l = (E)queue[left];
        E r = l;
        if(right < size)
            r = (E)queue[right];
        int i = left;
        E candidate = l;
        if(l.compareTo(r) > 0) {
            i = right;
            candidate = r;
        }

        if(candidate.compareTo(cur) < 0) {
            swap(idx, i);
            downHeap(i, cur);
            return;
        }
    }

    private void swap(int i, int j) {
        Object tmp = queue[i];
        queue[i] = queue[j];
        queue[j] = tmp;
    }

    private int leftChild(int idx) {
        return idx << 1 | 1;
    }

    private int rightChild(int idx) {
        return (idx << 1) + 2;
    }

    private int parent(int idx) {
        return (idx - 1) >> 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(0, sb);
        return sb.toString();
    }

    private void toString(int idx, StringBuilder sb) {
        if(idx >= size) return;
        String s = queue[idx].toString();
        sb.append(s).append(" ");
        toString(leftChild(idx), sb);
        toString(rightChild(idx), sb);
    }
}
