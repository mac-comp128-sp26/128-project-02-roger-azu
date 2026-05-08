import java.util.Iterator;
import java.util.Stack;

public class DijkstraPathFinder {
    
    private AdjacencyListGraph graph;
    private MinHeapPriorityQueue<StationNode> distancePQ; // saves the indexes of the stations with shortest path
    private double [] dist; // the distance array used to find the shortest path
    private boolean [] visited;
    private int [] previous;
    private int V; // number of vertices in a given path


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
     * 
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
                    dist[neighbor] = newDistance;
                    previous[neighbor] = v.stationIndex;
                    distancePQ.offer(new StationNode(neighbor, newDistance));
                }
        }
        }
        if (dist[target] != Double.MAX_VALUE) {
            String result = "Distance is: " + dist[target] + " path is: " + reconstructPath(previous, source, target);
            return result;
        }
        return "Target is unreachable!";
    }
    /**
     * Helper method.
     * @param previous
     * @param source
     * @param target
     * @return
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