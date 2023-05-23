import java.util.*;
import java.util.Random;

public class LookAheadPlayerAlgorithm extends AbstractPlayerAlgorithm {
    private Random random;
    private int lookAheadDepth;

    public LookAheadPlayerAlgorithm(Graph graph, int lookAheadDepth) {
        super(graph);
        this.random = new Random();
        this.lookAheadDepth = lookAheadDepth;
    }

    @Override
    public Vertex chooseStart() {
        Vertex startVertex = getRandomVertex();
        setCurrentVertex(startVertex);
        return startVertex;
    }

    @Override
    public Vertex chooseStart(Vertex other) {
        Vertex startVertex = getFarthestVertex(other);
        setCurrentVertex(startVertex);
        return startVertex;
    }

    @Override
    public Vertex chooseNext(Vertex otherPlayer) {
        Vertex currentVertex = this.getCurrentVertex(); // Use instance variable instead of local variable
        Vertex optimalNext = null;
        double maxMinDistance = Double.MIN_VALUE;
        for (Vertex next : currentVertex.adjacentVertices()) {
            double minDistance = depthLimitedDFS(next, otherPlayer, lookAheadDepth - 1, Double.MAX_VALUE);
            if (minDistance > maxMinDistance) {
                maxMinDistance = minDistance;
                optimalNext = next;
            }
        }
        setCurrentVertex(optimalNext != null ? optimalNext : currentVertex);
        return getCurrentVertex();
    }

    private double depthLimitedDFS(Vertex current, Vertex otherPlayer, int depth, double minDistanceSoFar) {
        double distanceToOtherPlayer = getGraph().distanceFrom(otherPlayer).get(current);
        double minDistance = Math.min(minDistanceSoFar, distanceToOtherPlayer);
        if (depth == 0 || minDistance == 0) {
            return minDistance;
        }
        double maxMinDistance = minDistance;
        for (Vertex next : current.adjacentVertices()) {
            double minDistanceThroughNext = depthLimitedDFS(next, otherPlayer, depth - 1, minDistance);
            maxMinDistance = Math.max(maxMinDistance, minDistanceThroughNext);
        }
        return maxMinDistance;
    }

    private Vertex getRandomVertex() {
        Iterable<Vertex> vertices = getGraph().getVertices();
        int numVertices = getGraph().size();
        int randomIndex = random.nextInt(numVertices);
        int currentIndex = 0;
        for (Vertex vertex : vertices) {
            if (currentIndex == randomIndex) {
                return vertex;
            }
            currentIndex++;
        }
        return null;
    }

    private Vertex getFarthestVertex(Vertex other) {
        HashMap<Vertex, Double> distances = getGraph().distanceFrom(other);
        double maxDistance = Double.MIN_VALUE;
        Vertex farthestVertex = null;
        for (Vertex vertex : distances.keySet()) {
            if (distances.get(vertex) > maxDistance) {
                maxDistance = distances.get(vertex);
                farthestVertex = vertex;
            }
        }
        return farthestVertex;
    }
}
