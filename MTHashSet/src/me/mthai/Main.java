package me.mthai;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        MTHashSet<Integer> set = new MTHashSet<>();

        int []a = {1, 18, 6, 7, 12, 4, 13, 13, 15, 15};
        int []b = {6, 10, 13, 15, 9, 13};
        int []c = {6, 7, 13, 9, 15, 4};
        for(int ai : a)
            set.add(ai);

        System.out.println("After insertion: " + set);

        for(int bi : b)
            System.out.printf("Remove %d: %b\n", bi, set.remove(bi));

        System.out.println("After removal: " + set);

        for(int ci : c)
            System.out.printf("Contains %d: %b\n", ci, set.contains(ci));
    }
}
