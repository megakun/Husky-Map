package pointsets;

import java.util.ArrayList;
import java.util.List;


/**
 * Naive nearest-neighbor implementation using a linear scan.
 */
public class NaivePointSet<T extends Point> implements PointSet<T> {
    private ArrayList<T> npSet;
    private List<T> points;


    /**
     * Instantiates a new NaivePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public NaivePointSet(List<T> points) {
        for (T point : points) {
            npSet.add(point);
        }
        this.points = points;
    }
    /**
     * Returns the point in this set closest to the given point in O(N) time, where N is the number
     * of points in this set.
     */
    @Override
    public T nearest(Point target) {
        T result = null;
        double nearest = Double.MAX_VALUE;
        for (T point : npSet) {
            double distance = point.distanceSquaredTo(target);
            if (distance < nearest) {
                nearest = distance;
                result = point;
            }
        }
        return result;
    }

    @Override
    public List<T> allPoints() {
        return points;

    }
}
