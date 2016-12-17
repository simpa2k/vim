package network;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Network {

    private Node[] nodes;
    private Node[] topNodes;

    private boolean duplicateRows;

    public Network(boolean duplicateRows) {

        this.duplicateRows = duplicateRows;

        nodes = new Node[5];
        topNodes = new Node[3];

        Node windSpeed = new Node("Wind Speed", 5, 4);
        Node soilMoistureBefore = new Node("Soil Moisture, before", 5, 4);
        Node uvLight = new Node("UV Light", 5, 4);

        Node irrigate = new Node("Irrigate", 5, 4);

        Node soilMoistureAfter = new Node("Soil Moisture, after", 5, 4);

        soilMoistureAfter.setParents(new Node[] {irrigate});
        irrigate.setChildren(new Node[] {soilMoistureAfter});

        irrigate.setParents(new Node[] {windSpeed, soilMoistureBefore, uvLight});

        windSpeed.setChildren(new Node[] {irrigate});
        soilMoistureBefore.setChildren(new Node[] {irrigate});
        uvLight.setChildren(new Node[] {irrigate});

        nodes[0] = windSpeed;
        topNodes[0] = windSpeed;

        nodes[1] = soilMoistureBefore;
        topNodes[1] = soilMoistureBefore;

        nodes[2] = uvLight;
        topNodes[2] = uvLight;

        nodes[3] = irrigate;
        nodes[4] = soilMoistureAfter;

    }

    public void addValues(double[] values) {

        boolean duplicateRows = getOutput(values) == null ? true : false;

        for(int i = 0; i < values.length; i++) {
            nodes[i].updateTable(values[i], duplicateRows);
        }

    }

    public HashSet<Double> getOutput(double[] values) throws IllegalArgumentException {

        if (values.length > nodes.length) {
            throw new IllegalArgumentException("The array of values passed must be of a length equal to or lower than the number of nodes.");
        }

        HashSet<Double> retrievedValues = new HashSet<>();

        for (int i = 0; i < values.length; i++) {

            ArrayList<Double> nodeOutput = nodes[i].getOutput(values[i]);

            if (nodeOutput.isEmpty()) {
                return null;
            } else {
                retrievedValues.addAll(nodeOutput);
            }
        }

        return retrievedValues;
    }

    /*
    ToDo: Make sure child nodes don't get searched multiple times if they have the same parent.
     */
    public boolean search(double value) {

        for (Node topNode : topNodes) {
            if(topNode.search(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsOne(double[] values) {

        for(int i = 0; i < values.length; i++) {
            if(search(values[i])) {
                return true;
            }
        }
        return false;
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
