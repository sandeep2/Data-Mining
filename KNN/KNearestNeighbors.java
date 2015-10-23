/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import weka.core.*;

/**
 *
 * @author hai
 */
public class KNearestNeighbors {

    /**
     * @param args the command line arguments
     */
    String trainingFile, testFile, outputFile;

    public KNearestNeighbors(String trainingFile, String testFile, String outputFile) {
        this.trainingFile = trainingFile;
        this.testFile = testFile;
        this.outputFile = outputFile;
    }

    public static void main(String[] args) {

        KNearestNeighbors knn = new KNearestNeighbors("train.arff", "test.arff", "KNNOutput.csv");
        knn.runKNN();

    }

    void runKNN() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.trainingFile));
        } catch (FileNotFoundException ex) {
            System.out.println("Error opening file");
        }
        Instances training = null;
        try {
            training = new Instances(reader);
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
        try {
            reader.close();
        } catch (IOException ex) {
            System.out.println("Error closing file");
        }

        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new FileReader(this.testFile));
        } catch (FileNotFoundException ex) {
            System.out.println("Error opening test file");
        }
        Instances testing = null;
        try {
            testing = new Instances(reader1);
        } catch (IOException ex) {
            System.out.println("Error reading test file");
        }
        try {
            reader1.close();
        } catch (IOException ex) {
            System.out.println("Error closing test file");
        }

        testing.insertAttributeAt(new Attribute("k=1", (FastVector) null), testing.numAttributes());
        testing.insertAttributeAt(new Attribute("k=3", (FastVector) null), testing.numAttributes());
        testing.insertAttributeAt(new Attribute("k=5", (FastVector) null), testing.numAttributes());
        testing.insertAttributeAt(new Attribute("k=7", (FastVector) null), testing.numAttributes());
        testing.insertAttributeAt(new Attribute("k=9", (FastVector) null), testing.numAttributes());
        double[] mean = new double[testing.numAttributes()];
        double[] std = new double[testing.numAttributes()];
        for (int i = 0; i < testing.numAttributes(); i++) {
            for (int j = 0; j < testing.numInstances(); j++) {
                mean[i] += testing.instance(j).value(i);
            }
            mean[i] /= testing.numInstances();
        }
        for (int i = 0; i < testing.numAttributes(); i++) {
            for (int j = 0; j < testing.numInstances(); j++) {
                std[i] += (testing.instance(j).value(i) - mean[i]) * (testing.instance(j).value(i) - mean[i]);
            }
            std[i] /= testing.numInstances() - 1;
            std[i] = Math.sqrt(std[i]);
        }
        for (int i = 0; i < testing.numInstances(); i++) {
            Instance test = testing.instance(i);
            int k = 4;
            for (int c = 5; c < 10; c++) {
                test.setValue(c - 1, this.nearestK(training, test, mean, std, c - k--));
            }
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(this.outputFile));
        } catch (IOException ex) {
            System.out.println("Error creating output file");
        }
        String result = testing.toString();
        StringTokenizer st = new StringTokenizer(result, "\n");
        int count = 0;
        while (st.hasMoreTokens()) {
            if (count++ > 10) {
                try {
                    writer.write(st.nextToken()+"\n");
                } catch (IOException ex) {
                    System.out.println("Error writing to file");
                }
            } else {
                st.nextToken();
            }
        }
        try {
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error closing file");
        }

    }

    String nearestK(Instances training, Instance example, double[] testMean, double[] testStd, int k) {
        SortedSet<Map.Entry<Integer, Double>> nearestneighbors = new TreeSet<Map.Entry<Integer, Double>>(
                new Comparator<Map.Entry<Integer, Double>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Double> c,
                            Map.Entry<Integer, Double> e2) {
                        return c.getValue().compareTo(e2.getValue());
                    }
                });
        SortedMap<Integer, Double> neighbors = new TreeMap<Integer, Double>();
        double[] mean = new double[training.numAttributes()];
        double[] std = new double[training.numAttributes()];
        for (int i = 0; i < training.numAttributes(); i++) {
            for (int j = 0; j < training.numInstances(); j++) {
                mean[i] += training.instance(j).value(i);
            }
            mean[i] /= training.numInstances();
        }
        for (int i = 0; i < training.numAttributes(); i++) {
            for (int j = 0; j < training.numInstances(); j++) {
                std[i] += (training.instance(j).value(i) - mean[i]) * (training.instance(j).value(i) - mean[i]);
            }
            std[i] /= training.numInstances() - 1;
            std[i] = Math.sqrt(std[i]);
        }
        for (int i = 0; i < training.numInstances(); i++) {
            Instance train = training.instance(i);
            double sum = 0;
            for (int j = 0; j < train.numAttributes() - 1; j++) {
                sum += (((train.value(j) - mean[j]) / std[j]) - ((example.value(j) - testMean[j]) / testStd[j])) * (((train.value(j) - mean[j]) / std[j]) - ((example.value(j) - testMean[j]) / testStd[j]));
            }
            neighbors.put(i, Math.sqrt(sum));
        }
        nearestneighbors.addAll(neighbors.entrySet());
        Iterator it = nearestneighbors.iterator();
        SortedSet<Map.Entry<String, Double>> likelyclass = new TreeSet<Map.Entry<String, Double>>(
                new Comparator<Map.Entry<String, Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> c,
                            Map.Entry<String, Double> e2) {
                        return e2.getValue().compareTo(c.getValue());
                    }
                });
        SortedMap<String, Double> kNeighbors = new TreeMap<String,Double>();

        for (int i = k; i > 0; i--) {
            Entry<Integer, Double> e = (Entry<Integer, Double>) it.next();
            String label = training.instance(e.getKey()).stringValue(training.numAttributes() - 1);
            if (kNeighbors.containsKey(label)) {
                kNeighbors.put(label, kNeighbors.get(label) + 1);
            } else {
                kNeighbors.put(label, 1.0);
            }
        }
        likelyclass.addAll(kNeighbors.entrySet());
        Entry<String, Double> e = likelyclass.first();
        Iterator it1 = likelyclass.iterator();
        ArrayList<String> classes = new ArrayList<String>();
        classes.add(e.getKey());
        while (it1.hasNext()) {
            Entry<String, Double> e1 = (Entry<String, Double>) it1.next();
            if (e.getKey().equals(e1.getKey())) {
                continue;
            }
            if (e.getValue().equals(e1.getValue())) {
                classes.add(e1.getKey());
            }
        }
        Iterator it2 = nearestneighbors.iterator();
        double[] totaldistances = new double[classes.size()];
        for (int j = 0; j < totaldistances.length; j++) {
            for (int i = k; i > 0; i--) {
                Entry<Integer, Double> e1 = (Entry<Integer, Double>) it2.next();
                if (classes.get(j).equals(training.instance(e1.getKey()).stringValue(training.numAttributes() - 1))) {
                    totaldistances[j] += e1.getValue();
                }
            }
        }
        double[] totaldistances1 = new double[classes.size()];
        System.arraycopy(totaldistances, 0, totaldistances1, 0, totaldistances.length);
        Arrays.sort(totaldistances);
        for (int i = 0; i < totaldistances.length; i++) {
            if (totaldistances[totaldistances.length - 1] == totaldistances1[i]) {
                return classes.get(i);
            }
        }
        return "NULL";
    }
}
