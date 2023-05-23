import java.util.*;

public class MoveAwayPlayerAlgorithm extends AbstractPlayerAlgorithm {
    private Random random;

    public MoveAwayPlayerAlgorithm(Graph graph) {
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
        // Choose the vertex that is farthest from the other player's starting vertex
        HashMap<Vertex, Double> distances = graph.distanceFrom(other);
        double maxDistance = Double.MIN_VALUE;
        Vertex farthestVertex = null;
        for (Vertex vertex : distances.keySet()) {
            if (distances.get(vertex) > maxDistance) {
                maxDistance = distances.get(vertex);
                farthestVertex = vertex;
            }
        }
        this.currentVertex = farthestVertex;
        return this.currentVertex;
    }

    @Override
    public Vertex chooseNext(Vertex otherPlayer) {
        // Check if currentVertex is null
        if(this.currentVertex == null) {
            // Choose a start vertex before choosing the next
            this.chooseStart(otherPlayer);
        }
        
        // Among the vertices in the neighborhood of the current vertex, 
        // choose the one that is farthest from the other player
        HashMap<Vertex, Double> distances = graph.distanceFrom(otherPlayer);
        double maxDistance = Double.MIN_VALUE;
        Vertex farthestVertex = null;
        for (Vertex vertex : currentVertex.adjacentVertices()) {
            if (distances.get(vertex) > maxDistance) {
                maxDistance = distances.get(vertex);
                farthestVertex = vertex;
            }
        }
        if (farthestVertex == null) {
            return currentVertex; // No choice but to stay in place
        }
        this.currentVertex = farthestVertex;
        return this.currentVertex;
    }
}
