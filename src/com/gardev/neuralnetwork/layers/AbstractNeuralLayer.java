package com.gardev.neuralnetwork.layers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gardev.neuralnetwork.Connection;
import com.gardev.neuralnetwork.Neuron;

public abstract class AbstractNeuralLayer {
    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected AbstractNeuralLayer nextLayer = null;
    
    protected Random random = new Random();

    public AbstractNeuralLayer(int neuronsCount) {
        for (int i = 0; i < neuronsCount; i++) {
            neurons.add(new Neuron());
        }
    }

    public void connectWith(AbstractNeuralLayer nextLayer) {
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

    public void serialize(ObjectOutputStream out ) {
        try {
            for(int i = 0; i < neurons.size(); i++) {
                for(int j = 0; j < neurons.get(i).getInputs().size(); j++) {
                    out.writeDouble(neurons.get(i).getInputs().get(j).getWeight());
                }
            }
        } catch (Exception E) {
            System.out.println(E);
        }
    }

    public void deserialize(ObjectInputStream in) {
        try {
            for(int i = 0; i < neurons.size(); i++) {
                for(int j = 0; j < neurons.get(i).getInputs().size(); j++) {
                    neurons.get(i).getInputs().get(j).setWeight(in.readDouble());
                }
            }
        } catch (Exception E) {
            System.out.println(E);
        }
    }

    private double randomWeight() {
        return random.nextDouble() * 0.1 - 0.05;
    }
    
    private double sigmoid(double sum) {
        return 1.0 / (1.0 + Math.pow(Math.E, -sum));
    }

}
