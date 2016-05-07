package me.mthai;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random();
	    MTArrayList<Integer> list = new MTArrayList<>();

        for(int i = 0; i < 15; ++i) {
            list.add(rand.nextInt(100));
        }
        list.add(10, 1994);
        list.add(0, 2000);
        list.add(3, 2001);
        list.add(15, 1995);
        list.remove(2001);
        list.remove(10); // remove if exists

        System.out.printf("size: %d, capacity: %d\n", list.size, list.data.length);
        System.out.println(list);

        System.out.println("clear");
        list.clear();
        System.out.println(list);
    }
}
