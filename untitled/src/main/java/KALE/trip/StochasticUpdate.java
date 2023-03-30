package KALE.trip;

import KALE.struct.Matrix;
import KALE.struct.Triple;

import java.util.ArrayList;

public class StochasticUpdate {
    public ArrayList<Triple> lstPosTriples;
    public ArrayList<Triple> lstHeadNegTriples;
    public ArrayList<Triple> lstTailNegTriples;
    public Matrix MatrixE;
    public Matrix MatrixR;
    public Matrix MatrixEGradient;
    public Matrix MatrixRGradient;
    public double dGammaE;
    public double dGammaR;
    public double dDelta;

    public StochasticUpdate(ArrayList<Triple> inLstPosTriples, ArrayList<Triple> inLstHeadNegTriples,
                            ArrayList<Triple> inLstTailNegTriples, Matrix inMatrixE, Matrix inMatrixR,
                            Matrix inMatrixEGradient, Matrix inMatrixRGradient,
                            double inGammaE, double inGammaR, double inDelta) {
        this.lstPosTriples = inLstPosTriples;
        this.lstHeadNegTriples = inLstHeadNegTriples;
        this.lstTailNegTriples = inLstTailNegTriples;
        this.MatrixE = inMatrixE;
        this.MatrixR = inMatrixR;
        this.MatrixEGradient = inMatrixEGradient;
        this.MatrixRGradient = inMatrixRGradient;
        this.dGammaE = inGammaE;
        this.dGammaR = inGammaR;
        this.dDelta = inDelta;
    }

    public void stochasticIteration() throws Exception {
        this.MatrixEGradient.setToValue(0.0);
        this.MatrixRGradient.setToValue(0.0);

        int i;
        for(i = 0; i < this.lstPosTriples.size(); ++i) {
            Triple PosTriple = (Triple)this.lstPosTriples.get(i);
            Triple HeadNegTriple = (Triple)this.lstHeadNegTriples.get(i);
            Triple TailNegTriple = (Triple)this.lstTailNegTriples.get(i);
            StochasticGradient headGradient = new StochasticGradient(PosTriple, HeadNegTriple, this.MatrixE, this.MatrixR, this.MatrixEGradient, this.MatrixRGradient, this.dDelta);
            headGradient.calculateGradient();
            StochasticGradient tailGradient = new StochasticGradient(PosTriple, TailNegTriple, this.MatrixE, this.MatrixR, this.MatrixEGradient, this.MatrixRGradient, this.dDelta);
            tailGradient.calculateGradient();
        }

        int j;
        double dValue;
        for(i = 0; i < this.MatrixE.rows(); ++i) {
            for(j = 0; j < this.MatrixE.columns(); ++j) {
                dValue = this.MatrixEGradient.get(i, j);
                this.MatrixE.add(i, j, -1.0 * this.dGammaE * dValue);
            }
        }

        for(i = 0; i < this.MatrixR.rows(); ++i) {
            for(j = 0; j < this.MatrixR.columns(); ++j) {
                dValue = this.MatrixRGradient.get(i, j);
                this.MatrixR.add(i, j, -1.0 * this.dGammaR * dValue);
            }
        }

        this.MatrixE.normalizeByRow();
        this.MatrixR.normalizeByRow();
    }
}
