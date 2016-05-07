package me.mthai;

import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by mt on 2/18/2016.
 */
public class MTSkipList<V extends Comparable<V>> {
    final int MAX_LEVEL = 32; // 0->31

    private Random rand = new Random();
    private Node<V> head = new Node<>(null, 33);
    private int levels = 1; // current number of levels

    private PrintWriter out = new PrintWriter(System.out); // debug

    public void add(V val) {
        int addLevel = levelToAdd();

        Node<V> newNode = new Node<>(val, addLevel + 1);
        Node<V> cur = head;
        for(int lv = levels - 1; lv >= 0; --lv) {
            // Find the correct position to add
            for(; cur.next[lv] != null; cur = cur.next[lv]) {
                if(cur.next[lv].val.compareTo(val) > 0)
                    break;
            }
            // Also, if we're at the correct level, add it!
            if(lv <= addLevel) {
                newNode.next[lv] = cur.next[lv];
                cur.next[lv] = newNode;
            }
        }
    }

    public boolean contains(V val) {
        Node<V> cur = head;
        for(int lv = levels - 1; lv >= 0; --lv) {
            for(; cur.next[lv] != null; cur = cur.next[lv]) {
                if(cur.next[lv].val.compareTo(val) == 0)
                    return true;
                else if(cur.next[lv].val.compareTo(val) > 0)
                    break; // not found at this level
            }
        }
        return false;
    }

    private int levelToAdd() {
        int addLevel = 0; // level to add
        while(rand.nextBoolean()) {
            addLevel++;
            if(addLevel == levels) {
                levels++;
                break;
            }
        }
        return addLevel;
    }

    @Override
    public String toString() {
        String rs = ""; // for simplicity, don't judge me :(
        for(int i = levels - 1; i >= 0; --i) {
            rs += String.format("[%d]: ", i);
            for(Node nd = head.next[i]; nd != null; nd = nd.next[i])
                rs += nd + " ";
            if(i > 0)
                rs += '\n';
        }
        return rs;
    }

    static class Node<T> {
        T val;
        Node<T> []next; // next_i = pointer to the next node at level i

        public Node(T val, int nLevel) {
            this.val = val;
            next = new Node[nLevel];
        }

        @Override
        public String toString() {
            return "{" + val + "}";
        }
    }
}
