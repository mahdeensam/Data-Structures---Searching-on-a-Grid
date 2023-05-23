import javax.swing.JFrame;

public class Driver {

    public Driver(int n, double p) throws InterruptedException {
        // Create a random graph on which to play
        Graph graph = new Graph(n, p);

        // Create the pursuer and evader
        AbstractPlayerAlgorithm pursuer = new MoveTowardsPlayerAlgorithm(graph);
        AbstractPlayerAlgorithm evader = new MoveAwayPlayerAlgorithm(graph);

        // Have each player choose a starting location
        pursuer.chooseStart();
        // Since the evader has a harder objective, they get to play second
        // and see where the pursuer chose
        evader.chooseStart(pursuer.getCurrentVertex());

        // Create a new JFrame
        JFrame frame = new JFrame("Graph Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the display
        GraphDisplay display = new GraphDisplay(graph, pursuer, evader, 40);

        frame.pack();
        frame.setVisible(true);

        // Play the game until the pursuer captures the evader
        while (pursuer.getCurrentVertex() != evader.getCurrentVertex()) {
            Thread.sleep(500);
            pursuer.chooseNext(evader.getCurrentVertex());
            display.repaint();
            if (pursuer.getCurrentVertex() != evader.getCurrentVertex()) {
                Thread.sleep(500);
                evader.chooseNext(pursuer.getCurrentVertex());
                display.repaint();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 10;
        double p = .3;
        new Driver(n, p);
    }
}
