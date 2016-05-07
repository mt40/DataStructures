package com.company;

import java.io.PrintStream;

public class Main {

    final static PrintStream out = System.out;
    public static void main(String[] args) {
        final int []data = {5, 6, 9, 4, 2, 6, 10, 12, 4, 1, 6};
        MTHeap<Integer> heap = new MTHeap<>();

        for(int i : data)
            heap.add(i);

        out.print("Init: ");
        out.println(heap);

        out.print("Remove 6: ");
        out.println(heap.remove(6));

        out.print("Remove 6: ");
        out.println(heap.remove(6));

        out.print("Remove 7: ");
        out.println(heap.remove(7));

        out.print("Peek: ");
        out.println(heap.peek());

        out.print("Poll (heap sort): ");
        while(!heap.isEmpty()) {
            out.print(heap.poll() + " ");
        }
    }
}
