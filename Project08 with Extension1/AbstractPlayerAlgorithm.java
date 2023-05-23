public abstract class AbstractPlayerAlgorithm {
    protected Graph graph;
    protected Vertex currentVertex;

    public AbstractPlayerAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public Vertex getCurrentVertex() {
        return this.currentVertex;
    }

    public void setCurrentVertex(Vertex vertex) {
        this.currentVertex = vertex;
    }

    public abstract Vertex chooseStart();

    public abstract Vertex chooseStart(Vertex other);

    public abstract Vertex chooseNext(Vertex otherPlayer);
}
