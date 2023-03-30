package KALE.trip;

import KALE.struct.Matrix;
import KALE.struct.Triple;
import KALE.struct.TripleSet;
import KALE.utils.MetricMonitor;
import KALE.utils.NegativeTripleGeneration;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class KALETripModel {
    public TripleSet m_TrainingTriples;
    public TripleSet m_ValidateTriples;
    public TripleSet m_TestingTriples;
    public TripleSet m_Triples;
    public Matrix m_Entity_Factor_MatrixE;
    public Matrix m_Relation_Factor_MatrixR;
    public Matrix m_MatrixEGradient;
    public Matrix m_MatrixRGradient;
    public int m_NumRelation;
    public int m_NumEntity;
    public String m_MatrixE_prefix = "";
    public String m_MatrixR_prefix = "";
    public int m_NumFactor = 20;
    public int m_NumMiniBatch = 100;
    public double m_Delta = 0.1;
    public double m_GammaE = 0.01;
    public double m_GammaR = 0.01;
    public int m_NumIteration = 1000;
    public int m_OutputIterSkip = 50;
    DecimalFormat decimalFormat = new DecimalFormat("#.####");

    public KALETripModel() {
    }

    public void Initialization(String strNumRelation, String strNumEntity, String fnTrainingTriples, String fnValidateTriples, String fnTestingTriples) throws Exception {
        this.m_NumRelation = Integer.parseInt(strNumRelation);
        this.m_NumEntity = Integer.parseInt(strNumEntity);
        this.m_MatrixE_prefix = "MatrixE-k" + this.m_NumFactor + "-d" + this.decimalFormat.format(this.m_Delta) + "-ge" + this.decimalFormat.format(this.m_GammaE) + "-gr" + this.decimalFormat.format(this.m_GammaR);
        this.m_MatrixR_prefix = "MatrixR-k" + this.m_NumFactor + "-d" + this.decimalFormat.format(this.m_Delta) + "-ge" + this.decimalFormat.format(this.m_GammaE) + "-gr" + this.decimalFormat.format(this.m_GammaR);
        System.out.println("\nLoading training and validate triples");
        this.m_TrainingTriples = new TripleSet(this.m_NumEntity, this.m_NumRelation);
        this.m_ValidateTriples = new TripleSet(this.m_NumEntity, this.m_NumRelation);
        this.m_Triples = new TripleSet();
        this.m_TrainingTriples.load(fnTrainingTriples);
        this.m_ValidateTriples.subload(fnValidateTriples);
        this.m_Triples.loadStr(fnTrainingTriples);
        this.m_Triples.loadStr(fnValidateTriples);
        this.m_Triples.loadStr(fnTestingTriples);
        System.out.println("Success.");
        System.out.println("\nRandomizing initial matrix E and matrix R");
        this.m_Entity_Factor_MatrixE = new Matrix(this.m_NumEntity, this.m_NumFactor);
        this.m_Entity_Factor_MatrixE.setToRandom();
        this.m_Entity_Factor_MatrixE.normalizeByRow();
        this.m_Relation_Factor_MatrixR = new Matrix(this.m_NumRelation, this.m_NumFactor);
        this.m_Relation_Factor_MatrixR.setToRandom();
        this.m_Relation_Factor_MatrixR.normalizeByRow();
        System.out.println("Success.");
        System.out.println("\nInitializing gradients of matrix E and matrix R");
        this.m_MatrixEGradient = new Matrix(this.m_NumEntity, this.m_NumFactor);
        this.m_MatrixRGradient = new Matrix(this.m_NumRelation, this.m_NumFactor);
        System.out.println("Success.");
    }

    public void TransE_Learn() throws Exception {
        HashMap<Integer, ArrayList<Triple>> lstPosTriples = new HashMap<>();
        HashMap<Integer, ArrayList<Triple>> lstHeadNegTriples = new HashMap<>();
        HashMap<Integer, ArrayList<Triple>> lstTailNegTriples = new HashMap<>();
        String PATHLOG = "log-k" + this.m_NumFactor + "-d" + this.decimalFormat.format(this.m_Delta) + "-ge" + this.decimalFormat.format(this.m_GammaE) + "-gr" + this.decimalFormat.format(this.m_GammaR);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(PATHLOG)), StandardCharsets.UTF_8));
        int iIter = 0;
        writer.write("Complete iteration #" + iIter + ":\n");
        System.out.println("Complete iteration #" + iIter + ":");
        MetricMonitor first_metrics = new MetricMonitor(this.m_ValidateTriples, this.m_Triples.tripleSet(), this.m_Entity_Factor_MatrixE, this.m_Relation_Factor_MatrixR);
        first_metrics.calculateMetrics();
        double dCurrentHits = first_metrics.dHits;
        double dCurrentMRR = first_metrics.dMRR;
        writer.write("------Current MRR:" + dCurrentMRR + "\tCurrent Hits@10:" + dCurrentHits + "\n");
        System.out.print("\n");
        double dBestHits = first_metrics.dHits;
        double dBestMRR = first_metrics.dMRR;
        int iBestIter = 0;
        long startTime = System.currentTimeMillis();

        while(iIter < this.m_NumIteration) {
            this.m_TrainingTriples.randomShuffle();

            for(int iIndex = 0; iIndex < this.m_TrainingTriples.triples(); ++iIndex) {
                Triple PosTriple = this.m_TrainingTriples.get(iIndex);
                NegativeTripleGeneration negTripGen = new NegativeTripleGeneration(PosTriple, this.m_NumEntity, this.m_NumRelation);
                Triple headNegTriple = negTripGen.generateHeadNegTriple();
                Triple tailNegTriple = negTripGen.generateTailNegTriple();
                int iID = iIndex % this.m_NumMiniBatch;
                if (!lstPosTriples.containsKey(iID)) {
                    ArrayList<Triple> tmpPosLst = new ArrayList<>();
                    ArrayList<Triple> tmpHeadNegLst = new ArrayList<>();
                    ArrayList<Triple> tmpTailNegLst = new ArrayList<>();
                    tmpPosLst.add(PosTriple);
                    tmpHeadNegLst.add(headNegTriple);
                    tmpTailNegLst.add(tailNegTriple);
                    lstPosTriples.put(iID, tmpPosLst);
                    lstHeadNegTriples.put(iID, tmpHeadNegLst);
                    lstTailNegTriples.put(iID, tmpTailNegLst);
                } else {
                    lstPosTriples.get(iID).add(PosTriple);
                    lstHeadNegTriples.get(iID).add(headNegTriple);
                    lstTailNegTriples.get(iID).add(tailNegTriple);
                }
            }

            double m_BatchSize = (double)this.m_TrainingTriples.triples() / (double)this.m_NumMiniBatch;

            for(int iID = 0; iID < this.m_NumMiniBatch; ++iID) {
                StochasticUpdate stochasticUpdate = new StochasticUpdate(lstPosTriples.get(iID),
                        lstHeadNegTriples.get(iID), (ArrayList)lstTailNegTriples.get(iID),
                        this.m_Entity_Factor_MatrixE, this.m_Relation_Factor_MatrixR,
                        this.m_MatrixEGradient, this.m_MatrixRGradient,
                        this.m_GammaE, this.m_GammaR / m_BatchSize, this.m_Delta);
                stochasticUpdate.stochasticIteration();
            }

            lstPosTriples = new HashMap<>();
            lstHeadNegTriples = new HashMap<>();
            lstTailNegTriples = new HashMap<>();
            ++iIter;
            if (iIter % this.m_OutputIterSkip == 0) {
                System.out.println("Complete iteration #" + iIter + ":");
                writer.write("Complete iteration #" + iIter + ":\n");
                MetricMonitor metric = new MetricMonitor(this.m_ValidateTriples, this.m_Triples.tripleSet(), this.m_Entity_Factor_MatrixE, this.m_Relation_Factor_MatrixR);
                metric.calculateMetrics();
                dCurrentHits = metric.dHits;
                dCurrentMRR = metric.dMRR;
                writer.write("------Current MRR:" + dCurrentMRR + "\tCurrent Hits@10:" + dCurrentHits + "\n");
                if (dCurrentMRR > dBestMRR) {
                    this.m_Relation_Factor_MatrixR.output(this.m_MatrixR_prefix + ".best");
                    this.m_Entity_Factor_MatrixE.output(this.m_MatrixE_prefix + ".best");
                    dBestHits = dCurrentHits;
                    dBestMRR = dCurrentMRR;
                    iBestIter = iIter;
                }

                writer.write("------Best iteration #" + iBestIter + "\t" + dBestMRR + "\t" + dBestHits + "\n");
                writer.flush();
                System.out.println("------\tBest iteration #" + iBestIter + "\tBest MRR:" + dBestMRR + "Best \tHits@10:" + dBestHits);
                writer.flush();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("All running time:" + (endTime - startTime) + "ms");
        writer.close();
    }
}
