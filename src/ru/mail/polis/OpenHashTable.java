package ru.mail.polis;

import java.util.Comparator;

//TODO: write code here
public class OpenHashTable<E extends Comparable<E>> implements ISet<E> {

    private static final int INIT_CAPACITY = 8;

    //private int n;
    private int m;           // size of linear probing table
    //private Key[] keys;      // the keys
    private E[] vals;    // the values
    private int size;   // number of key-value pairs in the symbol table


    private Comparator<E> comparator;

    public OpenHashTable() {
        this(INIT_CAPACITY);

    }

    public OpenHashTable(int capacity) {
        m = capacity;
        size = 0;
        //keys = (Key[])   new Object[m];
        //vals = (E[]) new  Object[m];
        vals = (E[]) new Comparable[m];
    }

    public OpenHashTable(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(value) != null;
    }

    public E get(E key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (int i = hash(key); vals[i] != null; i = (i + 1) % m)
            if (vals[i].equals(key))
                return vals[i];
        return null;
    }

    private int hash(E key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    private void resize(int capacity) {
        OpenHashTable<E> temp = new OpenHashTable<E>(capacity);
        for (int i = 0; i < m; i++) {
            if (vals[i] != null) {
                temp.add(vals[i]);
            }
        }
        vals = temp.vals;
        m    = temp.m;
    }


    @Override
    public boolean add(E value) {

        if (value == null) throw new NullPointerException("first argument to put() is null");


        // double table size if 50% full
        if (size >= m/2) resize(2*m);

        int i;
        for (i = hash(value); vals[i]!= null; i = (i + 1) % m) {
            if (vals[i].equals(value)) {
                return false;
            }
        }

        vals[i] = value;
        size++;
        return true;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) throw new NullPointerException("argument to delete() is null");
        if (!contains(value)) return false;

        // find position i of key
        int i = hash(value);
        while (!value.equals(vals[i])) {
            i = (i + 1) % m;
        }

        // delete key and associated value
        vals[i] = null;

        // rehash all keys in same cluster
        i = (i + 1) % m;
        while (vals[i] != null) {
            // delete keys[i] an vals[i] and reinsert
            E   valToRehash = vals[i];
            vals[i] = null;
            size--;
            add(valToRehash);
            i = (i + 1) % m;
        }

        size--;

        // halves size of array if it's 12.5% full or less
        if (size > 0 && size <= m/8) resize(m/2);
        return true;
    }
}
