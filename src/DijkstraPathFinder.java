import java.util.Iterator;
import java.util.Stack;

public class DijkstraPathFinder {
    
    private AdjacencyListGraph graph;
    private MinHeapPriorityQueue<StationNode> distancePQ; // saves the indexes of the stations with shortest path
    private double [] dist; // the distance array used to find the shortest path
    private boolean [] visited;
    private int [] previous;
    private int V; // number of vertices in a given path

    /**
     * Represents a station and its current known shortest distance from the source.
     * Used as entries in the min heap priority queue during Dijkstra's algorithm.
     * Natural ordering is defined by distance: closer stations have higher priority.
     */   
    public static class StationNode implements Comparable<StationNode> {
        public final int stationIndex; // station's index in the graph
        public final double distance; // current known distance from the source

        public StationNode(int stationIdx, double distance) {
            this.stationIndex = stationIdx;
            this.distance = distance;
        }
        /**
         * Shorter distance from the source define the natural ordering of stations.
         */
        public int compareTo(StationNode obj) {
            return Double.compare(this.distance, obj.distance);
        }
    }

    public DijkstraPathFinder(AdjacencyListGraph g, int V) {
        this.graph = g;
        this.V = V;
        distancePQ = new MinHeapPriorityQueue<StationNode>();
        dist = new double[V];
        visited = new boolean[V];
        previous = new int[V];
    }
    /**
     * All station distances are initialized to infinity and updated as each vertex is processed.
     * A min heap priority queue is used to always process the closest unvisited station first.
     * For each station, adjacent edges are relaxed: if the distance from the source through the 
     * current station is shorter than the previously known distance, the estimate is updated.
     * @param source    represents the index of the source in the graph
     * @param target    represents the index of the target destination in the graph
     */
    public String dijkstra(int source, int target) {

        distancePQ = new MinHeapPriorityQueue<StationNode>();
        
        for (int i = 0; i < V; i ++) {
            dist[i] = Double.MAX_VALUE; // setting up all vertices distance from source as infinity
            visited[i] = false; // setting up all vertices as unexplored
            previous[i] = -1;
        }
        dist[source] = 0;
        previous[source] = -1; // there are no previous stations for the source
        distancePQ.offer(new StationNode(source, 0)); // we beginning Dijkstra's algorithm at the source station

        while (!visited[target] && !distancePQ.isEmpty()) {
            StationNode v = distancePQ.poll();
            
            if (visited[v.stationIndex]) {
                continue; // skip this duplicate station if we already visited it
            }
            visited[v.stationIndex] = true;
            // finding all the routes station v is adjacent to
            Iterator<AdjacencyListGraph.Edge> iter = graph.adjEdges(v.stationIndex).iterator(); 
            while (iter.hasNext()) {
                AdjacencyListGraph.Edge edge = iter.next();
                int neighbor = edge.destination;
                double newDistance = dist[v.stationIndex] + edge.weight; 
                
                if (newDistance < dist[neighbor]) { 
                    dist[neighbor] = newDistance; // performing relaxation process; a better distance estimate has been found
                    previous[neighbor] = v.stationIndex;
                    distancePQ.offer(new StationNode(neighbor, newDistance));
                }
        }
        }
        if (dist[target] != Double.MAX_VALUE) {
            String result = "Time in seconds is: " + dist[target] + " path is: " + reconstructPath(previous, source, target);
            return result;
        }
        return "Target is unreachable!";
    }
    /**
     * Helper method. Reconstructs the shortest path from source to target by backtracking through
     * the previous array built during Dijkstra's algorithm.
     * Start at target, then each station's predecessor is pushed onto a stack
     * until the source is reached. The stack is then popped to produce the path
     * in the correct source-to-target order.
     * @param previous  an array of vertices indexes that allows backtracking 
     * @param source    index of source
     * @param target    index of target
     * @return a string that contains the path followed in source-to-target order
     */
    private String reconstructPath(int[] previous, int source, int target) {
    Stack<Integer> path = new Stack<Integer>();
    StringBuilder sb = new StringBuilder();
    int i = target;

    while (i != -1) {
        path.push(i);
        if (i == source) {
            break;
        }
        i = previous[i];
    }
    if (path.peek() != source) {
        return "No path found";
    }
    while (!path.isEmpty()) {
        int station = path.pop();

        if (!path.isEmpty()) {
            sb.append(station).append(" -> ");
        } else {
            sb.append(station);
        }
    }
    return sb.toString();
    }
}