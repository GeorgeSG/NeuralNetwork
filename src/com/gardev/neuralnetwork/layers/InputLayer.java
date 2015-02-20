package com.gardev.neuralnetwork.layers;

public class InputLayer extends NeuralLayer {

    public InputLayer(int neuronsCount) {
        super(neuronsCount);
    }

    public void propagate() {
        throw new UnsupportedOperationException("The input layer can't be propagated!");
    }

    public void setInput(double... inputs) {
        if (inputs.length != neurons.size()) {
            throw new IllegalArgumentException(
                    "You've passed more inputs than the number of neurons in the input layer!");
        }

        for (int i = 0; i < inputs.length; i++) {
            neurons.get(i).setOutput(inputs[i]);
        }
    }
}
