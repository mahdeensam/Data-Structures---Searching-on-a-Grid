import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Vertex {
    private List<Edge> edges;
    private static int count = 0;
    private int id;
    private int x; // x-coordinate
    private int y; // y-coordinate

    @Override
    public String toString() {
        return "Vertex " + id;
    }

    // Modify the constructor to accept the x and y coordinates
    public Vertex(int x, int y) {
        this.edges = new ArrayList<>();
        this.id = ++count; // Assign a unique ID to each vertex
        this.x = x;
        this.y = y;
    }

    public Edge getEdgeTo(Vertex vertex) {
        for (Edge edge : edges) {
            if (edge.getVertex1().equals(vertex) || edge.getVertex2().equals(vertex)) {
                return edge;
            }
        }
        return null;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public boolean removeEdge(Edge edge) {
        return this.edges.remove(edge);
    }

    public Iterable<Vertex> adjacentVertices() {
        List<Vertex> adjacentVertices = new ArrayList<>();
        for (Edge edge : edges) {
            Vertex vertex1 = edge.getVertex1();
            Vertex vertex2 = edge.getVertex2();
            if (vertex1.equals(this)) {
                adjacentVertices.add(vertex2);
            } else {
                adjacentVertices.add(vertex1);
            }
        }
        return adjacentVertices;
    }

    public Iterable<Edge> incidentEdges() {
        return edges;
    }

    // Getter for the x-coordinate
    public int getX() {
        return this.x;
    }

    // Getter for the y-coordinate
    public int getY() {
        return this.y;
    }
    public Vertex(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
