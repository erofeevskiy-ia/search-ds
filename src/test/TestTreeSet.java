package test;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import ru.mail.polis.AVLTree;
import ru.mail.polis.ISortedSet;
import ru.mail.polis.RedBlackTree;

public class TestTreeSet {

    private final Random random = new Random();

    private final Comparator<Integer> EVEN_FIRST = (v1, v2) -> {
        final int c = Integer.compare(v1 % 2, v2 % 2);
        return c != 0 ? c : Integer.compare(v1, v2);
    };
    private final Comparator<Integer> ALL_EQUALS = (v1, v2) -> 0;

    //private final List<Comparator<Integer>> comparators = Arrays.asList(
    //        null
    //);

    public static void main(String[] args) {
        new TestTreeSet().run();
    }

    private void run() {
        run(() -> {
            pre();
            return null;
        });
        //test(AVLTree.class.getName());
        test(RedBlackTree.class.getName());
    }

    private void pre() {
        int LEN = 10;
        SortedSet<Integer> OK = new TreeSet<>();
        //ISortedSet<Integer> set = new AVLTree<>();
         ISortedSet<Integer> set = new RedBlackTree<>();
        for (int value = 0; value < LEN; value++) {
            check(OK, set, value, true);
        }
        for (int value = LEN; value >= 0; value--) {
            check(OK, set, value, false);
        }
    }

    private void test(String className) {
        run(() -> {
            checkEmptyAndNull(create(className, null));
            return null;
        });
        run(() -> {
            smallTest(create(className, null));
            return null;
        });
        run(() -> {
            bigRandomTest(create(className, null));
            return null;
        });
        //for (ListIterator<Comparator<Integer>> iterator = comparators.listIterator(); iterator.hasNext(); ) {
        //    Comparator<Integer> comp = iterator.next();
        //    run(() -> {
        //        testTree(iterator.previousIndex(), new TreeSet<>(comp), create(className, comp));
        //        return null;
        //    });
        //}
    }

    private void smallTest(ISortedSet<Integer> set) {
        TreeSet<Integer> OK = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            check(OK, set, i, true);
        }
        for (int i = 10; i >= 0; i--) {
            check(OK, set, i, false);
        }
        for (int i = 0; i >= -10; i--) {
            check(OK, set, i, false);
        }
        for (int i = -10; i < 0; i++) {
            check(OK, set, i, true);
        }
    }

    private void testTree(int cmpIdx, SortedSet<Integer> OK, ISortedSet<Integer> set) {
        System.out.println("-------------------- " + cmpIdx + " -------------------");
        for (int i = 0; i < 15; i++) {
            check(OK, set, i, true);
        }
        System.out.println(set.inorderTraverse());
        for (int i = 15; i < 30; i++) {
            check(OK, set, i, true);
            if (i % 2 == 0) {
                check(OK, set, i, false);
            }
        }
        System.out.println(set.inorderTraverse());
        for (int i = 10; i >= 0; i--) {
            check(OK, set, i, false);
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^ " + cmpIdx + " ^^^^^^^^^^^^^^^^^^^");
    }

    private void bigRandomTest(ISortedSet<Integer> set) {
        SortedSet<Integer> OK = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            check(OK, set, random.nextInt(10), true);
        }
        for (int i = 0; i < 10; i++) {
            check(OK, set, random.nextInt(10), false);
        }
    }



    private void check(SortedSet<Integer> OK, ISortedSet<Integer> set, int value, boolean add) {

        assert OK.contains(value) == set.contains(value);
        if (!OK.isEmpty()) {
            assert OK.first().equals(set.first());
            assert OK.last().equals(set.last());
        }
        assert OK.size() == set.size();
        assert OK.contains(value) == set.contains(value);
        if (add) {
            assert OK.add(value) == set.add(value);
        } else {
            assert OK.remove(value) == set.remove(value);
        }
        assert OK.size() == set.size();
        System.out.println(value + " : " + "OK - " + OK.contains(value)
                + " set - " + set.contains(value));
        System.out.println(OK.toString());
        System.out.println(set.inorderTraverse());
        assert OK.contains(value) == set.contains(value);
        if (add) {
            assert OK.add(value) == set.add(value);
        } else {
            assert OK.remove(value) == set.remove(value);
        }
        assert OK.size() == set.size();
        assert OK.contains(value) == set.contains(value);
        if (!OK.isEmpty()) {
            assert OK.first().equals(set.first());
            assert OK.last().equals(set.last());
        }
    }

    private void checkEmptyAndNull(ISortedSet<Integer> set) {
        assert set.isEmpty();
        assert set.size() == 0;
        try {
            set.first();
            assert false;
        } catch (NoSuchElementException e) {
            /* empty */
        }
        try {
            set.last();
            assert false;
        } catch (NoSuchElementException e) {
            /* empty */
        }
        try {
            set.add(null);
            assert false;
        } catch (NullPointerException e) {
            /* empty */
        }
        try {
            set.remove(null);
            assert false;
        } catch (NullPointerException e) {
            /* empty */
        }
    }

    public void run(Callable<Void> callable) {
        try {
            callable.call();
        } catch (AssertionError | Exception ae) {
            ae.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private ISortedSet<Integer> create(String className, Comparator<Integer> comparator) {
        try {
            return (ISortedSet<Integer>) Class.forName(className).getConstructor(Comparator.class).newInstance(comparator);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }
}