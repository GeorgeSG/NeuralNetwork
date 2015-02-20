package com.gardev.neuralnetwork.layers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.gardev.neuralnetwork.Connection;
import com.gardev.neuralnetwork.Neuron;

public class NeuralLayer {
    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected NeuralLayer nextLayer = null;
    
    protected Random random = new Random();

    public NeuralLayer(int neuronsCount) {
        for (int i = 0; i < neuronsCount; i++) {
            neurons.add(new Neuron());
        }
    }

    public void connectWith(NeuralLayer nextLayer) {
        this.nextLayer = nextLayer;

        for (Neuron neuron : neurons) {
            for (Neuron nextNeuron : nextLayer.neurons) {
                Connection connection = new Connection(neuron, nextNeuron, randomWeight());
                nextNeuron.addInput(connection);
            }
        }
    }

    public void propagate() {
        for (Neuron neuron : neurons) {
            double sum = 0;

            for (Connection connection : neuron.getInputs()) {
                sum += connection.getWeight() * connection.getStart().getOutput();
            }

            neuron.setOutput(sigmoid(sum));
        }
    }

    public void adjustWeights(double learningRate) {
        for (Neuron neuron : neurons) {
            for (Connection connection : neuron.getInputs()) {
                double newWeight = connection.getWeight() + learningRate * neuron.getError()
                        * connection.getStart().getOutput();

                connection.setWeight(newWeight);
            }
        }
    }
    
    /**
     * @author Georgi Gardev
     * @author Nikola Taushanov
     */
    public void calculateErrors() {
        Map<Neuron, Double> errors = new HashMap<Neuron, Double>();

        for (Neuron neuron : neurons) {
            double neuronOutput = neuron.getOutput();
            neuron.setError(neuronOutput * (1.0 - neuronOutput));

            errors.put(neuron, Double.valueOf(0));
        }

        for (Neuron nextNeuron : nextLayer.neurons) {
            for (Connection connection : nextNeuron.getInputs()) {
                Neuron neuron = connection.getStart();

                double sumIteration = neuron.getError() * connection.getWeight() * connection.getEnd().getError();
                errors.put(neuron, errors.get(neuron) + sumIteration);
            }
        }

        for (Neuron neuron : neurons) {
            neuron.setError(errors.get(neuron));
        }
    }

    private double randomWeight() {
        return random.nextDouble() * 0.1 - 0.05;
    }
    
    private double sigmoid(double sum) {
        return 1.0 / (1.0 + Math.pow(Math.E, -sum));
    }
}
