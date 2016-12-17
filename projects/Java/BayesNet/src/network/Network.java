package network;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Network {

    private Node[][] nodes;

    public Network(Node[][] nodes) {

        int lastLayer = 0;
        for (int layer = 0; layer < nodes.length; layer++) {

            if (layer == 0) {
                continue;
            } else {

                for (int node = 0; node < nodes[layer].length; node++) {
                    nodes[layer][node].setParents(nodes[lastLayer]);
                }
                for (int lastLayerNode = 0; lastLayerNode < nodes[lastLayer].length; lastLayerNode++) {
                    nodes[lastLayer][lastLayerNode].setChildren(nodes[layer]);
                }
            }
            lastLayer = layer;
        }

        this.nodes = nodes;
    }

    public void addValues(Double[][] values) {

        boolean duplicateRows = getOutput(values) == null ? true : false;

        for(int layer = 0; layer < values.length; layer++) {

            for(int node = 0; node < nodes[layer].length; node++) {
                nodes[layer][node].updateTable(values[layer][node], duplicateRows);
            }
        }

    }

    public HashSet<Double> getOutput(Double[][] values) throws IllegalArgumentException {

        if (values.length > nodes.length) {
            throw new IllegalArgumentException("The array of values passed must be of a length equal to or lower than the number of nodes.");
        }

        ArrayList<HashSet<Double>> retrievedValues = new ArrayList<>();

        for (int layer = 0; layer < values.length; layer++) {

            for (int value = 0; value < values[layer].length; value++) {

                if (values[layer][value] != null) {
                    ArrayList<Double> nodeOutput = nodes[layer][value].getOutput(values[layer][value]);

                    if (nodeOutput.isEmpty()) {
                        return null;
                    } else {
                        retrievedValues.add(new HashSet<>(nodeOutput));
                    }
                }
            }
        }

        // Take the intersection of all retrieved sets of values
        HashSet<Double> lastSet = null;
        for (HashSet<Double> set : retrievedValues) {

            if (lastSet == null) {
                lastSet = set;
            } else {
                lastSet.retainAll(set);
            }

        }

        return lastSet;
    }

    @Override
    public String toString() {

        String output = "";

        for (int layer = 0; layer < nodes.length; layer++) {

            for (int node = 0; node < nodes[layer].length; node++) {
                output += String.format("%s:\n%s", nodes[layer][node].getName(), nodes[layer][node].toString());
            }
        }

        return output;
    }
}
