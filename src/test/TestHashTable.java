package test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ru.mail.polis.ISet;
import ru.mail.polis.OpenHashTable;

/**
 * Created by Nechaev Mikhail
 * Since 26/12/16.
 */
public class TestHashTable {

    private final Random random = new Random();

    public static void main(String[] args) {
        new TestHashTable().run();
    }

    private void run() {
        pre();
        t0();
        t1();
        t2();
        t3();
    }

    private void pre() {
        Set<String> OK = new HashSet<>();
        ISet<String> set = new OpenHashTable<>();
        String value = "b";
        for (int i = 0; i < 5; i++) {
            value = value + "a";
            System.err.println("A i = " + i + ", val = "
                    + value + ", " + set.add(value) + ", " + set.contains(value));
        }
    }

    private void t0() {
        ISet<String> set = new OpenHashTable<>();
        assert set.isEmpty();
        assert set.size() == 0;
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

    private void t1() {
        Set<String> OK = new HashSet<>();
        ISet<String> set = new OpenHashTable<>();
        final int LEN = 20;
        String value = "b";
        for (int i = 0; i < LEN; i++) {
            value = value + "a";
            check(OK, set, value, true);
        }
        for (int i = LEN; i >= 0; i--) {
            value = value.substring(0, value.length() - 1);
            check(OK, set, value, false);
        }

    }

    private void t2() {
        Set<String> OK = new HashSet<>();
        ISet<String> set = new OpenHashTable<>();
        for (char value = 'h'; value < 'm'; value++) {
            check(OK, set, value, true);
        }
        for (char value = 'n'; value >= 'h'; value--) {
            String v = "" + value;
            check(OK, set, value, false);
        }
        for (char value = 'h'; value >= 'a'; value--) {
            check(OK, set, value, false);
        }
        for (char value = 'a'; value < 'h'; value++) {
            check(OK, set, value, true);
        }
    }

    private void t3() {
        Set<String> OK = new HashSet<>();
        ISet<String> set = new OpenHashTable<>();
        for (int i = 0; i < 1000; i++) {
            check(OK, set, gen(10), true);
        }
        for (int i = 0; i < 1000; i++) {
            check(OK, set, gen(10), false);
        }
    }

    private String gen(int len) {
        String s = "";
        for (int i = 0; i < len; i++) {
            s += (char) random.nextInt(26) + 'a';
        }
        return s;
    }

    private void check(Set<String> OK, ISet<String> set, char c, boolean add) {
        check(OK, set, "" + c, add);
    }

    private void check(Set<String> OK, ISet<String> set, String value, boolean add) {
        assert OK.contains(value) == set.contains(value);
        assert OK.size() == set.size();
        assert OK.contains(value) == set.contains(value);
        if (add) {
            assert OK.add(value) == set.add(value);
        } else {
            assert OK.remove(value) == set.remove(value);
        }
        assert OK.size() == set.size();
        assert OK.contains(value) == set.contains(value);
        if (add) {
            assert OK.add(value) == set.add(value);
        } else {
            assert OK.remove(value) == set.remove(value);
        }
        assert OK.size() == set.size();
        assert OK.contains(value) == set.contains(value);
    }
}