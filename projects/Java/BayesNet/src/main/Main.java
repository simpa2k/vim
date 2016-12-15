package main;

import network.Network;

/**
 * Created by simpa2k on 2016-12-15.
 */
public class Main {

    public static void main(String[] args) {

        Network network = new Network(true);

        double[] values = new double[] {0.26, 1.0, 56, 0.22};
        network.backPropagate(values);

        System.out.println(network);

        values = new double[] {0.28, 1.0, 56, 0.26};
        network.backPropagate(values);

        System.out.println(network);

        System.out.println(network.getOutput(new double[] {57}));
    }
}
