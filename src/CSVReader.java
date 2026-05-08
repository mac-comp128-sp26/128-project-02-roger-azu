import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class CSVReader {

    private static final double RAMSEY_SOUTH_LAT = 44.88;
    private static final double RAMSEY_NORTH_LAT = 45.13;
    private static final double RAMSEY_WEST_LON = -93.23;
    private static final double RAMSEY_EAST_LON = -92.98;

    private HashMap<String, Integer> stopIdMap;       // GTFS stop_id -> graph index
    private HashMap<Integer, Station> stationMap;     // graph index -> Station

    public CSVReader() {
        stopIdMap = new HashMap<String, Integer>();
        stationMap = new HashMap<Integer, Station>();
    }
    /**
     * Represents a single stop entry within a trip from the GTFS stop_times.txt file.
     * Stores the stop id, arrival and departure times in seconds, and the stop's 
     * position within the trip sequence. Used internally during graph construction
     * to compute travel times between consecutive stops.
     */
    private static class StopTime {
        String stopId;
        int arrivalTime;
        int departureTime;
        int sequence;

        StopTime(String stopId, int arrivalTime, int departureTime, int sequence) {
            this.stopId = stopId;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.sequence = sequence;
        }
    }
    /**
     * Allows to load files from a folder and derives the parsing of the files to other helper methods.
     * @param folderPath    the path of the folder containing the .txt files
     * @return
     */
    public AdjacencyListGraph loadGTFS(String folderPath) {
        loadStopFile(folderPath + "/stops.txt");
        return loadStopTimesFile(folderPath + "/stop_times.txt");
    }
    /**
     * Parses stops.txt and builds the stop index and station maps.
     * Only valid stops (within Ramsey County) are loaded, filtered by geographic bounding box.
     * Each valid stop is assigned a unique integer index and stored as a Station object.
     * @param path
     */
    public void loadStopFile(String path) {
        int stationIndex = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String headerLine = br.readLine();
            String[] headers = parseCSVLine(headerLine);
            HashMap<String, Integer> headerMap = buildHeaderMap(headers);

            String line;

            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);

                String gtfsStopId = values[headerMap.get("stop_id")];
                String stopName = values[headerMap.get("stop_name")];

                double lat = Double.parseDouble(values[headerMap.get("stop_lat")]);
                double lon = Double.parseDouble(values[headerMap.get("stop_lon")]);

                if (isWithinRamsey(lat, lon)) {
                    stopIdMap.put(gtfsStopId, stationIndex);
                    stationMap.put(stationIndex, new Station(stationIndex, stopName));
                    stationIndex++;
                }
            }
            br.close();
            System.out.println("Loaded Ramsey County stops: " + stationIndex);

        } catch (IOException e) {
            System.out.println("Problem reading stop file: " + path);
            e.printStackTrace();
        }
    }
    /**
     * It parses stops_times.txt file and groups stops by trip.
     * Builds a graph from those trips and returns it.
     * @param path  relative file path where stops_times.txt is stored.
     * @return      a graph of connected stations by a helper method. 
     */
    public AdjacencyListGraph loadStopTimesFile(String path) {
        HashMap<String, ArrayList<StopTime>> trips = new HashMap<String, ArrayList<StopTime>>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String headerLine = br.readLine();
            String[] headers = parseCSVLine(headerLine);
            HashMap<String, Integer> headerMap = buildHeaderMap(headers);

            String line = "";

            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);

                String tripId = values[headerMap.get("trip_id")];
                String arrivalString = values[headerMap.get("arrival_time")];
                String departureString = values[headerMap.get("departure_time")];
                String stopId = values[headerMap.get("stop_id")];
                int sequence = Integer.parseInt(values[headerMap.get("stop_sequence")]);

                int arrivalTime = parseTime(arrivalString);
                int departureTime = parseTime(departureString);

                if (!trips.containsKey(tripId)) {
                    trips.put(tripId, new ArrayList<StopTime>());
                }

                trips.get(tripId).add(new StopTime(stopId, arrivalTime, departureTime, sequence));
            }
            br.close();

        } catch (IOException e) {
            System.out.println("Problem reading stop_times file: " + path);
            e.printStackTrace();
        }
        return buildGraphFromTrips(trips);
    }
    /**
     * Helper method. Builds a directed weighted graph from trip stop-time data. 
     * Consecutive stops in each trip become edges whose weights are
     * the average travel times between stations.
     * @param trips     a map that links trip_id with its respective list of StopTime objects 
     * @return          a directed graph of connected stations 
     */
    private AdjacencyListGraph buildGraphFromTrips(HashMap<String, ArrayList<StopTime>> trips) {
        HashMap<String, ArrayList<Integer>> edgeTimes = new HashMap<String, ArrayList<Integer>>();

        for (ArrayList<StopTime> trip : trips.values()) {
            Collections.sort(trip, new Comparator<StopTime>() {
                public int compare(StopTime a, StopTime b) {
                    return Integer.compare(a.sequence, b.sequence);
                }
            });
            for (int i = 0; i < trip.size() - 1; i++) {
                StopTime current = trip.get(i);
                StopTime next = trip.get(i + 1);

                if (!stopIdMap.containsKey(current.stopId) || !stopIdMap.containsKey(next.stopId)) {
                    continue;
                }
                int u = stopIdMap.get(current.stopId);
                int v = stopIdMap.get(next.stopId);
                int travelTime = next.arrivalTime - current.departureTime;

                if (travelTime < 0) {
                    continue;
                }
                String edgeKey = u + "->" + v;

                if (!edgeTimes.containsKey(edgeKey)) {
                    edgeTimes.put(edgeKey, new ArrayList<Integer>());
                }
                edgeTimes.get(edgeKey).add(travelTime);
            }
        }
        AdjacencyListGraph graph = new AdjacencyListGraph(stationMap.size());

        for (String edgeKey : edgeTimes.keySet()) {
            ArrayList<Integer> times = edgeTimes.get(edgeKey);
            double total = 0;

            for (int time : times) {
                total += time;
            }
            double average = total / times.size();
            String[] parts = edgeKey.split("->");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);

            graph.addEdge(u, v, average);
        }
        System.out.println("Built graph with " + graph.V() + " vertices and " + graph.E() + " edges.");
        return graph;
    }
    /**
     * Helper method. Determines whether the given latitude and longitude coordinates lie within Ramsey County.
     * @param lat   latitude coordinate
     * @param lon   longitute coordinate
     * @return      it's true if the input coordinates are within Ramsey County bounding box, otherwise is false.
     */
    private boolean isWithinRamsey(double lat, double lon) {
        return lat >= RAMSEY_SOUTH_LAT &&
               lat <= RAMSEY_NORTH_LAT &&
               lon >= RAMSEY_WEST_LON &&
               lon <= RAMSEY_EAST_LON;
    }

    private int parseTime(String time) {
        String[] parts = time.split(":");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return hours * 3600 + minutes * 60 + seconds;
    }

    private HashMap<String, Integer> buildHeaderMap(String[] headers) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i], i);
        }
        return map;
    }

    private String[] parseCSVLine(String line) {
        ArrayList<String> values = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                insideQuotes = !insideQuotes;
            } else if (ch == ',' && !insideQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values.toArray(new String[0]);
    }

    public HashMap<String, Integer> getStopIdMap() {
        return stopIdMap;
    }

    public HashMap<Integer, Station> getStationMap() {
        return stationMap;
    }

    public static void main(String[] args) {
        CSVReader reader = new CSVReader();
        AdjacencyListGraph graph = reader.loadGTFS("res/gtfs");

        System.out.println(graph);
    }
}