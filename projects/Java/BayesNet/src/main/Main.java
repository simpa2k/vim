package main;

import network.Network;
import network.Node;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Main {

    private Node[][] createNodes() {

        Node windSpeed = new Node("Wind Speed", 5, 4);
        Node soilMoistureBefore = new Node("Soil Moisture, before", 5, 4);
        Node uvLight = new Node("UV Light", 5, 4);

        Node irrigate = new Node("Irrigate", 5, 4);

        Node soilMoistureAfter = new Node("Soil Moisture, after", 5, 4);

        Node[][] nodes = new Node[][] {
            {windSpeed, soilMoistureBefore, uvLight},
            {irrigate},
            {soilMoistureAfter}
        };

        return nodes;

    }

    public static void main(String[] args) {

        Main main = new Main();
        Network network = new Network(main.createNodes());

        Double[][] values = new Double[][] {{56.0, 0.22, 3.0}, {1.0}, {0.26}};
        network.addValues(values);

        values = new Double[][] {{56.0, 0.22, 3.0}, {1.0}, {0.26}};
        network.addValues(values);

        values = new Double[][] {{56.0, 0.22, 4.0}, {0.0}, {0.28}};
        network.addValues(values);

        System.out.println(network);

        System.out.println(network.getOutput(new Double[][] {{}, {0.0}}));

    }
}
