package seamcarving;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;
import graphpathfinding.WeightedEdge;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
        PixelGraph graph = new PixelGraph(energies, false);

        ShortestPathFinder seamFinder = createPathFinder(graph);

        Duration time = Duration.ofSeconds(30);
        ShortestPathResult<Point> path = seamFinder.findShortestPath(graph.start, graph.end, time);
        List result = new ArrayList();

        for (Point p : path.solution()) {
            result.add(p.y);
        }

        result.remove(0);
        result.remove(result.size() - 1);
        return result;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        PixelGraph graph = new PixelGraph(energies, true);

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

    private class PixelGraph implements AStarGraph<Point> {
        Point start;
        Point end;
        private double[][] weights;
        private boolean vertical;

        public PixelGraph(double[][] energies, boolean vertical) {
            this.weights = energies;
            this.vertical = vertical;
            this.start = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            this.end = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        }

        public List<WeightedEdge<Point>> neighbors(Point v) {
            List<WeightedEdge<Point>> neighbors = new ArrayList<>();

            if (vertical) {
                if (v.getX() == Integer.MAX_VALUE) {
                    for (int i = 0; i < weights.length; i++) {
                        neighbors.add(new WeightedEdge<>(v, new Point(i, 0), weights[i][0]));
                    }
                } else if (v.getY() == weights[0].length - 1) {
                    neighbors.add(new WeightedEdge<>(v, end, 0));
                } else {
                    int x = (int) v.getX();
                    int y = (int) v.getY();
                    neighbors.add(new WeightedEdge<>(v, new Point(x, y + 1), weights[x][y + 1]));
                    if (x > 0) {
                        neighbors.add(new WeightedEdge<>(v, new Point(x - 1, y + 1), weights[x - 1][y + 1]));
                    }
                    if (x < weights.length - 1) {
                        neighbors.add(new WeightedEdge<>(v, new Point(x + 1, y + 1), weights[x + 1][y + 1]));
                    }
                }

            } else {
                if (v.getX() == Integer.MAX_VALUE) {
                    for (int i = 0; i < weights[0].length; i++) {
                        neighbors.add(new WeightedEdge<>(v, new Point(0, i), weights[0][i]));
                    }
                } else if (v.getX() == weights.length - 1) {
                    neighbors.add(new WeightedEdge<>(v, end, 0));
                } else {
                    int x = (int) v.getX();
                    int y = (int) v.getY();
                    neighbors.add(new WeightedEdge<>(v, new Point(x + 1, y), weights[x + 1][y]));
                    if (y > 0) {
                        neighbors.add(new WeightedEdge<>(v, new Point(x + 1, y - 1), weights[x + 1][y - 1]));
                    }
                    if (y < weights[0].length - 1) {
                        neighbors.add(new WeightedEdge<>(v, new Point(x + 1, y + 1), weights[x + 1][y + 1]));
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
