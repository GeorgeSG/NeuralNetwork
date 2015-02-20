package com.gardev.neuralnetwork.layers;

import com.gardev.neuralnetwork.Neuron;

public class OutputLayer extends NeuralLayer {

    public OutputLayer(int neuronsCount) {
        super(neuronsCount);
    }

    public int getResult() {
        int max = 0;
        double maxValue = neurons.get(0).getOutput();

        for (int i = 0; i < neurons.size(); i++) {
            double result = neurons.get(i).getOutput();
            if (maxValue < result) {
                maxValue = result;
                max = i;
            }
        }

        return max;
    }

    public void calculateErrors(double[] expectedOutputs) {
        for (int i = 0; i < neurons.size(); i++) {
            Neuron outputNeuron = neurons.get(i);
            double output = outputNeuron.getOutput();

            double error = output * (1.0 - output) * (expectedOutputs[i] - output);
            outputNeuron.setError(error);
        }
    }

    public void calculateErrors() {
        throw new UnsupportedOperationException("You must supply expected outputs for the output layer!");
    }
}
