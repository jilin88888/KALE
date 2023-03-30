package KALE.trip;

import KALE.struct.Matrix;
import KALE.struct.Triple;

public class StochasticGradient {
    public Triple PosTriple;
    public Triple NegTriple;
    public Matrix MatrixE;
    public Matrix MatrixR;
    public Matrix MatrixEGradient;
    public Matrix MatrixRGradient;
    public double dDelta;

    public StochasticGradient(Triple inPosTriple, Triple inNegTriple, Matrix inMatrixE, Matrix inMatrixR, Matrix inMatrixEGradient, Matrix inMatrixRGradient, double inDelta) {
        this.PosTriple = inPosTriple;
        this.NegTriple = inNegTriple;
        this.MatrixE = inMatrixE;
        this.MatrixR = inMatrixR;
        this.MatrixEGradient = inMatrixEGradient;
        this.MatrixRGradient = inMatrixRGradient;
        this.dDelta = inDelta;
    }

    public void calculateGradient() throws Exception {
        int iNumberOfFactors = this.MatrixE.columns();
        int iPosHead = this.PosTriple.head();
        int iPosTail = this.PosTriple.tail();
        int iPosRelation = this.PosTriple.relation();
        int iNegHead = this.NegTriple.head();
        int iNegTail = this.NegTriple.tail();
        int iNegRelation = this.NegTriple.relation();
        double dValue = 1.0 / (3.0 * Math.sqrt((double)iNumberOfFactors));
        double dPosPi = 0.0;

        for(int p = 0; p < iNumberOfFactors; ++p) {
            dPosPi -= Math.abs(this.MatrixE.get(iPosHead, p) + this.MatrixR.get(iPosRelation, p) - this.MatrixE.get(iPosTail, p));
        }

        dPosPi *= dValue;
        ++dPosPi;
        double dNegPi = 0.0;

        int p;
        for(p = 0; p < iNumberOfFactors; ++p) {
            dNegPi -= Math.abs(this.MatrixE.get(iNegHead, p) + this.MatrixR.get(iNegRelation, p) - this.MatrixE.get(iNegTail, p));
        }

        dNegPi *= dValue;
        ++dNegPi;
        if (this.dDelta - dPosPi + dNegPi > 0.0) {
            for(p = 0; p < iNumberOfFactors; ++p) {
                double dPosSgn = 0.0;
                if (this.MatrixE.get(iPosHead, p) + this.MatrixR.get(iPosRelation, p) - this.MatrixE.get(iPosTail, p) > 0.0) {
                    dPosSgn = 1.0;
                } else if (this.MatrixE.get(iPosHead, p) + this.MatrixR.get(iPosRelation, p) - this.MatrixE.get(iPosTail, p) < 0.0) {
                    dPosSgn = -1.0;
                }

                this.MatrixEGradient.add(iPosHead, p, dValue * dPosSgn);
                this.MatrixEGradient.add(iPosTail, p, -1.0 * dValue * dPosSgn);
                this.MatrixRGradient.add(iPosRelation, p, dValue * dPosSgn);
                double dNegSgn = 0.0;
                if (this.MatrixE.get(iNegHead, p) + this.MatrixR.get(iNegRelation, p) - this.MatrixE.get(iNegTail, p) > 0.0) {
                    dNegSgn = 1.0;
                } else if (this.MatrixE.get(iNegHead, p) + this.MatrixR.get(iNegRelation, p) - this.MatrixE.get(iNegTail, p) < 0.0) {
                    dNegSgn = -1.0;
                }

                this.MatrixEGradient.add(iNegHead, p, -1.0 * dValue * dNegSgn);
                this.MatrixEGradient.add(iNegTail, p, dValue * dNegSgn);
                this.MatrixRGradient.add(iNegRelation, p, -1.0 * dValue * dNegSgn);
            }
        }
    }
}