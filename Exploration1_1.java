/* Mahdeen Ahmed Khan Sameer
    Project 08
    Exploration1_1 Class: To simulate a pursuit-evasion game on a graph and determine if the evader can evade the pursuer indefinitely or be captured within a limited number of moves.
*/


public class Exploration1_1 {
    public static void main(String[] args) {
        // Create a graph where the evader could evade indefinitely.
        Graph graph = new Graph();
        Vertex vertex1 = graph.addVertex();
        Vertex vertex2 = graph.addVertex();
        Vertex vertex3 = graph.addVertex();
        graph.addEdge(vertex1, vertex2, 1);
        graph.addEdge(vertex2, vertex3, 1);
        graph.addEdge(vertex3, vertex1, 1);

        // Place the evader and pursuer on the graph.
        Vertex evaderCurrentVertex = vertex1; // Starts at vertex 1
        Vertex pursuerCurrentVertex = vertex2; // Starts at vertex 2

        // Create the MoveAwayPlayerAlgorithm
        MoveAwayPlayerAlgorithm strategy = new MoveAwayPlayerAlgorithm(graph);

        // Run until the evader is captured or 1000 moves have passed (to prevent infinite loop)
        int moves = 0;
        while (!evaderCurrentVertex.equals(pursuerCurrentVertex) && moves < 1000) {
            evaderCurrentVertex = strategy.chooseNext(pursuerCurrentVertex);
            pursuerCurrentVertex = strategy.chooseNext(evaderCurrentVertex);

            // Print the current state
            System.out.println("Move " + moves + ":");
            System.out.println("Evader is at: " + evaderCurrentVertex);
            System.out.println("Pursuer is at: " + pursuerCurrentVertex);
            System.out.println();
            printGraph(graph, evaderCurrentVertex, pursuerCurrentVertex);

            moves++;
        }

        // Print the result
        if (evaderCurrentVertex.equals(pursuerCurrentVertex)) {
            System.out.println("Evader was captured in " + moves + " moves!");
        } else {
            System.out.println("Evader has evaded successfully after " + moves + " moves.");
        }
    }

    private static void printGraph(Graph graph, Vertex evader, Vertex pursuer) {
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.equals(evader)) {
                System.out.println("Vertex " + vertex + " (Evader)");
            } else if (vertex.equals(pursuer)) {
                System.out.println("Vertex " + vertex + " (Pursuer)");
            } else {
                System.out.println("Vertex " + vertex);
            }
        }
        for (Edge edge : graph.getEdges()) {
            System.out.println("Edge " + edge);
        }
        System.out.println();
    }
}
