package ru.mail.polis;

import java.util.*;

//TODO: write code here
public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

    private Node root;

    private class Node {
        private final E key;      // the key
        private int height;      // height of the subtree
        //private int size;        // number of nodes in subtree
        private Node left;       // left subtree
        private Node right;      // right subtree

        public Node(E key) {
            this.key = key;
            //this.size = size;
           // this.height = height;
        }
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("N{");
            sb.append("d=").append(key);
            if (left != null) {
                sb.append(", l=").append(left);
            }
            if (right != null) {
                sb.append(", r=").append(right);
            }
            sb.append('}');
            return sb.toString();
        }
    }
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.key;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<E>(size);
        inorderTraverse(root, list);
        return list;
    }

    private void inorderTraverse(Node curr, List<E> list) {
        if (curr == null) {
            return;
        }
        inorderTraverse(curr.left, list);
        list.add(curr.key);
        inorderTraverse(curr.right, list);
    }

    @Override
    public int size() {
        return size;
    }

//    private int size(Node x) {
//        if (x == null) return 0;
//        return x.size;
//    }
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(E key) {
        if (key == null) {
            throw new NullPointerException("value is null");
        }
        if (root != null) {
            Node curr = root;
            while (curr != null) {
                int cmp = compare(curr.key, key);
                if (cmp == 0) {
                    return true;
                } else if (cmp < 0) {
                    curr = curr.right;
                } else {
                    curr = curr.left;
                }
            }
        }
        return false;
    }

    public int height() {
        return height(root);
    }

    private int height(Node curr) {
        if (curr == null) return -1;
        return curr.height;
    }

    int balanceFactor(Node curr)
    {
        return height(curr.right)-height(curr.left);
    }

    void fixHeight(Node curr)
    {
        int hl = height(curr.left);
        int hr = height(curr.right);
        curr.height= (hl>hr?hl:hr)+1;
    }

    private Node rotateRight(Node curr) {
        Node q = curr.left;
        curr.left = q.right;
        q.right = curr;
        //q.size = curr.size;
        //curr.size = 1 + size(curr.left) + size(curr.right);
        fixHeight(curr);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node curr) // левый поворот вокруг curr
    {
        Node p = curr.right;
        curr.right = p.left;
        p.left = curr;
        //p.size = curr.size;
        //curr.size = 1 + size(curr.left) + size(curr.right);
        fixHeight(curr);
        fixHeight(p);
        return p;
    }

    private Node balance(Node curr) // балансировка узла p
    {
        fixHeight(curr);
        if( balanceFactor(curr)==2 )
        {
            if( balanceFactor(curr.right) < 0 )
                curr.right = rotateRight(curr.right);
            return rotateLeft(curr);
        }
        if( balanceFactor(curr)==-2 )
        {
            if( balanceFactor(curr.left) > 0  )
                curr.left = rotateLeft(curr.left);
            return rotateRight(curr);
        }
        return curr; // балансировка не нужна
    }


    @Override
    public boolean add(E value) { // могут быть проблемы с балансировкой и т.д
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (root == null) {
            root = new Node(value);
        } else {
            Node curr = root;
            while (true) {
                int cmp = compare(curr.key, value);
                if (cmp == 0) {
                    return false;
                } else if (cmp < 0) {
                    if (curr.right != null) {
                        curr = curr.right;
                        balanceFactor(curr);
                    } else {
                        curr.right = new Node(value);
                        break;
                    }
                } else if (cmp > 0) {
                    if (curr.left != null) {
                        curr = curr.left;
                        balanceFactor(curr);
                    } else {
                        curr.left = new Node(value);
                        break;
                    }
                }
            }
        }
        size++;
        return true;
    }
    private boolean cont;
    @Override
    public boolean remove(E value) {
        if (value == null) throw new NullPointerException("argument to delete() is null");

        cont=false;
        root = delete(root, value);
        if(cont!=false)  {
            size--;
        }
        return cont;

        /*if (!contains(value)) return false;
        root = delete(root, value);
        size--;
        return true;*/
    }

    private Node delete(Node x, E key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        }
        else if (cmp > 0) {
            x.right = delete(x.right, key);
        }
        else {
            cont=true;
            if (x.left == null) {
                return x.right;
            }
            else if (x.right == null) {
                return x.left;
            }
            else {
                Node y = x;
                x = min(y.right);
                x.right = deleteMin(y.right);
                x.left = y.left;
            }
        }
        //x.size = 1 + size(x.left) + size(x.right);
        fixHeight(x);
        return balance(x);
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("called deleteMin() with empty symbol table");
        root = deleteMin(root);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }


    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        //x.size = 1 + size(x.left) + size(x.right);
        fixHeight(x);

        return balance(x);
    }



    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    @Override
    public String toString() {
        return "AVL{" + root + "}";
    }

    /*public static void main(String[] args) {
        *//*AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(10);
        tree.remove(15);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(5);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);
        tree.add(15);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);

        System.out.println("------------");
        Random rnd = new Random();
        tree = new AVLTree<>();
        for (int i = 0; i < 15; i++) {
            tree.add(rnd.nextInt(50));
        }
        System.out.println(tree.inorderTraverse());
        tree = new AVLTree<>((v1, v2) -> {
            // Even first
            final int c = Integer.compare(v1 % 2, v2 % 2);
            return c != 0 ? c : Integer.compare(v1, v2);
        });
        for (int i = 0; i < 15; i++) {
            tree.add(rnd.nextInt(50));
        }
        System.out.println(tree.inorderTraverse());*//*
        AVLTree<Integer> set = new AVLTree<Integer>();
        TreeSet<Integer> OK = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            assert set.add(i) == OK.add(i);
        }
        for (int i = 10; i >= 0; i--) {
            assert set.remove(i) == OK.remove(i);
            assert set.contains(i) == OK.contains(i);
        }
    }*/
}
