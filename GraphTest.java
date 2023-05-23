import java.util.*;

public class GraphTest {
    public static void main(String[] args) {
        // Create a graph with 5 vertices
        Graph graph = new Graph(5, 0.5);

        // Check the size of the graph
        System.out.println("Graph size: " + graph.size()); // Should be 5

        // Add a vertex to the graph
        Vertex v = graph.addVertex();
        System.out.println("Graph size after adding a vertex: " + graph.size()); // Should be 6

        // Get a reference to another vertex
        Iterator<Vertex> vertexIterator = graph.getVertices().iterator();
        Vertex u = vertexIterator.next();

        // Add an edge to the graph
        Edge e = graph.addEdge(u, v, 1.0);

        // Check the distance of the edge
        System.out.println("Edge distance: " + e.distance()); // Should be 1.0

        // Check the vertices of the edge
        System.out.println("Edge vertices: " + Arrays.toString(e.vertices())); // Should contain u and v

        // Check the edge between u and v
        System.out.println("Edge between u and v: " + graph.getEdge(u, v)); // Should be e

        // Check the adjacency of u
        System.out.println("Vertices adjacent to u: ");
        for (Vertex vertex : u.adjacentVertices()) {
            System.out.println(vertex);
        }

        // Check the incident edges of u
        System.out.println("Edges incident to u: ");
        for (Edge edge : u.incidentEdges()) {
            System.out.println(edge);
        }

        // Remove v from the graph
        System.out.println("Remove v from graph: " + graph.remove(v)); // Should be true

        // Check the size of the graph
        System.out.println("Graph size after removing a vertex: " + graph.size()); // Should be 5

        // Remove e from the graph
        System.out.println("Remove e from graph: " + graph.remove(e)); // Should be false

        // Check the edge between u and v
        System.out.println("Edge between u and v: " + graph.getEdge(u, v)); // Should be null

        // Compute the minimal distances from u
        System.out.println("Distances from u: " + graph.distanceFrom(u)); // Should show distances
    }
}
