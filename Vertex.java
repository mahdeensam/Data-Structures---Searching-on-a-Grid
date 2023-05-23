/* Mahdeen Ahmed Khan Sameer
    Project 08
    Vertex Class: To represent a vertex in a graph by storing its unique ID and maintaining a list of edges incident to the vertex. It provides methods to add and remove edges, retrieve adjacent vertices, and retrieve incident edges of the vertex.
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Vertex {
    private List<Edge> edges;
    private static int count = 0;
    private int id;


    @Override
    public String toString() {
        return "Vertex " + id;
    }

    public int getId() {
        return this.id;
    }

    
    public Vertex() {
        this.edges = new ArrayList<>();
        this.id = ++count; // Assign a unique ID to each vertex
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
}
