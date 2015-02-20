package com.gardev.neuralnetwork;

public class Connection {
    private Neuron start;
    private Neuron end;
    private double weight;

    public Connection(Neuron start, Neuron end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Neuron getStart() {
        return this.start;
    }

    public Neuron getEnd() {
        return this.end;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
