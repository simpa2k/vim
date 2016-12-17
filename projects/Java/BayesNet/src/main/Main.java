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

        soilMoistureAfter.setParents(new Node[] {irrigate});
        irrigate.setChildren(new Node[] {soilMoistureAfter});

        irrigate.setParents(new Node[] {windSpeed, soilMoistureBefore, uvLight});

        windSpeed.setChildren(new Node[] {irrigate});
        soilMoistureBefore.setChildren(new Node[] {irrigate});
        uvLight.setChildren(new Node[] {irrigate});

        Node[] nodes = new Node[5];

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

        double[] values = new double[] {56, 0.22, 3, 1.0, 0.26};
        network.addValues(values);

        System.out.println(network);

        values = new double[] {56, 0.22, 3, 1.0, 0.26};
        network.addValues(values);

        System.out.println(network);

        values = new double[] {56, 0.26, 4, 1.0, 0.28};
        network.addValues(values);

        System.out.println(network);

    }
}
