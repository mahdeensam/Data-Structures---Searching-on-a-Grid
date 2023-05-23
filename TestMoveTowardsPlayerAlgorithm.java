public class TestMoveTowardsPlayerAlgorithm {
    public static void main(String[] args) {
        Graph graph = new Graph(10, 0.5);  // Create a graph with 10 vertices and edge probability 0.5

        MoveTowardsPlayerAlgorithm moveTowardsPlayer = new MoveTowardsPlayerAlgorithm(graph);
        RandomPlayer randomPlayer = new RandomPlayer(graph);

        Vertex randomPlayerStart = randomPlayer.chooseStart();
        Vertex moveTowardsPlayerStart = moveTowardsPlayer.chooseStart(randomPlayerStart);

        System.out.println("Random player starts at: " + randomPlayerStart);
        System.out.println("MoveTowards player starts at: " + moveTowardsPlayerStart);

        for (int i = 0; i < 10; i++) {  // Simulate 10 steps of the game
            Vertex randomPlayerNext = randomPlayer.chooseNext(moveTowardsPlayer.getCurrentVertex());
            Vertex moveTowardsPlayerNext = moveTowardsPlayer.chooseNext(randomPlayerNext);

            System.out.println("Step " + (i+1));
            System.out.println("Random player moves to: " + randomPlayerNext);
            System.out.println("MoveTowards player moves to: " + moveTowardsPlayerNext);

            double distance = graph.distanceFrom(randomPlayerNext).get(moveTowardsPlayerNext);
            System.out.println("Distance between players: " + distance);
        }
    }
}
