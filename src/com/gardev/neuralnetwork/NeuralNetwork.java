package com.gardev.neuralnetwork;

import java.util.ArrayList;

import com.gardev.neuralnetwork.layers.InputLayer;
import com.gardev.neuralnetwork.layers.NeuralLayer;
import com.gardev.neuralnetwork.layers.OutputLayer;

public class NeuralNetwork {
    private InputLayer input;
    private ArrayList<NeuralLayer> hidden = new ArrayList<NeuralLayer>();
    private OutputLayer output;

    public NeuralNetwork(int... layerSize) {
        if (layerSize.length < 3) {
            throw new IllegalArgumentException("You must specify at least three layers");
        }

        this.input = new InputLayer(layerSize[0]);
        this.output = new OutputLayer(layerSize[layerSize.length - 1]);

        // Initialize and connect hidden layers
        for (int i = 1; i < layerSize.length - 1; i++) {
            NeuralLayer hiddenLayer = new NeuralLayer(layerSize[i]);

            if (i == 1) {
                input.connectWith(hiddenLayer);
            } else {
                hidden.get(hidden.size() - 1).connectWith(hiddenLayer);
            }

            hidden.add(hiddenLayer);
        }

        // connect last hidden layer with output
        hidden.get(hidden.size() - 1).connectWith(output);
    }

    public void train(double[] inputs, double[] expectedOutputs, double learningRate) {
        activate(inputs);
        calculateErrors(expectedOutputs);
        adjustWeights(learningRate);
    }

    public int test(double... inputs) {
        activate(inputs);
        return output.getResult();
    }

    private void activate(double... inputs) {
        input.setInput(inputs);

        for (NeuralLayer hiddenLayer : hidden) {
            hiddenLayer.propagate();
        }

        output.propagate();
    }

    private void calculateErrors(double[] expectedOutputs) {
        output.calculateErrors(expectedOutputs);

        for (int i = hidden.size() - 1; i >= 0; i--) {
            hidden.get(i).calculateErrors();
        }
    }

    private void adjustWeights(double learningRate) {
        output.adjustWeights(learningRate);

        for (int i = hidden.size() - 1; i >= 0; i--) {
            hidden.get(i).adjustWeights(learningRate);
        }

        input.adjustWeights(learningRate);
    }
}
