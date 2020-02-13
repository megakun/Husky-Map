package pointsets;

import java.util.Collections;
import java.util.List;

/**
 * Fast nearest-neighbor implementation using a k-d tree.
 */
public class KDTreePointSet<T extends Point> implements PointSet<T> {
    private Node<Point> overallRoot;
    private List<T> points;


    /**
     * Instantiates a new KDTreePointSet with a shuffled version of the given points.
     *
     * Randomizing the point order decreases likeliness of ending up with a spindly tree if the
     * points are sorted somehow.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public static <T extends Point> KDTreePointSet<T> createAfterShuffling(List<T> points) {
        Collections.shuffle(points);
        return new KDTreePointSet<T>(points);
    }

    /**
     * Instantiates a new KDTreePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    KDTreePointSet(List<T> points) {
        this.overallRoot = null;
        this.points = points;
        //this.best = new Point(0.0, 0.0);
        //this.bestDistance = Double.MAX_VALUE;
        for (Point p : points) {
            this.overallRoot = insert(p, overallRoot);
        }
    }

    private Node<Point> insert(Point p, Node<Point> root) {
        return insertHelper(root, p, true);
    }

    private Node<Point> insertHelper(Node<Point> root, Point p, boolean horizontal) {
        if (root == null) {
            root = new Node<>(p, horizontal);
        } else if (horizontal) {
            if (p.x() < root.p.x()) {
                root.left = insertHelper(root.left, p, false);
            } else {
                root.right = insertHelper(root.right, p, false);
            }
        } else {
            if (p.y() < root.p.y()) {
                root.left = insertHelper(root.left, p, true);
            } else {
                root.right = insertHelper(root.right, p, true);
            }
        }
        return root;


    }

    /**
     * Returns the point in this set closest to the given point in (usually) O(log N) time, where
     * N is the number of points in this set.
     */
    @Override
    public T nearest(Point target) {
        Point best = nearestHelper(overallRoot, target, overallRoot.p);
        return (T) best;

    }

    private Point nearestHelper(Node<Point> root, Point target, Point best) {
        if (root == null) {
            return best;
        }
        if (root.p.equals(target)) {
            return root.p;
        }

        double d = root.p.distanceSquaredTo(target);
        if (d <= best.distanceSquaredTo(target)) {
            best = root.p;
        }
        Node<Point> goodSide;
        Node<Point> badSide;

        if (root.horizontal) {
            if (target.x() < root.p.x()) {
                goodSide = root.left;
                badSide = root.right;
            } else {
                goodSide = root.right;
                badSide = root.left;
            }
            best = nearestHelper(goodSide, target, best);
            if (Math.pow((target.x() - root.p.x()), 2) <= best.distanceSquaredTo(target)) {
                best = nearestHelper(badSide, target, best);
            }
        } else { // vertical
            if (target.y() < root.p.y()) {
                goodSide = root.left;
                badSide = root.right;
            } else {
                goodSide = root.right;
                badSide = root.left;
            }
            best = nearestHelper(goodSide, target, best);
            if (Math.pow((target.y() - root.p.y()), 2) <= best.distanceSquaredTo(target)) {
                best = nearestHelper(badSide, target, best);
            }

        }

        return best;
    }

    @Override
    public List<T> allPoints() {
        return points;
    }

    private static final class Node<Point> {
        private Point p;
        private Node<Point> left;
        private Node<Point> right;
        private boolean horizontal;


        private Node(Point p, boolean horizontal) {
            this.p = p;
            this.left = null;
            this.right = null;
            this.horizontal = horizontal;
        }

        private Node(Point p, Node<Point> left, Node<Point> right, boolean horizontal) {
            this.p = p;
            this.left = left;
            this.right = right;
            this.horizontal = horizontal;
        }


    }
}
