package network;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Network {

    private Node[] nodes;
    private boolean duplicateRows;

    public Network(boolean duplicateRows) {

        this.duplicateRows = duplicateRows;

        nodes = new Node[4];

        Node windSpeed = new Node("Wind Speed", 5, 4, duplicateRows);
        Node soilMoistureBefore = new Node("Soil Moisture, before", 5, 4, duplicateRows);

        Node irrigate = new Node("Irrigate", 5, 4, duplicateRows);

        Node soilMoistureAfter = new Node("Soil Moisture, after", 5, 4, duplicateRows);

        soilMoistureAfter.setParents(new Node[] {irrigate});
        irrigate.setChildren(new Node[] {soilMoistureAfter});

        irrigate.setParents(new Node[] {windSpeed, soilMoistureBefore});

        windSpeed.setChildren(new Node[] {irrigate});
        soilMoistureBefore.setChildren(new Node[] {irrigate});

        nodes[0] = windSpeed;
        nodes[1] = soilMoistureBefore;
        nodes[2] = irrigate;
        nodes[3] = soilMoistureAfter;

    }

    public void backPropagate(double[] values) {
        nodes[nodes.length - 1].backPropagate(values);
    }

    public HashSet<Double> getOutput(double[] values) throws IllegalArgumentException {

        if (values.length > nodes.length) {
            throw new IllegalArgumentException("The array of values passed must be of a length equal to or lower than the number of nodes.");
        }

        HashSet<Double> retrievedValues = new HashSet<>();

        for (int i = 0; i < values.length; i++) {
            retrievedValues.addAll(nodes[i].getOutput(values[i]));
        }

        return retrievedValues;
    }

    @Override
    public String toString() {

        String output = "";

        for (Node node : nodes) {
            output += String.format("%s:\n%s", node.getName(), node.toString());
        }

        return output;
    }
}
