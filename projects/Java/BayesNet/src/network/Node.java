package network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by simpa2k on 2016-12-15.
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

    public void put(int row, int column, double value) {
        table[row][column] = value;
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

    public double get(int row, int column) {
        return table[row][column];
    }

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

    private ArrayList<Integer> getAll(double value) {

        ArrayList<Integer> rows = new ArrayList<>();

        for(int row = 0; row < table.length; row++) {

            if (table[row][MEASUREMENT] != null && table[row][MEASUREMENT] == value) {
                rows.add(row);
            }
        }

        return rows;
    }

    protected ArrayList<Double> getOutput(double value) {

        ArrayList<Integer> rows = getAll(value);
        return feedForward(rows);

    }

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

    public boolean search(double value) {

        for (int i = 0; i < table.length; i++) {

            if (table[i][MEASUREMENT] != null && table[i][MEASUREMENT] == value) {
                return true;
            }
        }

        if(children.length > 0) {

            for (Node child : children) {
                child.search(value);
            }

        }

        return false;

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
