package com.gardev.neuralnetwork;

import java.util.ArrayList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

import com.gardev.neuralnetwork.layers.InputLayer;
import com.gardev.neuralnetwork.layers.HiddenLayer;
import com.gardev.neuralnetwork.layers.OutputLayer;

public class NeuralNetwork {
    private InputLayer input;
    private ArrayList<HiddenLayer> hidden = new ArrayList<HiddenLayer>();
    private OutputLayer output;

    public NeuralNetwork(int... layerSize) {
        if (layerSize.length < 3) {
            throw new IllegalArgumentException("You must specify at least three layers");
        }

        this.input = new InputLayer(layerSize[0]);
        this.output = new OutputLayer(layerSize[layerSize.length - 1]);

        // Initialize and connect hidden layers
        for (int i = 1; i < layerSize.length - 1; i++) {
            HiddenLayer hiddenLayer = new HiddenLayer(layerSize[i]);

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

    public double[] getOutputs(double... inputs) {
        activate(inputs);
        return output.getOutputs();
    }

    public void serializeNetwork(OutputStream stream) {
        if (stream != null) {
            try {
                ZipOutputStream zipout = new ZipOutputStream(stream);
                zipout.setLevel(9);
                zipout.putNextEntry(new ZipEntry("data"));
                ObjectOutputStream out = new ObjectOutputStream(zipout);

                input.serialize(out);
                for(int i = 0; i < hidden.size(); i++) {
                    hidden.get(i).serialize(out);
                }

                out.flush();
                zipout.closeEntry();
                out.close();
            } catch (Exception E) {
                System.out.println(E);
            }
        }
    }

    public void deserializeNetwork(InputStream stream) {
        if (stream != null) {
            try {
                ZipInputStream zipin = new ZipInputStream(stream);
                zipin.getNextEntry();
                ObjectInputStream in = new ObjectInputStream(zipin);

                input.deserialize(in);
                for(int i = 0; i < hidden.size(); i++) {
                    hidden.get(i).deserialize(in);
                }

                in.close();
            } catch (Exception E) {
                System.out.println(E);
            }
        }
    }

    private void activate(double... inputs) {
        input.setInput(inputs);

        for (HiddenLayer hiddenLayer : hidden) {
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
