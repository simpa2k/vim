package main;

import network.Network;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Main {

    public static void main(String[] args) {

        Network network = new Network(true);

        double[] values = new double[] {56, 0.22, 3, 1.0, 0.26};
        network.addValues(values);

        System.out.println(network);

        values = new double[] {56, 0.22, 3, 1.0, 0.26};
        network.addValues(values);

        System.out.println(network);

        values = new double[] {56, 0.26, 4, 1.0, 0.28};
        network.addValues(values);

        System.out.println(network);

        //System.out.println(network.getOutput(new double[] {56}));
    }
}
