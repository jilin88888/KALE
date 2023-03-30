package KALE.utils;

import java.util.HashMap;

import KALE.struct.Matrix;
import KALE.struct.TripleSet;

public class MetricMonitor {
    public TripleSet lstValidateTriples;
    public HashMap<String, Boolean> lstTriples;
    public Matrix MatrixE;
    public Matrix MatrixR;
    public double dMeanRank;
    public double dMRR;
    public double dHits;

    public MetricMonitor(TripleSet inLstValidateTriples, HashMap<String, Boolean> inlstTriples, Matrix inMatrixE, Matrix inMatrixR) {
        this.lstValidateTriples = inLstValidateTriples;
        this.lstTriples = inlstTriples;
        this.MatrixE = inMatrixE;
        this.MatrixR = inMatrixR;
    }

    public void calculateMetrics() throws Exception {
        int iNumberOfEntities = this.MatrixE.rows();
        int iNumberOfFactors = this.MatrixE.columns();
        int iCnt = 0;
        double avgMeanRank = 0.0;
        double avgMRR = 0.0;
        int avgHits = 0;

        for(int iID = 0; iID < this.lstValidateTriples.triples(); ++iID) {
            int iRelationID = this.lstValidateTriples.get(iID).relation();
            int iSubjectID = this.lstValidateTriples.get(iID).head();
            int iObjectID = this.lstValidateTriples.get(iID).tail();
            double dTargetValue = 0.0;

            int iLeftRank;
            for(iLeftRank = 0; iLeftRank < iNumberOfFactors; ++iLeftRank) {
                dTargetValue -= Math.abs(this.MatrixE.get(iSubjectID, iLeftRank) + this.MatrixR.get(iRelationID, iLeftRank) - this.MatrixE.get(iObjectID, iLeftRank));
            }

            iLeftRank = 1;
            int iLeftIdentical = 0;

            int p;
            for(int iLeftID = 0; iLeftID < iNumberOfEntities; ++iLeftID) {
                double dValue = 0.0;
                String negTiple = iLeftID + "\t" + iRelationID + "\t" + iObjectID;
                if (!this.lstTriples.containsKey(negTiple)) {
                    for(p = 0; p < iNumberOfFactors; ++p) {
                        dValue -= Math.abs(this.MatrixE.get(iLeftID, p) + this.MatrixR.get(iRelationID, p) - this.MatrixE.get(iObjectID, p));
                    }

                    if (dValue > dTargetValue) {
                        ++iLeftRank;
                    }

                    if (dValue == dTargetValue) {
                        ++iLeftIdentical;
                    }
                }
            }

            double dLeftRank = iLeftRank;
            int iLeftHitsAt10 = 0;
            if (dLeftRank <= 10.0) {
                iLeftHitsAt10 = 1;
            }

            avgMeanRank += dLeftRank;
            avgMRR += 1.0 / dLeftRank;
            avgHits += iLeftHitsAt10;
            ++iCnt;
            int iRightRank = 1;

            for(int iRightID = 0; iRightID < iNumberOfEntities; ++iRightID) {
                double dValue = 0.0;
                String negTiple = iSubjectID + "\t" + iRelationID + "\t" + iRightID;
                if (!this.lstTriples.containsKey(negTiple)) {
                    for(p = 0; p < iNumberOfFactors; ++p) {
                        dValue -= Math.abs(this.MatrixE.get(iSubjectID, p) + this.MatrixR.get(iRelationID, p) - this.MatrixE.get(iRightID, p));
                    }

                    if (dValue > dTargetValue) {
                        ++iRightRank;
                    }

                    if (dValue == dTargetValue) {
                        ++p;
                    }
                }
            }

            double dRightRank = iRightRank;
            int iRightHitsAt10 = 0;
            if (dRightRank <= 10.0) {
                iRightHitsAt10 = 1;
            }

            avgMeanRank += dRightRank;
            avgMRR += 1.0 / dRightRank;
            avgHits += iRightHitsAt10;
            ++iCnt;
        }

        this.dMRR = avgMRR / (double)iCnt;
        this.dHits = (double)avgHits / (double)iCnt;
        System.out.println("avgMRR:" + avgMRR + "\t" + "avgHits:" + avgHits);
        System.out.println("MRR:" + this.dMRR + "\t" + "Hits:" + this.dHits);
    }
}
