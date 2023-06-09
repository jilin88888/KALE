package KALE.joint;

import KALE.utils.Arguments;

public class KALEProgram {
    public static void main(String[] args) throws Exception {
        Arguments cmmdArg = new Arguments(args);
        KALEJointModel transe = new KALEJointModel();
        String fnTrainingTriples = "";
        String fnValidateTriples = "";
        String fnTestingTriples = "";
        String fnTrainingRules = "";

        String strNumRelation = "";
        String strNumEntity = "";

        fnTrainingTriples = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\train.txt";
        fnValidateTriples = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\valid.txt";
        fnTestingTriples = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\test.txt";
        fnTrainingRules = "D:\\Program Files\\JavaProject\\KALE\\untitled\\src\\main\\java\\KALE\\datasets\\wn18\\groundings_filter.txt";
        strNumRelation = "18";
        strNumEntity = "40943";
        transe.m_Weight = 0.1;
        transe.m_NumFactor = 50;
        transe.m_Delta = 0.2;
        transe.m_GammaE = 0.1;
        transe.m_GammaR = 0.1;
        transe.m_NumIteration = 1000;
        transe.m_OutputIterSkip = 50;


        long startTime = System.currentTimeMillis();
        transe.Initialization(strNumRelation, strNumEntity,
                fnTrainingTriples, fnValidateTriples, fnTestingTriples,
                fnTrainingRules);
        System.out.println("\nStart learning KALE-Joint model (triples + rules)");
        transe.TransE_Learn();
        System.out.println("Success.");
        long endTime = System.currentTimeMillis();
        System.out.println("run time:" + (endTime - startTime) + "ms");

        //        try {
//            fnTrainingTriples = cmmdArg.getValue("train");
//            if (fnTrainingTriples == null || fnTrainingTriples.equals("")) {
//                Usage();
//                return;
//            }
//            fnValidateTriples = cmmdArg.getValue("valid");
//            if (fnValidateTriples == null || fnValidateTriples.equals("")) {
//                Usage();
//                return;
//            }
//            fnTestingTriples = cmmdArg.getValue("test");
//            if (fnTestingTriples == null || fnTestingTriples.equals("")) {
//                Usage();
//                return;
//            }
//            fnTrainingRules = cmmdArg.getValue("rule");
//            if (fnTrainingRules == null || fnTrainingRules.equals("")) {
//                Usage();
//                return;
//            }
//            strNumRelation = cmmdArg.getValue("m");
//            if (strNumRelation == null || strNumRelation.equals("")) {
//                Usage();
//                return;
//            }
//            strNumEntity = cmmdArg.getValue("n");
//            if (strNumEntity == null || strNumEntity.equals("")) {
//                Usage();
//                return;
//            }
//            if (cmmdArg.getValue("w") != null && !cmmdArg.getValue("w").equals("")) {
//                transe.m_Weight = Double.parseDouble(cmmdArg.getValue("w"));
//            }
//            if (cmmdArg.getValue("k") != null && !cmmdArg.getValue("k").equals("")) {
//                transe.m_NumFactor = Integer.parseInt(cmmdArg.getValue("k"));
//            }
//            if (cmmdArg.getValue("d") != null && !cmmdArg.getValue("d").equals("")) {
//                transe.m_Delta = Double.parseDouble(cmmdArg.getValue("d"));
//            }
//            if (cmmdArg.getValue("ge") != null && !cmmdArg.getValue("ge").equals("")) {
//                transe.m_GammaE = Double.parseDouble(cmmdArg.getValue("ge"));
//            }
//            if (cmmdArg.getValue("gr") != null && !cmmdArg.getValue("gr").equals("")) {
//                transe.m_GammaR = Double.parseDouble(cmmdArg.getValue("gr"));
//            }
//            if (cmmdArg.getValue("#") != null && !cmmdArg.getValue("#").equals("")) {
//                transe.m_NumIteration = Integer.parseInt(cmmdArg.getValue("#"));
//            }
//            if (cmmdArg.getValue("skip") != null && !cmmdArg.getValue("skip").equals("")) {
//                transe.m_OutputIterSkip = Integer.parseInt(cmmdArg.getValue("skip"));
//            }
//            long startTime = System.currentTimeMillis();
//            transe.Initialization(strNumRelation, strNumEntity,
//                    fnTrainingTriples, fnValidateTriples, fnTestingTriples,
//                    fnTrainingRules);
//
//            System.out.println("\nStart learning KALE-Joint model (triples + rules)");
//            transe.TransE_Learn();
//            System.out.println("Success.");
//            long endTime = System.currentTimeMillis();
//            System.out.println("run time:" + (endTime-startTime)+"ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Usage();
//            return;
//        }
//    }

//    static void Usage() {
//        System.out.println(
//                "Usage: java KALE.jar -train training_triple_path -valid validate_triple_path -test test_triple_path -rule groundings_path -m number_of_relations -n number_of_entities [options]\n\n"
//                        +
//
//                        "Options: \n"
//                        + "   -w        -> weights for rules  (default 0.01)\n"
//                        + "   -k        -> number of latent factors (default 20)\n"
//                        + "   -d        -> value of the margin (default 0.1)\n"
//                        + "   -ge       -> learning rate of matrix E (default 0.01)\n"
//                        + "   -gr       -> learning rate of tensor R (default 0.01)\n"
//                        + "   -#        -> number of iterations (default 1000)\n"
//                        + "   -skip     -> number of skipped iterations (default 50)\n"
//        );
//    }
    }
}
