package pointsets;

import java.util.Collections;
import java.util.List;

/**
 * Fast nearest-neighbor implementation using a k-d tree.
 */
public class KDTreePointSet<T extends Point> implements PointSet<T> {
    private Node<Point> overallRoot;
    private List<T> points;
    private Point best;
    private Double bestDistance;

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
        for (Point p : points) {
            overallRoot = insert(p, overallRoot);
        }
        this.points = points;
        best = overallRoot.p;
        bestDistance = Double.MAX_VALUE;

    }

    private Node<Point> insert(Point p, Node<Point> root) {
        return insertHelper(root, p, true);
    }

    private Node<Point> insertHelper(Node<Point> root, Point p, boolean horizontal) {
        if (root == null) {
            root = new Node<Point>(p);
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
        nearestHelper(this.overallRoot, target, true);
        return (T) best;

    }

    private void nearestHelper(Node<Point> root, Point target, boolean horizontal) {
        if (root == null) {
            return;
        }
        Double d = root.p.distanceSquaredTo(target);
        if (d < bestDistance) {
            bestDistance = d;
            best = root.p;
        }
        Node<Point> goodSide;
        Node<Point> badSide;
        if (horizontal) {
            if (target.x() < root.p.x()) {
                goodSide = root.left;
                badSide = root.right;
                //nearestHelper(goodSide, target, false);
            } else {
                goodSide = root.right;
                badSide = root.left;
                //nearestHelper(root.right, target, false);
            }
            nearestHelper(goodSide, target, false);
            nearestHelper(badSide, target, false);
        } else {
            if (target.y() < root.p.y()) {
                goodSide = root.left;
                badSide = root.right;
                //nearestHelper(root.left, target, true);
            } else {
                goodSide = root.right;
                badSide = root.left;
                //nearestHelper(root.right, target, true);
            }
            nearestHelper(goodSide, target, true);
            nearestHelper(badSide, target, false);
        }


    }

    @Override
    public List<T> allPoints() {
        return points;
    }

    private static final class Node<Point> {
        private Point p;
        private Node<Point> left;
        private Node<Point> right;


        private Node(Point p) {
            this.p = p;
            left = null;
            right = null;
        }

        private Node(Point p, Node<Point> left, Node<Point> right) {
            this.p = p;
            this.left = left;
            this.right = right;

        }


    }
}
