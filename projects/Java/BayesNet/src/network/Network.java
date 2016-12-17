package network;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a Bayesian network.
 */
public class Network {

    private Node[][] nodes;

    public Network(Node[][] nodes) {

        int lastLayer = 0;
        for (int layer = 1; layer < nodes.length; layer++) {

            for (int node = 0; node < nodes[layer].length; node++) {
                nodes[layer][node].setParents(nodes[lastLayer]);
            }

            for (int lastLayerNode = 0; lastLayerNode < nodes[lastLayer].length; lastLayerNode++) {
                nodes[lastLayer][lastLayerNode].setChildren(nodes[layer]);
            }

            lastLayer = layer;
        }

        this.nodes = nodes;
    }

    /**
     * Method for adding a set of values to the network.
     * First checks if the given set of values passed already exist in the network.
     * If they do, the probability of them occuring is updated, if they do not
     * the set is added to the network.
     *
     * @param values A set of values to be added to the network. The values need to be 
     * ordered in the exact same way as the nodes in the network are structured. If
     * certain input values are unknown they should be set to null.
     */
    public void addValues(Double[][] values) {

        boolean duplicateRows = getOutput(values) == null ? true : false;

        for(int layer = 0; layer < values.length; layer++) {

            for(int node = 0; node < nodes[layer].length; node++) {
                nodes[layer][node].updateTable(values[layer][node], duplicateRows);
            }
        }

    }

    /**
     * Method for running a set of values through the network and get the value(s) they resulted in.
     *
     * @param values A set of values to be run through the network. The values need to be 
     * ordered in the exact way as the nodes in the network are structured. If
     * certain input values are unknown they should be set to null.
     */
    public HashSet<Double> getOutput(Double[][] values) throws IllegalArgumentException {

        if (values.length > nodes.length) {
            throw new IllegalArgumentException("The array of values passed must be of a length equal to or lower than the number of nodes.");
        }

        ArrayList<HashSet<Double>> retrievedValues = new ArrayList<>();

        for (int layer = 0; layer < values.length; layer++) {

            for (int value = 0; value < values[layer].length; value++) {

                if(values[layer].length > nodes[layer].length) {
                    throw new IllegalArgumentException("Each sub array of values must be of a length equal to or lower than the number of nodes in the corresponding network layer.");
                }

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
        return intersect(retrievedValues);
    }

    /**
     * Method for taking the intersection of a series of sets of values.
     *
     * @param retrievedValues A list of sets containing the values to be intersected
     */
    private HashSet<Double> intersect(ArrayList<HashSet<Double>> retrievedValues) {

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
