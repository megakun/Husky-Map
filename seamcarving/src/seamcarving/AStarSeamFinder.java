package seamcarving;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;
import graphpathfinding.WeightedEdge;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Point;

public class AStarSeamFinder extends SeamFinder {
    /*
    Use this method to create your ShortestPathFinder.
    This will be overridden during grading to use our solution path finder, so you don't get
    penalized again for any bugs in code from previous assignments
    */
    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> graph) {
        return new AStarPathFinder<>(graph);
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        double input[][] = transpose(energies);
        return findVerticalSeam(input);
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        PixelGraph graph = new PixelGraph(energies);

        ShortestPathFinder seamFinder = createPathFinder(graph);

        Duration time = Duration.ofSeconds(30);
        ShortestPathResult<Point> path = seamFinder.findShortestPath(graph.start, graph.end, time);
        List result = new ArrayList();

        for (Point p : path.solution()) {
            result.add(p.x);
        }

        result.remove(0);
        result.remove(result.size() - 1);
        return result;


    }

    private double[][] transpose(double[][] energies) {
        int m = energies.length;
        int n = energies[0].length;

        double[][] trasposedMatrix = new double[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                trasposedMatrix[x][y] = energies[y][x];
            }
        }
        return trasposedMatrix;
    }

    private class PixelGraph implements AStarGraph<Point> {
        Point start;
        Point end;
        private Set<Point> pixels;
        private double[][] weights;

        public PixelGraph(double[][] energies) {
            this.pixels = new HashSet<>();
            this.weights = energies;

            for (int i = 0; i < energies.length; i++) {
                for (int j = 0; j < energies[0].length; j++) {
                    pixels.add(new Point(i, j));
                }
            }
            this.start = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            this.end = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
            pixels.add(start);
            pixels.add(end);
        }

        public List<WeightedEdge<Point>> neighbors(Point v) {
            List<WeightedEdge<Point>> neighbors = new ArrayList<>();
            double weight;
            for (Point p : pixels) {
                if (!p.equals(v)) {
                    if (isNeighbor(v, p)) {
                        if (p == end) {
                            weight = 0.0;
                        } else {
                            weight = weights[p.x][p.y];
                        }
                        neighbors.add(new WeightedEdge<>(v, p, weight));
                    }
                }
            }
            return neighbors;
        }

        public double estimatedDistanceToGoal(Point v, Point goal) {
            return 0.0;
        }


        private boolean isNeighbor(Point v, Point p) {
            if (p.y - v.y == 1 && p.y != Integer.MIN_VALUE) {
                if (p.x - v.x == 0 || p.x - v.x == -1 || p.x - v.x == 1) {
                    return true;
                }
            }

            if (v.x == Integer.MAX_VALUE && p.y == 0) {
                return true;
            }

            if (p.x == Integer.MIN_VALUE && v.y == weights[0].length - 1) {
                return true;
            }
            return false;
        }
    }


}
