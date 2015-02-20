package com.gardev.neuralnetwork;

import java.util.ArrayList;

public class Neuron {
    private ArrayList<Connection> inputs = new ArrayList<Connection>();
    private double output;
    private double error;

    public double getOutput() {
        return this.output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getError() {
        return this.error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public void addInput(Connection connection) {
        inputs.add(connection);
    }

    public ArrayList<Connection> getInputs() {
        return inputs;
    }
}
