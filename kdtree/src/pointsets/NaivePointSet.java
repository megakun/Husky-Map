package pointsets;

import java.util.HashSet;
import java.util.List;


/**
 * Naive nearest-neighbor implementation using a linear scan.
 */
public class NaivePointSet<T extends Point> implements PointSet<T> {
    private HashSet<Point> npSet;
    private List<T> points;


    /**
     * Instantiates a new NaivePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public NaivePointSet(List<T> points) {
        npSet = new HashSet<>();
        for (Point p : points) {
            npSet.add(p);
        }
        this.points = points;
    }
    /**
     * Returns the point in this set closest to the given point in O(N) time, where N is the number
     * of points in this set.
     */
    @Override
    public T nearest(Point target) {
        Point result = new Point(0, 0);
        double nearest = Double.MAX_VALUE;
        for (Point p : npSet) {
            double distance = p.distanceSquaredTo(target);
            if (distance < nearest) {
                nearest = distance;
                result = p;
            }
        }
        return (T) result;
    }

    @Override
    public List<T> allPoints() {
        return points;

    }
}
