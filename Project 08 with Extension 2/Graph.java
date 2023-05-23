import java.util.*;

public class Graph {
    private List<Vertex> vertices;
    private List<Edge> edges;
    private Random random = new Random();

    public Graph() {
        this(0, 0.0);
    }

    public Graph(int n) {
        this(n, 0.0);
    }

    public Graph(int n, double probability) {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            addVertex();
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < probability) {
                    Vertex v1 = vertices.get(i);
                    Vertex v2 = vertices.get(j);
                    addEdge(v1, v2, 1.0);
                }
            }
        }
    }

    public int size() {
        return vertices.size();
    }

    public Iterable<Vertex> getVertices() {
        return vertices;
    }

    public Iterable<Edge> getEdges() {
        return edges;
    }

    public Vertex addVertex() {
        int x = random.nextInt(100); // Generate a random x-coordinate
        int y = random.nextInt(100); // Generate a random y-coordinate
        Vertex v = new Vertex(x, y);
        vertices.add(v);
        return v;
    }
    

    public Edge addEdge(Vertex u, Vertex v, double distance) {
        Edge e = new Edge(u, v, distance);
        edges.add(e);
        u.addEdge(e);
        v.addEdge(e);
        return e;
    }

    public Edge getEdge(Vertex u, Vertex v) {
        for (Edge edge : edges) {
            if ((edge.getVertex1().equals(u) && edge.getVertex2().equals(v)) ||
                (edge.getVertex1().equals(v) && edge.getVertex2().equals(u))) {
                return edge;
            }
        }
        return null;
    }

    public boolean remove(Vertex vertex) {
        if (!vertices.contains(vertex)) {
            return false;
        }
        List<Edge> incidentEdges = new ArrayList<>();
        for (Edge edge : vertex.incidentEdges()) {
            incidentEdges.add(edge);
        }
        for (Edge edge : incidentEdges) {
            remove(edge);
        }
        vertices.remove(vertex);
        return true;
    }
    

    public boolean remove(Edge edge) {
        if (!edges.contains(edge)) {
            return false;
        }
        edge.getVertex1().removeEdge(edge);
        edge.getVertex2().removeEdge(edge);
        edges.remove(edge);
        return true;
    }

    public HashMap<Vertex, Double> distanceFrom(Vertex source) {
        PriorityQueue<VertexDistancePair> queue = new PriorityQueue<>();
        HashMap<Vertex, Double> distance = new HashMap<>();
        HashMap<Vertex, Vertex> previous = new HashMap<>();

        for (Vertex vertex : vertices) {
            if (vertex.equals(source)) {
                distance.put(vertex, 0.0);
            } else {
                distance.put(vertex, Double.MAX_VALUE);
            }
            queue.add(new VertexDistancePair(vertex, distance.get(vertex)));
        }

        while (!queue.isEmpty()) {
            Vertex u = queue.poll().getVertex();

            for (Vertex v : u.adjacentVertices()) {
                double alt = distance.get(u) + u.getEdgeTo(v).distance();
                if (alt < distance.get(v)) {
                    distance.put(v, alt);
                    previous.put(v, u);
                    queue.remove(new VertexDistancePair(v, distance.get(v)));
                    queue.add(new VertexDistancePair(v, alt));
                }
            }
        }

        return distance;
    }

    private class VertexDistancePair implements Comparable<VertexDistancePair> {
        private Vertex vertex;
        private Double distance;

        public VertexDistancePair(Vertex vertex, Double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public Vertex getVertex() {
            return vertex;
        }

        public Double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(VertexDistancePair other) {
            return this.distance.compareTo(other.getDistance());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            VertexDistancePair that = (VertexDistancePair) obj;
            return Objects.equals(vertex, that.vertex);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertex);
        }
    }
}

