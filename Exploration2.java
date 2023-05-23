/* Mahdeen Ahmed Khan Sameer
    Project 08
    Exploration2 Class: To simulate a pursuit-evasion game on a randomly generated graph, where a pursuer and an evader move through the graph attempting to capture or evade each other, while displaying their movements and distances between them in the output.
*/

import java.util.*;

public class Exploration2 {

    public Exploration2(int n, double p) throws InterruptedException {
        // Create a specific graph on which to play
        Graph graph = new Graph();

        // Add vertices
        Vertex vertex1 = graph.addVertex();
        Vertex vertex2 = graph.addVertex();
        Vertex vertex3 = graph.addVertex();
        Vertex vertex4 = graph.addVertex();

        // Add edges
        graph.addEdge(vertex1, vertex2, 1);
        graph.addEdge(vertex2, vertex3, 1);
        graph.addEdge(vertex3, vertex4, 1);

        // Create the pursuer and evader
        AbstractPlayerAlgorithm pursuer = new MoveTowardsPlayerAlgorithm(graph);
        AbstractPlayerAlgorithm evader = new MoveTowardsPlayerAlgorithm(graph);

        // Have each player choose a starting location
        pursuer.chooseStart();
        evader.chooseStart();

        // Make the display
        GraphDisplay display = new GraphDisplay(graph, pursuer, evader, 40);
        display.repaint();

        // Track the movement in the output
        System.out.println("Pursuer: " + pursuer.getCurrentVertex());
        System.out.println("Evader: " + evader.getCurrentVertex());

        // Play the game until the pursuer catches the evader
        while (!pursuer.getCurrentVertex().equals(evader.getCurrentVertex())) {
            Thread.sleep(300);
            pursuer.chooseNext(evader.getCurrentVertex());
            display.repaint();

            // Track the movement in the output
            System.out.println("Pursuer: " + pursuer.getCurrentVertex());

            if (!pursuer.getCurrentVertex().equals(evader.getCurrentVertex())) {
                Thread.sleep(300);
                evader.chooseNext(pursuer.getCurrentVertex());
                display.repaint();

                // Track the movement in the output
                System.out.println("Evader: " + evader.getCurrentVertex());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 4;
        double p = 1.0;
        new Exploration2(n, p);
    }
}
