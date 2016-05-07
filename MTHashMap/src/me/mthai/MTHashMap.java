package me.mthai;

import java.util.Map;

/**
 * Created by mt on 1/2/2016.
 */
public class MTHashMap<K,V> {
    final int INITIAL_CAP = 8;
    final int MAX_CAP = 1 << 30;

    public int size = INITIAL_CAP;

    private Entry<K,V> []table;

    public MTHashMap() {
        initTable(INITIAL_CAP);
    }

    public MTHashMap(int initialCapacity) {
        initTable(initialCapacity);
    }

    private void initTable(int toSize) {
        int n = roundToPower2(toSize);
        table = new Entry[INITIAL_CAP];
    }

    private int roundToPower2(int x) {
        // Round to the next power of 2
        if(x >= MAX_CAP) return MAX_CAP;
        int high = Integer.highestOneBit(x);
        return high == 0
                ? INITIAL_CAP
                : (Integer.bitCount(x) == 1) ? x : high << 1;
    }

    public V get(K key) {
        Entry<K,V> e = getEntry(key);
        return (e != null) ? e.value : null;
    }

    private Entry<K,V> getEntry(K key) {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K,V> e = table[i]; e != null; e = e.next) {
            if(e.key.hashCode() == hash && e.key.equals(key))
                return e;
        }
        return null;
    }

    public V put(K key, V value) {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        // Find duplicate
        for(Entry<K,V> e = table[i]; e != null; e = e.next) {
            if(e.key.hashCode() == hash && e.key.equals(key)) {
                V old = e.value;
                e.value = value;

                Console.log("Duplicate key = " + key);
                return old;
            }
        }

        addEntry(key, value, hash, i);
        return null;
    }

    private void addEntry(K key, V value, int hash, int id) {
        if(size >= table.length) {
            resize(table.length << 1);
            id = indexFor(hash, table.length);
        }
        createEntry(key, value, hash, id);
    }

    private void createEntry(K key, V value, int hash, int id) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[id];
        table[id] = newEntry;
        size++;
    }

    private void resize(int toSize) {
        int old = table.length;
        if(old >= MAX_CAP)
            return;

        Entry<K,V> []newTable = new Entry[toSize];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Entry []newTable) {
        int n = newTable.length;
        for(Entry e : table) {
            while(e != null) {
                int i = indexFor(e.key.hashCode(), n);
                e.next = newTable[i];
                newTable[i] = e;
                e = e.next;
            }
        }
    }

    private int indexFor(int hash, int size) {
        /**
         * Because size is always a power of 2, size - 1 in binary
         * is 1...1111. So doing an AND operation will result in a
         * number in [0,size-1] which can be consider as a faster
         * version of hash % size
         */
        return hash & (size - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; ++i) {
            if(table[i] == null) continue;
            sb.append(String.format("bucket %d: ", i));
            for(Entry<K,V> e = table[i]; e != null; e = e.next) {
                sb.append(e).append((null == e.next) ? "" : ", ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    static class Entry<A,B> {
        A key;
        B value;
        Entry<A,B> next;

        public Entry(A key, B value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry<?, ?> entry = (Entry<?, ?>) o;

            if (!key.equals(entry.key)) return false;
            return value != null ? value.equals(entry.value) : entry.value == null;

        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}