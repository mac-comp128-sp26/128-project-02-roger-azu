import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestDijkstraPathFinder {
    
    // --- 1. Basic test: For a graph in a single line. Change in weights between two vertices
    @Test
    public void testLineGraph() {
        int V = 3;
        int source = 0;
        int target = 2;
        AdjacencyListGraph graph = new AdjacencyListGraph(V);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 6.5);
        graph.addEdge(1, 0, 3); // changing the weight from 1 -> 0

        DijkstraPathFinder pathFinder = new DijkstraPathFinder(graph, V);
        assertEquals("Distance is: " + 11.5  + " path is: " + "0 -> 1 -> 2", pathFinder.dijkstra(source, target));
        source = 1;
        target = 0;
        assertEquals("Distance is: " + 3.0  + " path is: " + "1 -> 0", pathFinder.dijkstra(source, target));
    }
    // --- 2. Graph contains a cycle 0 -> 1 -> 2 -> 0
    @Test
    public void testCycleInGraph() {
        int V = 4;
        int source = 0;
        int target = 3;
        AdjacencyListGraph graph = new AdjacencyListGraph(V);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        DijkstraPathFinder pathFinder = new DijkstraPathFinder(graph, V);
        assertEquals("Distance is: " + 3.0  + " path is: " + "0 -> 1 -> 3", pathFinder.dijkstra(source, target));
    }
    // --- 3. Target vertex is unreachable because two components of the graph are disconnected
    @Test
    public void testUnreachableTarget() {
        int V = 5;
        int source = 0;
        int target = 3;
        AdjacencyListGraph graph = new AdjacencyListGraph(V);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 1, 1);
        graph.addEdge(3, 4, 3);

        DijkstraPathFinder pathFinder = new DijkstraPathFinder(graph, V);
        assertEquals("Target is unreachable!", pathFinder.dijkstra(source, target));
    }
    // --- 4. Larger graph, all vertices are connected in both directions to their adjacent vertices
    @Test
    public void testLargerGraph() {
        int V = 9;
        int source = 0;
        int target = 8;
        AdjacencyListGraph graph = new AdjacencyListGraph(V);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 7, 8);
        graph.addEdge(1, 0, 4);
        graph.addEdge(1, 7, 11);
        graph.addEdge(1, 2, 8);
        graph.addEdge(2, 1, 8);
        graph.addEdge(2, 8, 2);
        graph.addEdge(2, 5, 4);
        graph.addEdge(2, 3, 7);
        graph.addEdge(3, 2, 7);
        graph.addEdge(3, 5, 14);
        graph.addEdge(3, 4, 9);
        graph.addEdge(4, 3, 9);
        graph.addEdge(4, 5, 10);
        graph.addEdge(5, 6, 2);
        graph.addEdge(5, 2, 4);
        graph.addEdge(5, 3, 14);
        graph.addEdge(5, 4, 10);
        graph.addEdge(6, 7, 1);
        graph.addEdge(6, 8, 6);
        graph.addEdge(6, 5, 2);
        graph.addEdge(7, 0, 8);
        graph.addEdge(7, 1, 11);
        graph.addEdge(7, 8, 7);
        graph.addEdge(7, 6, 1);
        graph.addEdge(8, 7, 7);
        graph.addEdge(8, 2, 2);
        graph.addEdge(8, 6, 6);

        DijkstraPathFinder pathFinder = new DijkstraPathFinder(graph, V);
        assertEquals("Distance is: " + 14.0  + " path is: " + "0 -> 1 -> 2 -> 8", pathFinder.dijkstra(source, target));
        target = 4;
        assertEquals("Distance is: " + 21.0  + " path is: " + "0 -> 7 -> 6 -> 5 -> 4", pathFinder.dijkstra(source, target));
    }
}
