import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AdjacencyListGraph implements Graph {
    
    private final int V;
    private int E;
    private Bag<Edge>[] adj;
    private static final String NEWLINE = System.getProperty("line.separator");

    // Static nested class to represent a weighted directed edge
    public static class Edge {
        public final int to;
        public final double weight;

        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    @SuppressWarnings("unchecked")
    public AdjacencyListGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

    /**
     * Adds the directed edge v -> w with a specific weight.
     */
    @Override
    public void addEdge(int v, int w, double weight) {
        validateVertex(v);
        validateVertex(w);
        // Only add to v's list because it is directed (v -> w)
        adj[v].add(new Edge(w, weight));
        E++;
    }

    @Override
    public int V() { return V; }

    @Override
    public int E() { return E; }

    /**
     * Returns an Iterable of neighbor vertex IDs.
     */
    @Override
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the full Edge objects (destination and weight).
     * Useful for algorithms like Dijkstra's or Bellman-Ford.
     */
    public Iterable<Edge> adjEdges(int v) {
        validateVertex(v);
        return adj[v];
    }

    @Override
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size(); // In a directed graph, this is the "out-degree"
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V).append(" vertices, ").append(E).append(" edges ").append(NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (Edge e : adj[v]) {
                s.append(v).append("->").append(e.to)
                 .append(" (").append(e.weight).append(")  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}