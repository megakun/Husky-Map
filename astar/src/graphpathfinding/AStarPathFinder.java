package graphpathfinding;

import priorityqueues.ArrayHeapMinPQ;

import java.time.Duration;

import timing.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @see ShortestPathFinder for more method documentation
 */
public class AStarPathFinder<VERTEX> extends ShortestPathFinder<VERTEX> {
    private final AStarGraph<VERTEX> graph;
    private ArrayHeapMinPQ<VERTEX> pq;

    /**
     * Creates a new AStarPathFinder that works on the provided graph.
     */
    public AStarPathFinder(AStarGraph<VERTEX> graph) {
        this.graph = graph;
        pq = new ArrayHeapMinPQ<>();
    }

    @Override
    public ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end, Duration timeout) {
        Timer timer = new Timer(timeout);

        pq.add(start, heuristic(start, end));
        int explore = 0;
        Map<VERTEX, Double> distances = new HashMap<>();
        Map<VERTEX, VERTEX> previousVertex = new HashMap<>();
        distances.put(start, 0.0);

        if (start.equals(end)) {
            return new ShortestPathResult.Solved<>(List.of(start), 0.0, 1, timer.elapsedDuration());
        }

        while (!pq.isEmpty()) {
            VERTEX p = pq.removeMin();
            explore += 1;
            if (p.equals(end)) {
                return new ShortestPathResult.Solved<>(solution(previousVertex, start, end),
                    distances.get(end), explore, timer.elapsedDuration());
            }
            if (timer.isTimeUp()) {
                return new ShortestPathResult.Timeout<>(1, timer.elapsedDuration());
            }

            for (WeightedEdge<VERTEX> edge : graph.neighbors(p)) {
                VERTEX v = edge.to();
                double weight = edge.weight();
                if (distances.containsKey(v)) {
                    if (distances.get(v) > distances.get(p) + weight) {
                        distances.put(v, distances.get(p) + weight);
                        previousVertex.put(v, p);
                        pq.changePriority(v, distances.get(v) + heuristic(v, end));

                    }
                } else {
                    distances.put(v, distances.get(p) + weight);
                    previousVertex.put(v, p);
                    pq.add(v, distances.get(v) + heuristic(v, end));
                }

            }
        }


        return new ShortestPathResult.Unsolvable<>(1, timer.elapsedDuration());
    }

    private double heuristic(VERTEX v, VERTEX goal) {
        return graph.estimatedDistanceToGoal(v, goal);
    }

    private List<VERTEX> solution(Map<VERTEX, VERTEX> previousVertex, VERTEX start, VERTEX end) {
        List<VERTEX> result = new ArrayList<>();
        result.add(end);
        VERTEX v = previousVertex.get(end);
        while (!v.equals(start)) {
            result.add(v);
            v = previousVertex.get(v);
        }
        result.add(v);
        return reverse(result);
    }

    private List<VERTEX> reverse(List<VERTEX> list) {
        List<VERTEX> result = new LinkedList<>();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            result.add(list.remove(list.size() - 1));
        }
        return result;
    }

    @Override
    protected AStarGraph<VERTEX> graph() {
        return this.graph;
    }
}
