package com.gardev.neuralnetwork.examples.iris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IrisParser {
    public static ArrayList<Iris> parse(String file) {
        ArrayList<Iris> result = new ArrayList<Iris>();

        BufferedReader bufferedReader = null;
        String line = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] row = line.split(",");

                double sepalLength = Double.parseDouble(row[0]);
                double sepalWidth = Double.parseDouble(row[1]);
                double petalLength = Double.parseDouble(row[2]);
                double petalWidth = Double.parseDouble(row[3]);
                
                Iris dataPoint = null;
                
                if (row.length == 5) {
                    int species = new Integer(row[4]);
                    dataPoint = new Iris(sepalLength, sepalWidth, petalLength, petalWidth, species);
                } else {
                    dataPoint = new Iris(sepalLength, sepalWidth, petalLength, petalWidth);
                }

                result.add(dataPoint);
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("There was an issue parsing the data file!");
            System.out.println(e.getMessage());
        }

        return result;
    }
}
