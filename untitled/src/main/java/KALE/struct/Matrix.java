package KALE.struct;

import KALE.utils.StringSplitter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Matrix {
    private double[][] pData = null;
    private double[][] pSumData = null; // 记录pData对应位置的平方和
    private int iNumberOfRows;
    private int iNumberOfColumns;

    public Matrix() {
    }

    // 构造一个 iRows * iColumns 的 0.0 矩阵
    public Matrix(int iRows, int iColumns) {
        this.pData = new double[iRows][];
        this.pSumData = new double[iRows][];

        for (int i = 0; i < iRows; ++i) {
            this.pData[i] = new double[iColumns];
            this.pSumData[i] = new double[iColumns];

            for (int j = 0; j < iColumns; ++j) {
                this.pData[i][j] = 0.0;
                this.pSumData[i][j] = 0.0;
            }
        }

        this.iNumberOfRows = iRows;
        this.iNumberOfColumns = iColumns;
    }

    public int rows() {
        return this.iNumberOfRows;
    }

    public int columns() {
        return this.iNumberOfColumns;
    }

    // 返回i,j的值，指标错误抛异常
    public double get(int i, int j) throws Exception {
        if (i >= 0 && i < this.iNumberOfRows) {
            if (j >= 0 && j < this.iNumberOfColumns) {
                return this.pData[i][j];
            } else {
                throw new Exception("get error in DenseMatrix: ColumnID out of range");
            }
        } else {
            throw new Exception("get error in DenseMatrix: RowID out of range");
        }
    }


    // 设置i,j的值，指标错误抛异常
    public void set(int i, int j, double dValue) throws Exception {
        if (i >= 0 && i < this.iNumberOfRows) {
            if (j >= 0 && j < this.iNumberOfColumns) {
                this.pData[i][j] = dValue;
            } else {
                throw new Exception("set error in DenseMatrix: ColumnID out of range");
            }
        } else {
            throw new Exception("set error in DenseMatrix: RowID out of range");
        }
    }

    // 把矩阵的所有元素都赋值为dValue
    public void setToValue(double dValue) {
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            for (int j = 0; j < this.iNumberOfColumns; ++j) {
                this.pData[i][j] = dValue;
            }
        }
    }

    // 构造y=2x-1的随机矩阵
    public void setToRandom() {
        Random rd = new Random(19950209L);
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            for (int j = 0; j < this.iNumberOfColumns; ++j) {
                double dValue = rd.nextDouble();
                this.pData[i][j] = 2.0 * dValue - 1.0;
            }
        }
    }

    // 返回pSumData的i,j位置值
    public double getSum(int i, int j) throws Exception {
        if (i >= 0 && i < this.iNumberOfRows) {
            if (j >= 0 && j < this.iNumberOfColumns) {
                return this.pSumData[i][j];
            } else {
                throw new Exception("get error in DenseMatrix: ColumnID out of range");
            }
        } else {
            throw new Exception("get error in DenseMatrix: RowID out of range");
        }
    }

    // 在(i,j)位置+=dValue
    public void add(int i, int j, double dValue) throws Exception {
        if (i >= 0 && i < this.iNumberOfRows) {
            if (j >= 0 && j < this.iNumberOfColumns) {
                double[] var10000 = this.pData[i];
                var10000[j] += dValue;
            } else {
                throw new Exception("add error in DenseMatrix: ColumnID out of range");
            }
        } else {
            throw new Exception("add error in DenseMatrix: RowID out of range");
        }
    }

    // 元素 = 全体元素的平方和开根号的倒数
    public void normalize() {
        double dNorm = 0.0;
        int i;
        int j;
        for (i = 0; i < this.iNumberOfRows; ++i) {
            for (j = 0; j < this.iNumberOfColumns; ++j) {
                dNorm += this.pData[i][j] * this.pData[i][j];
            }
        }

        dNorm = Math.sqrt(dNorm);
        if (dNorm != 0.0) {
            for (i = 0; i < this.iNumberOfRows; ++i) {
                for (j = 0; j < this.iNumberOfColumns; ++j) {
                    double[] var10000 = this.pData[i];
                    var10000[j] /= dNorm;
                }
            }
        }

    }

    // 按行做归一化处理
    public void normalizeByRow() {
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            double dNorm = 0.0;
            int j;
            for (j = 0; j < this.iNumberOfColumns; ++j) {
                dNorm += this.pData[i][j] * this.pData[i][j];
            }
            dNorm = Math.sqrt(dNorm);
            if (dNorm != 0.0) {
                for (j = 0; j < this.iNumberOfColumns; ++j) {
                    double[] var10000 = this.pData[i];
                    var10000[j] /= dNorm;
                }
            }
        }

    }

    // 把数组归一化到0，1区间：Math.min(1.0, 1.0 / dNorm)
    public void rescaleByRow() {
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            double dNorm = 0.0;
            int j;
            for (j = 0; j < this.iNumberOfColumns; ++j) {
                dNorm += this.pData[i][j] * this.pData[i][j];
            }
            dNorm = Math.sqrt(dNorm);
            if (dNorm != 0.0) {
                for (j = 0; j < this.iNumberOfColumns; ++j) {
                    double[] var10000 = this.pData[i];
                    var10000[j] *= Math.min(1.0, 1.0 / dNorm);
                }
            }
        }
    }

    // 按列做归一化处理
    public void normalizeByColumn() {
        for (int j = 0; j < this.iNumberOfColumns; ++j) {
            double dNorm = 0.0;
            int i;
            for (i = 0; i < this.iNumberOfRows; ++i) {
                dNorm += this.pData[i][j] * this.pData[i][j];
            }
            dNorm = Math.sqrt(dNorm);
            if (dNorm != 0.0) {
                for (i = 0; i < this.iNumberOfRows; ++i) {
                    double[] var10000 = this.pData[i];
                    var10000[j] /= dNorm;
                }
            }
        }
    }

    // 把pSumData矩阵的i,j位置赋值为pData矩阵i,j位置的平方和
    public void accumulatedByGrad(int i, int j) throws Exception {
        if (i >= 0 && i < this.iNumberOfRows) {
            if (j >= 0 && j < this.iNumberOfColumns) {
                double[] var10000 = this.pSumData[i];
                var10000[j] += this.pData[i][j] * this.pData[i][j];
            } else {
                throw new Exception("add error in DenseMatrix: ColumnID out of range");
            }
        } else {
            throw new Exception("add error in DenseMatrix: RowID out of range");
        }
    }

    public boolean load(String
                                fnInput) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fnInput)), StandardCharsets.UTF_8));
        String line = "";
        line = reader.readLine();
        String[] first_line = StringSplitter.RemoveEmptyEntries(StringSplitter.split(":; ", line));
        if (this.iNumberOfRows == Integer.parseInt(first_line[1]) && this.iNumberOfColumns == Integer.parseInt(first_line[3])) {
            for (int iRowID = 0; (line = reader.readLine()) != null; ++iRowID) {
                String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter.split("\t ", line));
                if (iRowID < 0 || iRowID >= this.iNumberOfRows) {
                    throw new Exception("load error in DenseMatrix: RowID out of range");
                }

                if (tokens.length != this.iNumberOfColumns) {
                    throw new Exception("load error in DenseMatrix: ColumnID out of range");
                }

                for (int iColumnID = 0; iColumnID < tokens.length; ++iColumnID) {
                    this.pData[iRowID][iColumnID] = Double.parseDouble(tokens[iColumnID]);
                }
            }
            reader.close();
            return true;
        } else {
            throw new Exception("load error in DenseMatrix: row/column number incorrect");
        }
    }

    // 把矩阵按行列的格式写出到文件
    public void output(String fnOutput) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(fnOutput)), StandardCharsets.UTF_8));
        writer.write("iNumberOfRows: " + this.iNumberOfRows + "; iNumberOfColumns: " + this.iNumberOfColumns + "\n");
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            writer.write((this.pData[i][0] + " ").trim());
            for (int j = 1; j < this.iNumberOfColumns; ++j) {
                writer.write("\t" + (this.pData[i][j] + " ").trim());
            }
            writer.write("\n");
        }
        writer.close();
    }

    // 把pSum矩阵输出到文件
    public void output_pSum(String fnOutput) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(fnOutput)), StandardCharsets.UTF_8));
        writer.write("iNumberOfRows: " + this.iNumberOfRows + "; iNumberOfColumns: " + this.iNumberOfColumns + "\n");
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            writer.write((this.pSumData[i][0] + " ").trim());
            for (int j = 1; j < this.iNumberOfColumns; ++j) {
                writer.write("\t" + (this.pSumData[i][j] + " ").trim());
            }
            writer.write("\n");
        }
        writer.close();
    }

    // 清空矩阵对象内的全部数据
    public void releaseMemory() {
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            this.pData[i] = null;
        }
        this.pData = null;
        this.iNumberOfRows = 0;
        this.iNumberOfColumns = 0;
    }

    // 矩阵所有元素置0.0
    public void resetToZero() {
        for (int i = 0; i < this.iNumberOfRows; ++i) {
            for (int j = 0; j < this.iNumberOfColumns; ++j) {
                this.pSumData[i][j] = 0.0;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Matrix matrix = new Matrix(15, 10);
        matrix.setToValue(1.2);
        matrix.accumulatedByGrad(1,1);
        matrix.output_pSum("D:\\a3.txt");
    }
}
