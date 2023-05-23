import javax.swing.JFrame;

public class LookAheadPlayerAlgorithmTest {

    public static void main(String[] args) {
        int n = 10;
        double p = 0.3;
        int scale = 40;

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
        GraphDisplay display = new GraphDisplay(graph, pursuer, evader, scale);


        // Add the GraphDisplay to the frame
        frame.add(display.getCanvas());


        frame.pack();
        frame.setVisible(true);

        // Play the game until the pursuer captures the evader
        while (pursuer.getCurrentVertex() != evader.getCurrentVertex()) {
            try {
                Thread.sleep(500);
                pursuer.chooseNext(evader.getCurrentVertex());
                display.repaint();
                if (pursuer.getCurrentVertex() != evader.getCurrentVertex()) {
                    Thread.sleep(500);
                    evader.chooseNext(pursuer.getCurrentVertex());
                    display.repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
