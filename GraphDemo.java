/* Mahdeen Ahmed Khan Sameer
    Project 08
    GraphDemo Class: To serve as the main class for running a pursuit-evasion game on a graph. It initializes the graph, pursuer, and evader, and manages the game by having the players choose their starting locations and take turns making moves until the pursuer captures the evader. 
*/

import java.util.*;

public class GraphDemo {
    private Graph graph;
    private AbstractPlayerAlgorithm pursuer;
    private AbstractPlayerAlgorithm evader;

    public GraphDemo(Graph graph, AbstractPlayerAlgorithm pursuer, AbstractPlayerAlgorithm evader) {
        this.graph = graph;
        this.pursuer = pursuer;
        this.evader = evader;
    }

    public void startGame() throws InterruptedException {
        // Have each player choose a starting location
        pursuer.chooseStart();
        // Since the evader has a harder objective, they get to play second
        // and see where the pursuer chose
        evader.chooseStart(pursuer.getCurrentVertex());

        // Make the display
        GraphDisplay display = new GraphDisplay(graph, pursuer, evader, 40);
        display.repaint();

        // Play the game until the pursuer captures the evader
        while (pursuer.getCurrentVertex() != evader.getCurrentVertex()){
            Thread.sleep(500);
            pursuer.chooseNext(evader.getCurrentVertex());
            display.repaint();
            if (pursuer.getCurrentVertex() != evader.getCurrentVertex()){
                Thread.sleep(500);
                evader.chooseNext(pursuer.getCurrentVertex());
                display.repaint();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        // Create a random graph on which to play
        int n = 10;
        double p = .3;
        Graph graph = new Graph(n, p);

        // Create the pursuer and evader
        AbstractPlayerAlgorithm pursuer = new MoveTowardsPlayerAlgorithm(graph);
        AbstractPlayerAlgorithm evader = new MoveAwayPlayerAlgorithm(graph);

        GraphDemo game = new GraphDemo(graph, pursuer, evader);
        game.startGame();
    }
}
