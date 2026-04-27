import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader{

    public static Graph loadGraph(String folderPath) throws IOException {
    Map<String, String> stopIdToName =
        loadStops(folderPath + "/stops.txt");

    Map<String, List<StopTime>> trips =
        loadStopTimes(folderPath + "/stop_times.txt");

    return buildGraph(stopIdToName, trips);
    }

    private static Map<String, String> loadStops(String path) throws IOException {
    Map<String, String> map = new HashMap<>();

    BufferedReader br = new BufferedReader(new FileReader(path));
    String line = br.readLine(); // skip header

    while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");

        String stopId = parts[0];
        String stopName = parts[2];

        map.put(stopId, stopName);
    }

    br.close();
    return map;
    }

    private static Map<String, List<StopTime>> loadStopTimes(String path) throws IOException {
    Map<String, List<StopTime>> trips = new HashMap<>();

    BufferedReader br = new BufferedReader(new FileReader(path));
    String line = br.readLine(); // skip header

    while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");

        String tripId = parts[0];
        String arrivalTime = parts[1];
        String stopId = parts[3];
        int sequence = Integer.parseInt(parts[4]);

        int time = parseTime(arrivalTime);

        trips.putIfAbsent(tripId, new ArrayList<>());
        trips.get(tripId).add(new StopTime(stopId, time, sequence));
    }

    br.close();
    return trips;
    }

    private static int parseTime(String t) {
    String[] parts = t.split(":");
    int h = Integer.parseInt(parts[0]);
    int m = Integer.parseInt(parts[1]);
    int s = Integer.parseInt(parts[2]);
    return h * 3600 + m * 60 + s;
    }

}