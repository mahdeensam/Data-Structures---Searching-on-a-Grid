/* Mahdeen Ahmed Khan Sameer
    Project 08
    Exploration1_2 Class: To represent a pursuit-evasion game scenario on a graph, where a pursuer and an evader navigate through the graph to capture or evade each other.
*/

import java.util.*;

public class Exploration1_2 {
    public static void main(String[] args) {
        Graph graph = new Graph(3);

        // Get the three vertices
        Vertex vertex1 = null, vertex2 = null, vertex3 = null;
        int index = 1;
        for (Vertex vertex : graph.getVertices()) {
            if (index == 1)
                vertex1 = vertex;
            else if (index == 2)
                vertex2 = vertex;
            else
                vertex3 = vertex;

            index++;
        }

        // Connect the vertices in a straight line 1-2-3
        graph.addEdge(vertex1, vertex2, 1.0);
        graph.addEdge(vertex2, vertex3, 1.0);

        // Set evader's algorithm to MoveAwayPlayerAlgorithm
        MoveAwayPlayerAlgorithm evaderAlgorithm = new MoveAwayPlayerAlgorithm(graph);
        evaderAlgorithm.chooseStart(vertex1); // Start at vertex 1

        // Set pursuer's algorithm to MoveTowardsPlayerAlgorithm
        MoveTowardsPlayerAlgorithm pursuerAlgorithm = new MoveTowardsPlayerAlgorithm(graph);
        pursuerAlgorithm.chooseStart(vertex2); // Start at vertex 2

        // Print initial positions
        System.out.println("Initial positions:");
        System.out.println("Evader: " + evaderAlgorithm.getCurrentVertex());
        System.out.println("Pursuer: " + pursuerAlgorithm.getCurrentVertex());

        // Simulate the game
        int round = 1;
        while (!evaderAlgorithm.getCurrentVertex().equals(pursuerAlgorithm.getCurrentVertex())) {
            System.out.println("\nRound " + round + ":");

            // Evader moves
            Vertex evaderNext = evaderAlgorithm.chooseNext(pursuerAlgorithm.getCurrentVertex());
            System.out.println("Evader moves to: " + evaderNext);

            // Pursuer moves
            Vertex pursuerNext = pursuerAlgorithm.chooseNext(evaderAlgorithm.getCurrentVertex());
            System.out.println("Pursuer moves to: " + pursuerNext);

            round++;
        }

        System.out.println("\nEvader captured at round " + round + " at vertex " + evaderAlgorithm.getCurrentVertex());
    }
}

