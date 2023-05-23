public class TestMoveAwayPlayerAlgorithm {
    public static void main(String[] args) {
        Graph graph = new Graph(10, 0.5);  // Create a graph with 10 vertices and edge probability 0.5

        MoveAwayPlayerAlgorithm moveAwayPlayer = new MoveAwayPlayerAlgorithm(graph);
        RandomPlayer randomPlayer = new RandomPlayer(graph);

        Vertex randomPlayerStart = randomPlayer.chooseStart();
        Vertex moveAwayPlayerStart = moveAwayPlayer.chooseStart(randomPlayerStart);

        System.out.println("Random player starts at: " + randomPlayerStart);
        System.out.println("MoveAway player starts at: " + moveAwayPlayerStart);

        for (int i = 0; i < 10; i++) {  // Simulate 10 steps of the game
            Vertex randomPlayerNext = randomPlayer.chooseNext(moveAwayPlayer.getCurrentVertex());
            Vertex moveAwayPlayerNext = moveAwayPlayer.chooseNext(randomPlayerNext);

            System.out.println("Step " + (i+1));
            System.out.println("Random player moves to: " + randomPlayerNext);
            System.out.println("MoveAway player moves to: " + moveAwayPlayerNext);

            double distance = graph.distanceFrom(randomPlayerNext).get(moveAwayPlayerNext);
            System.out.println("Distance between players: " + distance);
        }
    }
}
