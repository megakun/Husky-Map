package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import pointsets.KDTreePointSet;
import pointsets.Point;
import pointsets.PointSet;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static huskymaps.utils.Spatial.projectToPoint;

/**
 * @see Router
 */
public class DefaultRouter extends Router {
    private StreetMapGraph graph;
    private PointSet pointSet;
    private ShortestPathFinder finder;

    public DefaultRouter(StreetMapGraph graph) {
        this.graph = graph;
        List<NodePoint> nodePoints = new LinkedList<>();

        for (Node vertex : graph.allNodes()) {
            if (!graph.neighbors(vertex).isEmpty()) {
                nodePoints.add(createNodePoint(vertex));
            }
        }

        pointSet = createPointSet(nodePoints);
        finder = createPathFinder(graph);
    }

    @Override
    protected <T extends Point> PointSet<T> createPointSet(List<T> points) {
        // uncomment (and import) if you want to use WeirdPointSet instead of your own KDTreePointSet:
        // return new WeirdPointSet<>(points);
        return KDTreePointSet.createAfterShuffling(points);
    }

    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g) {
        return new AStarPathFinder<>(g);
    }

    @Override
    protected NodePoint createNodePoint(Node node) {
        return projectToPoint(Coordinate.fromNode(node), (x, y) -> new NodePoint(x, y, node));
    }

    @Override
    protected Node closest(Coordinate c) {
        // Project to x and y coordinates instead of using raw lat and lon for finding closest points:
        Point p = projectToPoint(c, Point::new);
        NodePoint result = (NodePoint) pointSet.nearest(p);
        return result.node();
    }

    @Override
    public List<Node> shortestPath(Coordinate start, Coordinate end) {
        Node src = closest(start);
        Node dest = closest(end);
        Duration time = Duration.ofSeconds(30);
        ShortestPathResult<Node> path = finder.findShortestPath(src, dest, time);
        /*
        Feel free to use any arbitrary duration for your path finding timeout; we don't expect
        queries to take more than a few seconds, so e.g. 10-30 seconds is pretty reasonable.
         */

        return path.solution();
    }

    @Override
    public List<NavigationDirection> routeDirections(List<Node> route) {
        // Optional
        return null;
    }
}
