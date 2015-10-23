/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author hai
 */
public class Kmeans {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Kmeans m = new Kmeans();
        Instances data = m.read();
        Integer[] indices = {775, 1020, 200, 127, 329, 1626, 1515, 651, 658, 328, 1160, 108, 422, 88, 105, 261,
            212, 1941, 1724, 704, 1469, 635, 867, 1187, 445, 222, 1283, 1288, 1766, 1168,
            566, 1812, 214, 53, 423, 50, 705, 1284, 1356, 996, 1084, 1956, 254, 711, 1997,
            1378, 827, 1875, 424, 1790, 633, 208, 1670, 1517, 1902, 1476, 1716, 1709, 264,
            1, 371, 758, 332, 542, 672, 483, 65, 92, 400, 1079, 1281, 145, 1410, 664, 155,
            166, 1900, 1134, 1462, 954, 1818, 1679, 832, 1627, 1760, 1330, 913, 234, 1635,
            1078, 640, 833, 392, 1425, 610, 1353, 1772, 908, 1964, 1260, 784, 520, 1363, 544,
            426, 1146, 987, 612, 1685, 1121, 1740, 287, 1383, 1923, 1665, 19, 1239, 251, 309,
            245, 384, 1306, 786, 1814, 7, 1203, 1068, 1493, 859, 233, 1846, 1119, 469, 1869,
            609, 385, 1182, 1949, 1622, 719, 643, 1692, 1389, 120, 1034, 805, 266, 339, 826,
            530, 1173, 802, 1495, 504, 1241, 427, 1555, 1597, 692, 178, 774, 1623, 1641,
            661, 1242, 1757, 553, 1377, 1419, 306, 1838, 211, 356, 541, 1455, 741, 583, 1464,
            209, 1615, 475, 1903, 555, 1046, 379, 1938, 417, 1747, 342, 1148, 1697, 1785,
            298, 1485, 945, 1097, 207, 857, 1758, 1390, 172, 587, 455, 1690, 1277, 345, 1166,
            1367, 1858, 1427, 1434, 953, 1992, 1140, 137, 64, 1448, 991, 1312, 1628, 167,
            1042, 1887, 1825, 249, 240, 524, 1098, 311, 337, 220, 1913, 727, 1659, 1321, 130,
            1904, 561, 1270, 1250, 613, 152, 1440, 473, 1834, 1387, 1656, 1028, 1106, 829,
            1591, 1699, 1674, 947, 77, 468, 997, 611, 1776, 123, 979, 1471, 1300, 1007, 1443,
            164, 1881, 1935, 280, 442, 1588, 1033, 79, 1686, 854, 257, 1460, 1380, 495, 1701,
            1611, 804, 1609, 975, 1181, 582, 816, 1770, 663, 737, 1810, 523, 1243, 944, 1959,
            78, 675, 135, 1381, 1472};
        m.performKMeans(indices, data);
    }

    void performKMeans(Integer[] indices, Instances data) {
        List<Double> meanNsse;
        data.deleteAttributeAt(2);
        data.deleteAttributeAt(data.numAttributes() - 1);
        for (int k = 1; k < 13; k++) {
            meanNsse = this.getOutput(k, indices, data);
            System.out.println(k + "mean=" + meanNsse.get(0) + ", " + meanNsse.get(1));
        }
    }

    Instances read() {
        BufferedReader reader = null;
        FileReader file = null;
        try {
            file = new FileReader("E:\\Spring2015\\Data Mining\\HW3\\segment.arff");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Kmeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        reader = new BufferedReader(file);
        Instances data = null;
        try {
            data = new Instances(reader);
        } catch (IOException ex) {
            Logger.getLogger(Kmeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Kmeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    ArrayList<Double> getOutput(int k, Integer[] indices, Instances data) {
        List<Double> meanNsse = new ArrayList<Double>();
        int ind = 0;
        Double[][] kCentroids = new Double[k][data.numAttributes()];
        Double[][] updatedKCentroids = new Double[k][data.numAttributes()];
        ArrayList<ArrayList<Integer>> objInClusters = new ArrayList<ArrayList<Integer>>();
        double meanSSE = 0, stdSSE = 0;
        for (int trial = 0; trial < 25; trial++) {
            objInClusters.clear();
            for (int cl = 0; cl < k; cl++) {
                objInClusters.add(new ArrayList<Integer>());
            }
            int j = ind;
            while (j < k + ind) {
                double[] centroidArray = data.instance(indices[j]).toDoubleArray();
                for (int a = 0; a < data.numAttributes(); a++) {
                    updatedKCentroids[j - ind][a] = (centroidArray[a] - data.meanOrMode(a)) / Math.sqrt(data.variance(a));
                }
                j++;
            }
            ind = j;
            int count = 0;
            boolean checkEqual = false;

            int[] instToClusters = new int[data.numInstances()];
            while (count != 49) {
                for (int r = 0; r < updatedKCentroids.length; r++) {
                    kCentroids[r] = updatedKCentroids[r].clone();
                }
                for (int l = 0; l < data.numInstances(); l++) {
                    int Ci = 0;
                    double min = Double.MAX_VALUE;
                    for (int m = 0; m < k; m++) {
                        double error = 0;
                        double[] objArray = data.instance(l).toDoubleArray();
                        for (int a = 0; a < objArray.length - 1; a++) {
                            double nObj = (objArray[a] - data.meanOrMode(a)) / Math.sqrt(data.variance(a));
                            error += (nObj - kCentroids[m][a]) * (nObj - kCentroids[m][a]);
                        }
                        if (error < min) {
                            min = error;
                            Ci = m;
                        }
                    }

                    ArrayList<Integer> objects;
                    if (Ci > 0) {
                        objects = objInClusters.get(Ci - 1);
                    } else {
                        objects = objInClusters.get(Ci);
                    }
                    if (objInClusters.get(instToClusters[l]).contains(new Integer(l))) {
                        objInClusters.get(instToClusters[l]).remove(new Integer(l));
                    }
                    objects.add(new Integer(l));
                    if (instToClusters[l] != Ci + 1) {
                        checkEqual = checkEqual | true;
                    } else {
                        checkEqual = false;
                    }
                    instToClusters[l] = Ci + 1;
                }

                if (checkEqual) {
                    break;
                }
                for (int m = 0; m < k; m++) {
                    ArrayList<Integer> objects = objInClusters.get(m);
                    for (int a = 0; a < data.numAttributes() - 1; a++) {
                        double sum = 0;
                        for (int n = 0; n < objects.size(); n++) {
                            double attValue = (data.instance(objects.get(n)).value(a) - data.meanOrMode(a)) / Math.sqrt(data.variance(a));
                            sum += attValue;
                        }
                        updatedKCentroids[m][a] = sum / objects.size();
                    }
                }

                count++;
            }
            for (int m = 0; m < k; m++) {
                ArrayList<Integer> objects = objInClusters.get(m);
                for (int n = 0; n < objects.size(); n++) {
                    for (int a = 0; a < data.numAttributes(); a++) {
                        double attValue = (data.instance(objects.get(n)).value(a) - data.meanOrMode(a)) / Math.sqrt(data.variance(a));
                        meanSSE += Math.pow(updatedKCentroids[m][a] - attValue, 2);
                    }
                }
            }

            
            stdSSE = 0;
            for (int m = 0; m < k; m++) {
                ArrayList<Integer> objects = objInClusters.get(m);
                for (int n = 0; n < objects.size(); n++) {
                    for (int a = 0; a < data.numAttributes(); a++) {
                        double attValue = (data.instance(objects.get(n)).value(a) - data.meanOrMode(a)) / Math.sqrt(data.variance(a));
                        stdSSE += Math.pow(Math.pow(updatedKCentroids[m][a] - attValue, 2) - meanSSE, 2);
                    }
                }
            }
            stdSSE/=24;
        }
        meanSSE /= 25;
        stdSSE /= 24;
        stdSSE = Math.sqrt(stdSSE);
        meanNsse.add(meanSSE);
        meanNsse.add(stdSSE);
        return (ArrayList<Double>) meanNsse;
    }
}
