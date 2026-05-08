import edu.macalester.graphics.*;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.ui.TextField;

import java.util.HashMap;

public class TransitApp {

    private CanvasWindow canvas;

    private TextField startField;
    private TextField destinationField;
    private TextField timeField;

    private Button searchButton;
    private Button showStationsButton;

    private GraphicsText resultText;
    private GraphicsText stationMapText;

    private AdjacencyListGraph graph;
    private CSVReader csvReader;
    private HashMap<Integer, Station> stationMap;

    public TransitApp() {
        canvas = new CanvasWindow("Best Bus Route Finder", 900, 700);

        csvReader = new CSVReader();
        csvReader.loadStopFile("res/gtfs/stops.txt");
        stationMap = csvReader.getStationMap();

        csvReader = new CSVReader();
        graph = csvReader.loadGTFS("res/gtfs");
        stationMap = csvReader.getStationMap();

        setupUI();
    }

    private void setupUI() {
        GraphicsText title = new GraphicsText("Best Bus Route Finder");
        title.setFont(FontStyle.BOLD, 28);
        canvas.add(title, 30, 45);

        GraphicsText startLabel = new GraphicsText("Starting station index:");
        startLabel.setFont(FontStyle.PLAIN, 18);
        canvas.add(startLabel, 30, 100);

        startField = new TextField();
        canvas.add(startField, 250, 78);

        GraphicsText destLabel = new GraphicsText("Destination station index:");
        destLabel.setFont(FontStyle.PLAIN, 18);
        canvas.add(destLabel, 30, 150);

        destinationField = new TextField();
        canvas.add(destinationField, 250, 128);

        GraphicsText timeLabel = new GraphicsText("Time of day (optional):");
        timeLabel.setFont(FontStyle.PLAIN, 18);
        canvas.add(timeLabel, 30, 200);

        timeField = new TextField();
        canvas.add(timeField, 250, 178);

        searchButton = new Button("Find Shortest Route");
        searchButton.onClick(() -> findRoute());
        canvas.add(searchButton, 30, 250);

        showStationsButton = new Button("Print Station IDs");
        showStationsButton.onClick(() -> showStationIds());
        canvas.add(showStationsButton, 220, 250);

        resultText = new GraphicsText("Enter stations, then click Find Shortest Route.");
        resultText.setFont(FontStyle.PLAIN, 16);
        canvas.add(resultText, 30, 320);

        stationMapText = new GraphicsText("");
        stationMapText.setFont(FontStyle.PLAIN, 13);
        canvas.add(stationMapText, 30, 370);
    }

    private void findRoute() {
        try {
            int source = Integer.parseInt(startField.getText());
            int target = Integer.parseInt(destinationField.getText());

            if (graph == null) {
                resultText.setText("Graph is not loaded yet. Connect stop_times.txt in CSVReader first.");
                return;
            }

            if (!stationMap.containsKey(source) || !stationMap.containsKey(target)) {
                resultText.setText("One of those station indices does not exist.");
                return;
            }

            DijkstraPathFinder finder = new DijkstraPathFinder(graph, graph.V());
            String result = finder.dijkstra(source, target);

            resultText.setText(result);

        } catch (NumberFormatException e) {
            resultText.setText("Please enter valid integer station indices.");
        }
    }

    private void showStationIds() {
    StringBuilder sb = new StringBuilder();

    int maxToShow = 15;

    for (int id = 0; id < stationMap.size() && id < maxToShow; id++) {
        Station station = stationMap.get(id);

        if (station != null) {
            sb.append(id)
              .append(": ")
              .append(station.name)
              .append("\n");

            // Full list still goes to terminal
            System.out.println(id + ": " + station.name);
        }
    }

    sb.append("\nShowing first ")
      .append(maxToShow)
      .append(" stations.\n");
    sb.append("Full station list printed in terminal.");

    stationMapText.setText(sb.toString());

    // Print rest to terminal
    for (int id = maxToShow; id < stationMap.size(); id++) {
        Station station = stationMap.get(id);

        if (station != null) {
            System.out.println(id + ": " + station.name);
        }
    }
    }

    public static void main(String[] args) {
        new TransitApp();
    }
}