/* Mahdeen Ahmed Khan Sameer
    Project 08
    Edge Class: To represent an edge between two vertices in a graph
*/

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private double distance;
    private static int count = 0;
    private int id;

    public Edge(Vertex vertex1, Vertex vertex2, double distance) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.distance = distance;
        this.id = ++count; // Assign a unique ID to each edge
    }

    @Override
    public String toString() {
        return "Edge " + id + " between " + vertex1 + " and " + vertex2 + " with distance " + distance;
    }


    public double distance() {
        return this.distance;
    }

    public Vertex other(Vertex vertex) {
        if (vertex.equals(this.vertex1)) {
            return this.vertex2;
        } else if (vertex.equals(this.vertex2)) {
            return this.vertex1;
        } else {
            return null;
        }
    }

    public Vertex[] vertices() {
        return new Vertex[]{this.vertex1, this.vertex2};
    }

    public Vertex getVertex1() {
        return this.vertex1;
    }

    public Vertex getVertex2() {
        return this.vertex2;
    }
}
