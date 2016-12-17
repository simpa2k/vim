package network;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Network {

    private Node[] nodes;

    public Network(Node[] nodes) {
        this.nodes = nodes;
    }

    public void addValues(Double[] values) {

        boolean duplicateRows = getOutput(values) == null ? true : false;

        for(int i = 0; i < values.length; i++) {
            nodes[i].updateTable(values[i], duplicateRows);
        }

    }

    public HashSet<Double> getOutput(Double[] values) throws IllegalArgumentException {

        if (values.length > nodes.length) {
            throw new IllegalArgumentException("The array of values passed must be of a length equal to or lower than the number of nodes.");
        }

        ArrayList<HashSet<Double>> retrievedValues = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {

            if(values[i] != null) {
                ArrayList<Double> nodeOutput = nodes[i].getOutput(values[i]);

                if (nodeOutput.isEmpty()) {
                    return null;
                } else {
                    retrievedValues.add(new HashSet<>(nodeOutput));
                    //retrievedValues.addAll(nodeOutput);
                }
            }
        }

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

    /*
    ToDo: Make sure child nodes don't get searched multiple times if they have the same parent.
     */
    /*public boolean search(double value) {

        for (Node topNode : topNodes) {
            if(topNode.search(value)) {
                return true;
            }
        }
        return false;
    }*/

    /*public boolean containsOne(double[] values) {

        for(int i = 0; i < values.length; i++) {
            if(search(values[i])) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public String toString() {

        String output = "";

        for (Node node : nodes) {
            output += String.format("%s:\n%s", node.getName(), node.toString());
        }

        return output;
    }
}
