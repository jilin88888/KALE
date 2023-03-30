package KALE.struct;

import KALE.utils.StringSplitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class RuleSet {
    private int iNumberOfEntities;
    private int iNumberOfRelations;
    private int iNumberOfRules;
    public ArrayList<Rule> pRule = null;

    public RuleSet() {
    }

    public RuleSet(int iEntities, int iRelations) throws Exception {
        this.iNumberOfEntities = iEntities;
        this.iNumberOfRelations = iRelations;
    }

    public int entities() {
        return this.iNumberOfEntities;
    }

    public int relations() {
        return this.iNumberOfRelations;
    }

    public int rules() {
        return this.iNumberOfRules;
    }

    public Rule get(int iID) throws Exception {
        if (iID >= 0 && iID < this.iNumberOfRules) {
            return this.pRule.get(iID);
        } else {
            throw new Exception("getRule error in RuleSet: ID out of range");
        }
    }

    public void load(String fnInput) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fnInput)), StandardCharsets.UTF_8));
        this.pRule = new ArrayList<>();
        String line = "";

        while ((line = reader.readLine()) != null) {
            String[] tokens = StringSplitter.RemoveEmptyEntries(StringSplitter.split("\t() ", line));
            if (tokens.length != 6 && tokens.length != 9) {
                throw new Exception("load error in RuleSet: data format incorrect");
            }

            int iFstHead = Integer.parseInt(tokens[0]);
            int iFstTail = Integer.parseInt(tokens[2]);
            int iFstRelation = Integer.parseInt(tokens[1]);
            if (iFstHead >= 0 && iFstHead < this.iNumberOfEntities) {
                if (iFstTail >= 0 && iFstTail < this.iNumberOfEntities) {
                    if (iFstRelation >= 0 && iFstRelation < this.iNumberOfRelations) {
                        Triple fstTriple = new Triple(iFstHead, iFstTail, iFstRelation);
                        int iSndHead = Integer.parseInt(tokens[3]);
                        int iSndTail = Integer.parseInt(tokens[5]);
                        int iSndRelation = Integer.parseInt(tokens[4]);
                        if (iSndHead >= 0 && iSndHead < this.iNumberOfEntities) {
                            if (iSndTail >= 0 && iSndTail < this.iNumberOfEntities) {
                                if (iSndRelation >= 0 && iSndRelation < this.iNumberOfRelations) {
                                    Triple sndTriple = new Triple(iSndHead, iSndTail, iSndRelation);
                                    if (tokens.length == 6) {
                                        this.pRule.add(new Rule(fstTriple, sndTriple));
                                        continue;
                                    }

                                    int iTrdHead = Integer.parseInt(tokens[6]);
                                    int iTrdTail = Integer.parseInt(tokens[8]);
                                    int iTrdRelation = Integer.parseInt(tokens[7]);
                                    if (iTrdHead >= 0 && iTrdHead < this.iNumberOfEntities) {
                                        if (iTrdTail >= 0 && iTrdTail < this.iNumberOfEntities) {
                                            if (iTrdRelation >= 0 && iTrdRelation < this.iNumberOfRelations) {
                                                Triple trdTriple = new Triple(iTrdHead, iTrdTail, iTrdRelation);
                                                this.pRule.add(new Rule(fstTriple, sndTriple, trdTriple));
                                                continue;
                                            }

                                            throw new Exception("load error in RuleSet: 3rd relation ID out of range");
                                        }

                                        throw new Exception("load error in RuleSet: 3rd tail entity ID out of range");
                                    }

                                    throw new Exception("load error in RuleSet: 3rd head entity ID out of range");
                                }

                                throw new Exception("load error in RuleSet: 2nd relation ID out of range");
                            }

                            throw new Exception("load error in RuleSet: 2nd tail entity ID out of range");
                        }

                        throw new Exception("load error in RuleSet: 2nd head entity ID out of range");
                    }

                    throw new Exception("load error in RuleSet: 1st relation ID out of range");
                }

                throw new Exception("load error in RuleSet: 1st tail entity ID out of range");
            }

            throw new Exception("load error in RuleSet: 1st head entity ID out of range");
        }

        this.iNumberOfRules = this.pRule.size();
        reader.close();
    }

    public void randomShuffle() {
        TreeMap<Double, Rule> tmpMap = new TreeMap<>();
        int a,b,m,n,s,p,q,t,c;
        for (int iID = 0; iID < this.iNumberOfRules; ++iID) {
            m = this.pRule.get(iID).fstTriple().head();
            n = this.pRule.get(iID).fstTriple().tail();
            s = this.pRule.get(iID).fstTriple().relation();
            Triple fstTriple = new Triple(m, n, s);
            n = this.pRule.get(iID).sndTriple().head();
            s = this.pRule.get(iID).sndTriple().tail();
            t = this.pRule.get(iID).sndTriple().relation();
            Triple sndTriple = new Triple(n, s, t);
            if (this.pRule.get(iID).trdTriple() == null) {
                tmpMap.put(Math.random(), new Rule(fstTriple, sndTriple));
            } else {
                q = this.pRule.get(iID).trdTriple().head();
                t = this.pRule.get(iID).trdTriple().tail();
                c = this.pRule.get(iID).trdTriple().relation();
                Triple trdTriple = new Triple(q, t, c);
                tmpMap.put(Math.random(), new Rule(fstTriple, sndTriple, trdTriple));
            }
        }

        this.pRule = new ArrayList<>();

        for (double dRand : tmpMap.keySet()) {
            Rule rule = tmpMap.get(dRand);
            m = rule.fstTriple().head();
            n = rule.fstTriple().tail();
            s = rule.fstTriple().relation();
            Triple fstTriple = new Triple(m, n, s);
            p = rule.sndTriple().head();
            q = rule.sndTriple().tail();
            t = rule.sndTriple().relation();
            Triple sndTriple = new Triple(p, q, t);
            if (rule.trdTriple() == null) {
                this.pRule.add(new Rule(fstTriple, sndTriple));
            } else {
                a = rule.trdTriple().head();
                b = rule.trdTriple().tail();
                c = rule.trdTriple().relation();
                Triple trdTriple = new Triple(a, b, c);
                this.pRule.add(new Rule(fstTriple, sndTriple, trdTriple));
            }
        }
        this.iNumberOfRules = this.pRule.size();
        tmpMap.clear();
    }
}
