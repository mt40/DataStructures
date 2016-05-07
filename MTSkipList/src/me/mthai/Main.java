package me.mthai;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        PrintStream out = System.out;

        int []a = {5, 1, 4, 10, 15, 99, 6, 4};
        MTSkipList<Integer> skipList = new MTSkipList<>();

        for(int ai : a)
            skipList.add(ai);

        out.printf("Contains %d: %b\n", 6, skipList.contains(6));
        out.printf("Contains %d: %b\n", 4, skipList.contains(4));
        out.printf("Contains %d: %b\n", 12, skipList.contains(12));

        out.println("Built list:");
        out.println(skipList);
    }
}
