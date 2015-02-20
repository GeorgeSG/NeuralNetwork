package com.gardev.neuralnetwork.examples.iris;

import java.util.ArrayList;
import java.util.Collections;

import com.gardev.neuralnetwork.NeuralNetwork;

public class IrisMain {

    public static void main(String[] args) {
        int hiddenLayerNeurons = 5;
        int learningIterations = 50;
        double learningRate = 0.1;
        
        ArrayList<Iris> testData = IrisParser.parse("example_data/iris/iris_test.txt");
        ArrayList<Iris> trainData = IrisParser.parse("example_data/iris/iris_train.txt");
        Collections.shuffle(trainData);
        
        NeuralNetwork network = new NeuralNetwork(4, hiddenLayerNeurons, 3);

        for (int i = 0; i < learningIterations; i++) {
            for (Iris iris : trainData) {
                network.train(generateInput(iris), generateOutput(iris), learningRate);
            }
        }

        for (Iris iris : testData) {
            int result = network.test(generateInput(iris)) + 1;
            System.out.println(result);
        }
    }

    private static double[] generateInput(Iris iris) {
        double[] inputs = { iris.getSepalLength(), iris.getSepalWidth(), iris.getPetalLength(), iris.getPetalWidth() };
        return inputs;
    }

    private static double[] generateOutput(Iris iris) {
        double[] output = {0, 0, 0};
        output[iris.getSpecies() - 1] = 1;

        return output;
    }
}
