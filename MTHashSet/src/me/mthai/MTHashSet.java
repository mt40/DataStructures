package me.mthai;

public class MTHashSet<K> {
    final int INITIAL_CAP = 4;
    final int MAX_CAP = 1 << 30;

    private Entry<K> [] table;
    private int size;

    public MTHashSet() {
        createTable(INITIAL_CAP);
    }

    public MTHashSet(int initialCapacity) {
        int powerOf2 = roundToPowerOf2(initialCapacity);
        createTable(powerOf2);
    }

    private void createTable(int cap) {
        table = new Entry[cap];

        Console.log("Created with initial size = " + cap);
    }

    public boolean add(K key) {
        if(size == table.length)
            expandTable();

        int hash = key.hashCode();
        int id = indexFor(hash, table.length);

        // Check if already exist
        if(contains(key))
            return false;

        Entry<K> newEntry = new Entry<>(key);
        newEntry.next = table[id];
        table[id] = newEntry;
        size++;
        return true;
    }

    /**
     * @return true if not already contain the element
     */
    public boolean contains(K key) {
        int hash = key.hashCode();
        int id = indexFor(hash, table.length);

        for(Entry<K> e = table[id]; e != null; e = e.next) {
            if(e.key.equals(key) && e.key.hashCode() == hash)
                return true;
        }
        return false;
    }

    /**
     * @return false if element does not exist
     */
    public boolean remove(K key) {
        int hash = key.hashCode();
        int id = indexFor(hash, table.length);
        boolean rs = false;

        Entry<K> prev = table[id];
        if(prev != null && prev.key.equals(key) && prev.key.hashCode() == hash) {
            table[id] = prev.next;
            rs = true;
        }
        else if(prev != null) {
            for(Entry<K> e = table[id]; e != null; e = e.next) {
                if(e.key.equals(key) && e.key.hashCode() == hash) {
                    prev.next = e.next;
                    rs = true;
                    break;
                }
                prev = e;
            }
        }

        if(rs) {
            size--;
            return true;
        }
        Console.log("Remove " + key + " but it does not exist.");
        return false;
    }

    private int roundToPowerOf2(int n) {
        if(n >= MAX_CAP)
            return MAX_CAP;
        if(n == 0)
            return INITIAL_CAP;
        return (Integer.bitCount(n) == 1)
                ? n
                : Integer.highestOneBit(n) << 1;
    }

    private void expandTable() {
        int newSize = size * 2;
        Entry<K> []newTable = new Entry[newSize];
        transfer(newTable, newSize);
        table = newTable;

        Console.log("Expanded to size = " + newSize);
    }

    private void transfer(Entry<K> []newTable, int newSize) {
        for(int i = 0; i < table.length; ++i) {
            for(Entry<K> e = table[i]; e != null;) {
                int hash = e.key.hashCode();
                int newId = indexFor(hash, newSize);

                Entry<K> next = e.next;
                e.next = newTable[newId];
                newTable[newId] = e;

                e = next;
            }
        }
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < table.length; ++i)
            for(Entry<K> e = table[i]; e != null; e = e.next)
                s += e + " ";
        return s;
    }

    static class Entry<K> {
        K key;
        Entry<K> next;

        public Entry(K key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key.toString();
        }
    }
}
