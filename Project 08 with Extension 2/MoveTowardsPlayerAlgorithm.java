import java.util.*;

public class MoveTowardsPlayerAlgorithm extends AbstractPlayerAlgorithm {
    private Random random;

    public MoveTowardsPlayerAlgorithm(Graph graph) {
        super(graph);
        this.random = new Random();
    }

    @Override
    public Vertex chooseStart() {
        List<Vertex> vertices = new ArrayList<>();
        graph.getVertices().forEach(vertices::add);
        this.currentVertex = vertices.get(random.nextInt(vertices.size()));
        return this.currentVertex;
    }

    @Override
    public Vertex chooseStart(Vertex other) {
        // Choose the vertex that is closest to the other player's starting vertex
        HashMap<Vertex, Double> distances = graph.distanceFrom(other);
        double minDistance = Double.MAX_VALUE;
        Vertex closestVertex = null;
        for (Vertex vertex : distances.keySet()) {
            if (distances.get(vertex) < minDistance) {
                minDistance = distances.get(vertex);
                closestVertex = vertex;
            }
        }
        this.currentVertex = closestVertex;
        return this.currentVertex;
    }

    @Override
    public Vertex chooseNext(Vertex otherPlayer) {
        // Choose the vertex that is closest to the other player's current vertex among the adjacent vertices
        HashMap<Vertex, Double> distances = graph.distanceFrom(otherPlayer);
        double minDistance = Double.MAX_VALUE;
        Vertex closestVertex = null;
        for (Vertex vertex : this.currentVertex.adjacentVertices()) {
            if (distances.get(vertex) < minDistance) {
                minDistance = distances.get(vertex);
                closestVertex = vertex;
            }
        }
        // If no closer vertex is found among the adjacent vertices, stay at the current vertex
        if (closestVertex == null) {
            closestVertex = this.currentVertex;
        }
        this.currentVertex = closestVertex;
        return this.currentVertex;
    }
}
