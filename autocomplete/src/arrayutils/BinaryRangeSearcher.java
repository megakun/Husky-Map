package arrayutils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Make sure to check out the interface for more method details:
 *
 * @see ArraySearcher
 */
public class BinaryRangeSearcher<T, U> implements ArraySearcher<T, U> {
    private final T[] array;
    private final Matcher<T, U> matcher;

    /**
     * Creates a BinaryRangeSearcher for the given array of items that matches items using the
     * Matcher matchUsing.
     *
     * First sorts the array in place using the Comparator sortUsing. (Assumes that the given array
     * will not be used externally afterwards.)
     *
     * Requires that sortUsing sorts the array such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * For example:
     * sortUsing lexicographic string sort: [  aaa,  abc,   ba,  bzb, cdef ]
     * matchUsing T is prefixed by U
     * matchUsing.match for prefix "b":     [   -1,   -1,    0,    0,    1 ]
     *
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if sortUsing or matchUsing is null
     */
    public static <T, U> BinaryRangeSearcher<T, U> forUnsortedArray(T[] array,
                                                                    Comparator<T> sortUsing,
                                                                    Matcher<T, U> matchUsing) {
        if (sortUsing == null || matchUsing == null) {
            throw new IllegalArgumentException();
        }
        /*
        Tip: To reduce redundancy, you can let the BinaryRangeSearcher constructor throw some of
        the exceptions mentioned in this method's documentation. The caller doesn't care which
        method exactly causes the exception, as long as it's something that happens while
        executing this method.
        */
        Arrays.sort(array, sortUsing);
        return new BinaryRangeSearcher<>(array, matchUsing);
    }

    /**
     * Requires that array is sorted such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate the array).
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if matcher is null
     */
    protected BinaryRangeSearcher(T[] array, Matcher<T, U> matcher) {
        if (array == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        this.array = array;
        this.matcher = matcher;
    }

    /*
    Binary search code based on
    https://www.geeksforgeeks.org/find-first-and-last-positions-of-an-element-in-a-sorted-array/
     */
    public MatchResult<T> findAllMatches(U target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        int length = this.array.length;
        int first = findFirstMatches(target, 0, length - 1, length);
        int last = findLastMatches(target, 0, length - 1, length);
        if (first != -1 && last != -1) {
            return new MatchResult<>(this.array, first, last + 1);
        } else {
            return new MatchResult<>(this.array);
        }
    }

    private int findFirstMatches(U target, int lo, int hi, int length) {
        if (lo > hi) {
            return -1;
        }
        int mid = lo + (hi - lo) / 2;
        T item = this.array[mid];
        if ((mid == 0 || this.matcher.match(this.array[mid - 1], target) < 0)
            && this.matcher.match(this.array[mid], target) == 0) {
            return mid;
        } else if (this.matcher.match(this.array[mid], target) < 0) {
            return findFirstMatches(target, mid + 1, hi, length);
        } else {
            return findFirstMatches(target, lo, mid - 1, length);
        }

    }

    private int findLastMatches(U target, int lo, int hi, int length) {
        if (lo > hi) {
            return -1;
        }
        int mid = lo + (hi - lo) / 2;
        T item = this.array[mid];
        if ((mid == length - 1 || this.matcher.match(this.array[mid + 1], target) > 0)
            && this.matcher.match(this.array[mid], target) == 0) {
            return mid;
        } else if (this.matcher.match(this.array[mid], target) > 0) {
            return findLastMatches(target, lo, mid - 1, length);
        } else {
            return findLastMatches(target, mid + 1, hi, length);
        }
    }

    public static class MatchResult<T> extends AbstractMatchResult<T> {
        final T[] array;
        final int start;
        final int end;

        /**
         * Use this constructor if there are no matching results.
         * (This lets us use Arrays.copyOfRange to make a new T[], which can be difficult to
         * acquire otherwise due to the way Java handles generics.)
         */
        protected MatchResult(T[] array) {
            this(array, 0, 0);
        }

        protected MatchResult(T[] array, int startInclusive, int endExclusive) {
            this.array = array;
            this.start = startInclusive;
            this.end = endExclusive;
        }

        @Override
        public int count() {
            return this.end - this.start;
        }

        @Override
        public T[] unsorted() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
