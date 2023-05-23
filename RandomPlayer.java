/* Mahdeen Ahmed Khan Sameer
    Project 08
    RandomPlayer Class: To implement a random movement algorithm for the evader in a pursuit-evasion game on a graph. The algorithm chooses the starting location randomly and selects the next move by randomly picking one of the adjacent vertices or staying in the current vertex.
*/

import java.util.*;

public class RandomPlayer extends AbstractPlayerAlgorithm {
    private Random random;

    public RandomPlayer(Graph graph) {
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
        // Ignore the other player's start point and choose a random start point
        return chooseStart();
    }

    @Override
    public Vertex chooseNext(Vertex otherPlayer) {
        List<Vertex> adjacentVertices = new ArrayList<>();
        this.currentVertex.adjacentVertices().forEach(adjacentVertices::add);
        
        // Add the current vertex to the list to account for the choice of sitting still
        adjacentVertices.add(this.currentVertex);
    
        Vertex nextVertex = adjacentVertices.get(random.nextInt(adjacentVertices.size()));
        System.out.println("Evader moved from " + this.currentVertex + " to " + nextVertex);
        this.currentVertex = nextVertex;
        return this.currentVertex;
    }
    
}
