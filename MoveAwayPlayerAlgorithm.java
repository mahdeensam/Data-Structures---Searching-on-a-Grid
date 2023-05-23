/* Mahdeen Ahmed Khan Sameer
    Project 08
    MoveAwayPlayerAlgorithm Class: To implement an algorithm for the evader in a pursuit-evasion game on a graph. The algorithm chooses the starting location and determines the next move for the evader based on moving away from the pursuer and maximizing the distance between them.
*/

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
        // Always start at Vertex 4
        int index = 0;
        for (Vertex v : graph.getVertices()) {
            if (index == 3) {
                this.currentVertex = v;
                break;
            }
            index++;
        }
        return this.currentVertex;
    }
    

    @Override
    public Vertex chooseNext(Vertex otherPlayer) {
        // Check if currentVertex is null
        if (this.currentVertex == null) {
            // Choose a start vertex before choosing the next
            this.chooseStart(otherPlayer);
        }

        // Among the vertices in the neighborhood of the current vertex, 
        // choose the one that maximizes the distance from the other player
        HashMap<Vertex, Double> distances = graph.distanceFrom(otherPlayer);
        List<Vertex> neighbors = new ArrayList<>();
        double maxDistance = Double.MIN_VALUE;
        Vertex farthestVertex = null;
        for (Vertex vertex : currentVertex.adjacentVertices()) {
            double distance = distances.getOrDefault(vertex, Double.MAX_VALUE);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestVertex = vertex;
            }
        }
        
        if (farthestVertex == null) {
            // No choice but to stay in place
            return currentVertex;
        }

        this.currentVertex = farthestVertex;
        return this.currentVertex;
    }
}
