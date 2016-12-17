package main;

import network.Network;
import network.Node;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Main {

    private Node[] createNodes() {

        Node windSpeed = new Node("Wind Speed", 5, 4);
        Node soilMoistureBefore = new Node("Soil Moisture, before", 5, 4);
        Node uvLight = new Node("UV Light", 5, 4);

        Node irrigate = new Node("Irrigate", 5, 4);

        Node soilMoistureAfter = new Node("Soil Moisture, after", 5, 4);

        /*
        ToDo: Move connection of nodes back inside Network again. The array passed to its constructor should be two-dimensional, modelling the layers which form the basis for connections.
         */
        soilMoistureAfter.setParents(new Node[] {irrigate});
        irrigate.setChildren(new Node[] {soilMoistureAfter});

        irrigate.setParents(new Node[] {windSpeed, soilMoistureBefore, uvLight});

        windSpeed.setChildren(new Node[] {irrigate});
        soilMoistureBefore.setChildren(new Node[] {irrigate});
        uvLight.setChildren(new Node[] {irrigate});

        Node[] nodes = new Node[5];

        String[][] nodeLayers = new String[][] {
                {"Wind Speed", "Soil Moisture, before", "UV Light"},
                {"Irrigate"},
                {"Soil Moisture, after"}
        };

        nodes[0] = windSpeed;
        nodes[1] = soilMoistureBefore;
        nodes[2] = uvLight;
        nodes[3] = irrigate;
        nodes[4] = soilMoistureAfter;

        return nodes;

    }

    public static void main(String[] args) {

        Main main = new Main();
        Network network = new Network(main.createNodes());

        Double[] values = new Double[] {56.0, 0.22, 3.0, 1.0, 0.26};
        network.addValues(values);

        values = new Double[] {56.0, 0.22, 3.0, 1.0, 0.26};
        network.addValues(values);

        values = new Double[] {56.0, 0.26, 4.0, 0.0, 0.28};
        network.addValues(values);

        System.out.println(network);

        System.out.println(network.getOutput(new Double[] {56.0, 0.22}));

    }
}
