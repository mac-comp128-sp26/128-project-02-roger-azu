import edu.macalester.graphics.*;
import edu.macalester.graphics.ui.Button;
import edu.macalester.graphics.ui.TextField;

public class TransitApp {

    private CanvasWindow canvas;

    private TextField startField;
    private TextField destinationField;
    private TextField timeField;

    private Button searchButton;
    private GraphicsText resultText;

    private AdjacencyListGraph graph;

    public TransitApp() {
        canvas = new CanvasWindow("Best Bus Route Finder", 800, 600);

        // TODO: Replace this with real GTFS loading later.
        // CSVReader.LoadedTransitData data = CSVReader.loadGTFS("res/gtfs");
        // graph = data.graph;

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

        resultText = new GraphicsText("Enter stations, then click Find Shortest Route.");
        resultText.setFont(FontStyle.PLAIN, 16);
        canvas.add(resultText, 30, 320);
    }

    private void findRoute() {
        try {
            int source = Integer.parseInt(startField.getText());
            int target = Integer.parseInt(destinationField.getText());

            if (graph == null) {
                resultText.setText("Graph is not loaded yet. Connect CSVReader first.");
                return;
            }

            DijkstraPathFinder finder = new DijkstraPathFinder(graph, graph.V());
            String result = finder.dijkstra(source, target);

            resultText.setText(result);

        } catch (NumberFormatException e) {
            resultText.setText("Please enter valid integer station indices.");
        }
    }

    public static void main(String[] args) {
        new TransitApp();
    }
}