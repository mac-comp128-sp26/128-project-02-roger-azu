import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CSVReader {


    public CSVReader() {

    }

    public void loadStopFile(String path) {
        String line = "";
        int stopId = 0;
        HashMap<String, Integer> stopIdMap = new HashMap<String, Integer>();
        

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                stopIdMap.put(values[0], stopId); // stopId with stopIndex map
                stopId ++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // String path = "/Users/azu_v22/Desktop/greenhouse-gas-emissions.csv"; // need to update data in res folder
        // String line = "";

        // try {
        //     BufferedReader br = new BufferedReader(new FileReader(path));
        //     while ((line = br.readLine()) != null) {
        //         String[] values = line.split(",");
        //         System.out.println(values[9]);
        //     }

        // } catch (FileNotFoundException e) {
        //     e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        String stopsPath = "res/gtfs/stops.txt";
        CSVReader csvReader = new CSVReader();
        csvReader.loadStopFile(stopsPath);

    }



}
