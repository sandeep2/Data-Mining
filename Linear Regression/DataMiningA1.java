


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import Jama.Matrix;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;



public class DataMiningA1 {

    String File1, File2, File3;

    public DataMiningA1(String File1, String File2, String File3) {
        this.File1 = File1;
        this.File2 = File2;
        this.File3 = File3;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        DataMiningA1 l2reg = new DataMiningA1("train-1000-100.csv", "test-1000-100.csv", "q1-1000-100.csv");
        l2reg.sol1(100, 1000, 1000);
        //l2reg.sol1(100, 1000, 1000);

        DataMiningA1 l2reg1 = new DataMiningA1("train-100-100.csv", "test-100-100.csv", "q1-100-100.csv");
        l2reg1.sol1(100, 100, 1000);
        //l2reg1.sol1(100, 100, 1000);

        DataMiningA1 l2reg2 = new DataMiningA1("train-100-10.csv", "test-100-10.csv", "q1-100-10.csv");
        l2reg2.sol1(10, 100, 1000);
        //l2reg2.sol1(10, 100, 1000);

        DataMiningA1 l2reg3 = new DataMiningA1("50(1000)_100_train.csv", "test-1000-100.csv", "q1-50-100-100.csv");
        l2reg3.sol1(100, 50, 1000);
        //l2reg3.sol1(100, 50, 1000);

        DataMiningA1 l2reg4 = new DataMiningA1("100(1000)_100_train.csv", "test-1000-100.csv", "q1-100-100-100.csv");
        l2reg4.sol1(100, 100, 1000);
        //l2reg4.CV(100, 1000, 100);
        DataMiningA1 l2reg5 = new DataMiningA1("150(1000)_100_train.csv", "test-1000-100.csv", "q1-150-100-100.csv");
        //l2reg.sol1(100, 1000, 1000);
        l2reg5.sol1(100, 150, 1000);

        DataMiningA1 l2reg6 = new DataMiningA1("train-1000-100.csv", "test-1000-100.csv", "q2-soln.csv");
        l2reg6.sol2(100, 1000, 1000);
        //l2reg6.sol2(100, 1000, 1000);

        DataMiningA1 l2reg7 = new DataMiningA1("train-1000-100.csv", "test-1000-100.csv", "q3-1000-100.csv");
        l2reg7.CV(1000, 1000, 100);
        //l2reg.sol1(100, 1000, 1000);

        DataMiningA1 l2reg8 = new DataMiningA1("train-100-100.csv", "test-100-100.csv", "q3-100-100.csv");
        l2reg8.CV(100, 1000, 100);
        //l2reg1.sol1(100, 100, 1000);

        DataMiningA1 l2reg9 = new DataMiningA1("train-100-10.csv", "test-100-10.csv", "q3-100-10.csv");
        l2reg9.CV(100, 1000, 10);
        //l2reg2.sol1(10, 100, 1000);

        DataMiningA1 l2reg10 = new DataMiningA1("50(1000)_100_train.csv", "test-1000-100.csv", "q3-50-100-100.csv");
        l2reg10.CV(50, 1000, 100);
        //l2reg3.sol1(100, 50, 1000);

        DataMiningA1 l2reg11 = new DataMiningA1("100(1000)_100_train.csv", "test-1000-100.csv", "q3-100-100-100.csv");
        l2reg11.CV(100, 1000, 100);
        //l2reg4.CV(100, 1000, 100);
        DataMiningA1 l2reg12 = new DataMiningA1("150(1000)_100_train.csv", "test-1000-100.csv", "q3-150-100-10.csv");
        //l2reg.sol1(100, 1000, 1000);
        l2reg12.CV(150, 1000, 100);

    }

    public void sol1(int cols, int rows1, int rows2) {
        Matrix X = new Matrix(rows1, cols + 1);
        Matrix Y = new Matrix(rows1, 1);

        Matrix X1 = new Matrix(rows2, cols + 1);
        Matrix Y1 = new Matrix(rows2, 1);

        FileReader open1 = null;
        try {
            open1 = new FileReader(this.File1);
        } catch (FileNotFoundException ex) {
            System.out.println("File is not found");
        }
        FileReader open2 = null;
        try {
            open2 = new FileReader(this.File2);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
        BufferedReader br1 = new BufferedReader(open1);
        BufferedReader br2 = new BufferedReader(open2);
        String t1;
        int k = -1;
        try {
            while ((t1 = br1.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X.set(k, r++, 1.0);
                        X.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("Unable to read/write");
        }

        k = -1;
        try {
            while ((t1 = br2.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X1.set(k, r++, 1.0);
                        X1.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y1.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X1.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("file unable to read/write");
        }

        FileWriter writer = null;

        try {

            writer = new FileWriter(this.File3);
        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        BufferedWriter wt = new BufferedWriter(writer);
        try {
            wt.write("lambda,error1,error2\n");
        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }
        for (int i = 0; i < 151; i++) {
            Matrix identity = Matrix.identity(cols + 1, cols + 1);
            Matrix w = X.transpose().times(X).plus(identity.timesEquals(i)).inverse().times(X.transpose()).times(Y);
            //System.out.println(this.MSError(X1, Y1, w));

            try {
                wt.write(i + "," + this.MSError(X, Y, w) + "," + this.MSError(X1, Y1, w) + "\n");
            } catch (IOException ex) {
                System.out.println("file unable to read/write.");
            }
        }

        try {
            br1.close();
            open1.close();

            br2.close();
            open2.close();
            wt.close();
            writer.close();

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

    }

    public void sol2(int cols, int rows1, int rows2) {
        Matrix X = new Matrix(rows1, cols + 1);
        Matrix Y = new Matrix(rows1, 1);

        Matrix X1 = new Matrix(rows2, cols + 1);
        Matrix Y1 = new Matrix(rows2, 1);

        FileReader open1 = null;
        try {
            open1 = new FileReader(this.File1);
        } catch (FileNotFoundException ex) {
            System.out.println("file not found.");
        }
        FileReader open2 = null;
        try {
            open2 = new FileReader(this.File2);
        } catch (FileNotFoundException ex) {
           System.out.println("file not found.");
        }
        BufferedReader br1 = new BufferedReader(open1);
        BufferedReader br2 = new BufferedReader(open2);
        String t1;
        int k = -1;
        try {
            while ((t1 = br1.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X.set(k, r++, 1.0);
                        X.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        k = -1;
        try {
            while ((t1 = br2.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X1.set(k, r++, 1.0);
                        X1.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y1.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X1.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        FileWriter writer = null;

        try {

            writer = new FileWriter(this.File3);
        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        BufferedWriter wt = new BufferedWriter(writer);
        try {
            wt.write("sample,lambda,error1,error2\n");
        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }
        int sample = 0;
        for (int i = 0; i < 15; i++) {
            sample = sample + X.getRowDimension() / 15;
            for (int count = 0; count < 3; count++) {
                double error1 = 0, error2 = 0;
                for (int repeat = 0; repeat < 15; repeat++) {
                    Matrix sampleX = new Matrix(sample, cols + 1);
                    Matrix sampleY = new Matrix(sample, 1);
                    int row = 0;

                    for (int j = 0; j < sample; j++) {

                        int rowIndex = this.randInt(0, X.getRowDimension() - 1);
                        for (int column = 0; column < X.getColumnDimension(); column++) {
                            sampleX.set(row, column, X.get(rowIndex, column));
                        }
                        sampleY.set(row, 0, Y.get(rowIndex, 0));
                        row++;
                    }

                    Matrix identity = Matrix.identity(cols + 1, cols + 1);
                    Matrix w = null;
                    switch (count) {
                        case 0: {
                            w = sampleX.transpose().times(sampleX).plus(identity.times(1)).inverse().times(sampleX.transpose()).times(sampleY);
                            error1 += this.MSError(sampleX, sampleY, w);
                            error2 += this.MSError(X1, Y1, w);
                            break;
                        }
                        case 1: {
                            w = sampleX.transpose().times(sampleX).plus(identity.times(25)).inverse().times(sampleX.transpose()).times(sampleY);
                            error1 += this.MSError(sampleX, sampleY, w);
                            error2 += this.MSError(X1, Y1, w);
                            break;
                        }
                        case 2: {
                            w = sampleX.transpose().times(sampleX).plus(identity.times(150)).inverse().times(sampleX.transpose()).times(sampleY);
                            error1 += this.MSError(sampleX, sampleY, w);
                            error2 += this.MSError(X1, Y1, w);
                            break;
                        }
                    }
                }

                error1 = error1 / 15;
                error2 = error2 / 15;
                try {
                    if (count == 0) {
                        wt.write(sample + ",1," + error1 + "," + error2 + "\n");
                    } else if (count == 1) {
                        wt.write(sample + ",25," + error1 + "," + error2 + "\n");
                    } else if (count == 2) {
                        wt.write(sample + ",150," + error1 + "," + error2 + "\n");
                    }
                } catch (IOException ex) {
                    System.out.println("file unable to read/write.");
                }

            }

        }

        try {
            br1.close();
            open1.close();

            br2.close();
            open2.close();
            wt.close();
            writer.close();

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

    }

    public int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public double MSError(Matrix X, Matrix Y, Matrix w) {
        Matrix predicted = X.times(w);
        double sum = 0;
        for (int i = 0; i < Y.getRowDimension(); i++) {
            for (int j = 0; j < Y.getColumnDimension(); j++) {
                sum += (predicted.get(i, j) - Y.get(i, j)) * (predicted.get(i, j) - Y.get(i, j));
            }
        }
        double error = sum / X.getRowDimension();
        return error;
    }

    void CV(int rows1, int rows2, int cols) {
        int folds = 10;
        Matrix X = new Matrix(rows1, cols + 1);
        Matrix Y = new Matrix(rows1, 1);

        Matrix X1 = new Matrix(rows2, cols + 1);
        Matrix Y1 = new Matrix(rows2, 1);

        FileReader open1 = null;
        try {
            open1 = new FileReader(this.File1);
        } catch (FileNotFoundException ex) {
            System.out.println("file unable to read/write.");
        }
        FileReader open2 = null;
        try {
            open2 = new FileReader(this.File2);
        } catch (FileNotFoundException ex) {
            System.out.println("file unable to read/write.");
        }
        BufferedReader br1 = new BufferedReader(open1);
        BufferedReader br2 = new BufferedReader(open2);
        String t1;
        int k = -1;
        try {
            while ((t1 = br1.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X.set(k, r++, 1.0);
                        X.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        k = -1;
        try {
            while ((t1 = br2.readLine()) != null) {
                String[] wordsplit = t1.split(",");

                int r = 0;
                for (int i = 0; i < wordsplit.length; i++) {
                    double p;
                    try {
                        p = Double.parseDouble(wordsplit[i]);
                    } catch (NumberFormatException ve) {

                        continue;
                    }

                    if (i == 0) {
                        r = 0;
                        X1.set(k, r++, 1.0);
                        X1.set(k, r++, p);
                    } else if (i % cols == 0) {
                        Y1.set(k, 0, Double.parseDouble(wordsplit[i]));
                    } else {
                        X1.set(k, r++, p);
                    }

                }
                k++;

            }

        } catch (IOException ex) {
            System.out.println("file unable to read/write.");
        }

        SortedSet<Map.Entry<Integer, Double>> sset = new TreeSet<Map.Entry<Integer, Double>>(
                new Comparator<Map.Entry<Integer, Double>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Double> c,
                            Map.Entry<Integer, Double> e2) {
                        return c.getValue().compareTo(e2.getValue());
                    }
                });
        SortedMap<Integer, Double> blambda = new TreeMap<Integer, Double>();

        for (int l = 1; l < 151; l++) {
            double averageerror = 0;
            for (int j = 0; j < folds; j++) {

                averageerror += this.getCVError(j, X, Y, l);

            }
            averageerror /= folds;
            blambda.put(l, averageerror);
        }
        sset.addAll(blambda.entrySet());
        Entry<Integer, Double> e = sset.first();
        Matrix identity = Matrix.identity(X.getColumnDimension(), X.getColumnDimension());
        Matrix w = X.transpose().times(X).plus(identity.times(e.getKey())).inverse().times(X.transpose()).times(Y);
        int lambda = e.getKey();
        double error1 = this.MSError(X, Y, w);
        double error2 = this.MSError(X1, Y1, w);
        blambda.clear();
        sset.clear();

        for (int l = 1; l < 151; l++) {
            w = X.transpose().times(X).plus(identity.times(l)).inverse().times(X.transpose()).times(Y);
            blambda.put(l, this.MSError(X1, Y1, w));
        }
        sset.addAll(blambda.entrySet());
        Entry<Integer, Double> e1 = sset.first();
        w = X.transpose().times(X).plus(identity.times(e1.getKey())).inverse().times(X.transpose()).times(Y);
        System.out.println("CV Output: Lambda = " + lambda + "\tTrainingError = " + error1 + "\tTestError = " + error2
                + "\nL2Regression Output: Lambda = " + e1.getKey() + "\tTrainingError = " + this.MSError(X, Y, w) + "\tTestError = " + this.MSError(X1, Y1, w));

    }

    double getCVError(int i, Matrix X, Matrix Y, int lambda) {
        Matrix w = null;
        Matrix trainX = new Matrix(X.getRowDimension() - X.getRowDimension() / 10, X.getColumnDimension());
        Matrix trainY = new Matrix(X.getRowDimension() - X.getRowDimension() / 10, 1);
        Matrix testX = new Matrix(X.getRowDimension() / 10, X.getColumnDimension());
        Matrix testY = new Matrix(X.getRowDimension() / 10, 1);
        int testrow = 0, trainrow = 0;
        for (int row = 0; row < X.getRowDimension(); row++) {
            for (int col = 0; col < X.getColumnDimension(); col++) {
                if ((i * X.getRowDimension() / 10) <= row && row < ((i + 1) * X.getRowDimension() / 10)) {
                    testX.set(testrow, col, X.get(row, col));
                } else {
                    trainX.set(trainrow, col, X.get(row, col));
                }

            }
            if ((i * X.getRowDimension() / 10) <= row && row < ((i + 1) * X.getRowDimension() / 10)) {
                testrow++;
            } else {
                trainrow++;
            }
        }

        testrow = 0;
        trainrow = 0;
        for (int row = 0; row < Y.getRowDimension(); row++) {
            for (int col = 0; col < Y.getColumnDimension(); col++) {
                if ((i * Y.getRowDimension() / 10) <= row && row < ((i + 1) * Y.getRowDimension() / 10)) {
                    testY.set(testrow, col, Y.get(row, col));
                } else {
                    trainY.set(trainrow, col, Y.get(row, col));
                }

            }
            if ((i * Y.getRowDimension() / 10) <= row && row < ((i + 1) * Y.getRowDimension() / 10)) {
                testrow++;
            } else {
                trainrow++;
            }
        }
        Matrix identity = Matrix.identity(trainX.getColumnDimension(), trainX.getColumnDimension());
        w = trainX.transpose().times(trainX).plus(identity.times(lambda)).inverse().times(trainX.transpose()).times(trainY);

        return this.MSError(testX, testY, w);

    }
}
