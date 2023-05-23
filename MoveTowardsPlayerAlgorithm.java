/* Mahdeen Ahmed Khan Sameer
    Project 08
    MoveTowardsPlayerAlgorithm Class: to implement an algorithm for the evader in a pursuit-evasion game on a graph. The algorithm chooses the starting location and determines the next move for the evader based on moving away from the pursuer and maximizing the distance between them.
*/

import java.util.*;

public class MoveTowardsPlayerAlgorithm extends AbstractPlayerAlgorithm {
    private Random random;

    public MoveTowardsPlayerAlgorithm(Graph graph) {
        super(graph);
        this.random = new Random();
    }

    public MoveTowardsPlayerAlgorithm(Graph graph, Vertex initialVertex) {
        super(graph);
        this.currentVertex = initialVertex;
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
        // Choose the vertex that is farthest from the other player's current vertex among the adjacent vertices
        HashMap<Vertex, Double> distances = graph.distanceFrom(otherPlayer);
        double maxDistance = Double.MIN_VALUE;
        Vertex farthestVertex = null;
        for (Vertex vertex : this.currentVertex.adjacentVertices()) {
            if (distances.get(vertex) > maxDistance) {
                maxDistance = distances.get(vertex);
                farthestVertex = vertex;
            }
        }
        // If no farther vertex is found among the adjacent vertices, stay at the current vertex
        if (farthestVertex == null) {
            farthestVertex = this.currentVertex;
        }
        System.out.println("Evader moved from " + this.currentVertex + " to " + farthestVertex);
        this.currentVertex = farthestVertex;
        return this.currentVertex;
    }
    
    
}