package network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a node in a Bayesian network.
 */
public class Node {

    private static final int MEASUREMENT = 0;
    private static final int OPPORTUNITIES = 1;
    private static final int OBSERVATIONS = 2;
    private static final int PROBABILITY = 3;

    private String name;

    private Node[] parents = new Node[0];
    private Node[] children = new Node[0];

    private Double[][] table;
    private int currentRow = 0;
    private double dataPoints = 0;

    public Node(String name, int rows, int columns) {

        this.name = name;
        table = new Double[rows][columns];

    }

    public String getName() {
        return name;
    }

    public void setParents(Node[] parents) {
        this.parents = parents;
    }

    public void setChildren(Node[] children) {
        this.children = children;
    }

    private void addNewRow(double measurement, boolean duplicateRows) {

        table[currentRow][MEASUREMENT] = measurement;

        if (duplicateRows) {
            if (table[currentRow][OBSERVATIONS] == null) table[currentRow][OBSERVATIONS] = 1.0;
        } else {
            table[currentRow][OBSERVATIONS] = 1.0;
        }
        table[currentRow][PROBABILITY] = table[currentRow][OBSERVATIONS] / table[currentRow][OPPORTUNITIES];

        currentRow++;

    }

    /**
     * Method for adding a value to the node's table. Updates the opportunities of all
     * rows in the table and increments observations instead of adding a new row if
     * the measurement is already in the table.
     *
     * @param measurement The value to be added to the table.
     * @param duplicateRows Boolean value indicating whether to add the measurement on 
     * a new row even if the value already exists in the table. This is determined by 
     * the network and is needed if some other node in the table does not contain the 
     * value passed to it during the same run, thus resulting in the value being added 
     * to a new row in that node's table. This makes sure that values in a node's table always 
     * map to the correct values in different node tables, by virtue of being 
     * stored on the same row indices. See the documentation on getOutput.
     */
    public void updateTable(double measurement, boolean duplicateRows) {

        dataPoints++;
        boolean addNewRow = true;

        for (int row = 0; row < table.length; row++) {

            table[row][OPPORTUNITIES] = dataPoints;

            if(table[row][MEASUREMENT] != null) {

                if (table[row][MEASUREMENT] == measurement) {

                    table[row][OBSERVATIONS]++;

                    if(duplicateRows) {
                        table[currentRow][OBSERVATIONS] = table[row][OBSERVATIONS];
                    } else {
                        addNewRow = false;
                    }

                }
                table[row][PROBABILITY] = table[row][OBSERVATIONS] / table[row][OPPORTUNITIES];
            }
        }

        if (addNewRow) {
            addNewRow(measurement, duplicateRows);
        }

    }

    /**
     * Method for adding a set of values to the node and its children. Picks out the first element in the
     * set of values, removes it and passes the rest down the network.
     *
     * @param values The set of values to be added.
     * @param duplicateRows Boolean value indicating whether to add the measurement on a new row even if the 
     * value already exists in the table. See the documentation on updateTable for more details.
     */
    protected double[] addValues(double[] values, boolean duplicateRows) {

        if(values.length > 0) {

            updateTable(values[0], duplicateRows);

            values = Arrays.copyOfRange(values, 1, values.length);

            for (int i = 0; i < children.length; i++) {
                values = children[i].addValues(values, duplicateRows);
            }
        }

        return values;

    }

    /**
     * Method for retrieving the indices of each row a given value occurs on in the node's table.
     *
     * @param value The value to retrieve the row indices of.
     */
    private ArrayList<Integer> getAll(double value) {

        ArrayList<Integer> rows = new ArrayList<>();

        for(int row = 0; row < table.length; row++) {

            if (table[row][MEASUREMENT] != null && table[row][MEASUREMENT] == value) {
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Method for getting the result of a given measurement. Works by picking out all 
     * the rows that the given value occurs on in the node getOutput is called on and 
     * then going through the whole network to the bottommost child node and picking out 
     * the values on the corresponding rows. This means that the method assumes that there 
     * is a correspondence between all the rows of a table. If, for example, the 
     * measurement 56 resulted in 0.22 these two values need to be stored
     * on the same rows in the nodes they belong to.
     *
     * @param value The value to get the result of.
     */
    protected ArrayList<Double> getOutput(double value) {

        ArrayList<Integer> rows = getAll(value);
        return feedForward(rows);

    }

    /**
     * Method for travelling down the given node's children and pick out results stored on the specified rows.
     *
     * @param rows A list of all the rows that contain the relevant values.
     */
    protected ArrayList<Double> feedForward(ArrayList<Integer> rows) {

        ArrayList<Double> results = new ArrayList<>();

        if (children.length == 0) {

            for (Integer row : rows) {
                results.add(table[row][MEASUREMENT]);
            }

        }

        if (children.length > 0) {

            for (int i = 0; i < children.length; i++) {

                results = children[i].feedForward(rows);
            }

        }

        return results;

    }

    @Override
    public String toString() {

        String output = "";

        for (int row = 0; row < table.length; row++) {

            for (int column = 0; column < table[row].length; column++) {

                String value = table[row][column] == null ? "" : "" + table[row][column];
                output += String.format("[%s]", value);

            }
            output += "\n";
        }
        return output;
    }
}
