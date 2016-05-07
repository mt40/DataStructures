package me.mthai;

public class Main {

    public static void main(String[] args) {
        MTHashMap<Character, Integer> map = new MTHashMap<>();

        for(int i = 0; i < 10; ++i) {
            map.put((char)('a' + i), i + 1);
        }

        map.put('a', 99); // test duplicate
        
        System.out.printf("value of key 'a' is: %s\n", map.get('a'));
        System.out.printf("value of key 'g' is: %s\n", map.get('g'));

        System.out.println(map);
    }
}
