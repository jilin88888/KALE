package KALE.utils;
import KALE.struct.Rule;
import KALE.struct.Triple;
public class NegativeRuleGeneration {
    public Rule PositiveRule;
    public int iNumberOfRelations;

    public NegativeRuleGeneration(Rule inPositiveRule, int inNumberOfRelations) {
        this.PositiveRule = inPositiveRule;
        this.iNumberOfRelations = inNumberOfRelations;
    }

    public Rule generateSndNegRule() throws Exception {
        Triple fstTriple;
        int iTrdHead;
        int iTrdTail;
        int iTrdRelation;
        int iNegRelation;
        if (this.PositiveRule.trdTriple() == null) {
            fstTriple = this.PositiveRule.fstTriple();
            int iSndHead = this.PositiveRule.sndTriple().head();
            iTrdHead = this.PositiveRule.sndTriple().tail();
            iTrdTail = this.PositiveRule.sndTriple().relation();
            iTrdRelation = this.PositiveRule.fstTriple().relation();
            iNegRelation = iTrdTail;

            Triple sndTriple;
            for(sndTriple = new Triple(iSndHead, iTrdHead, iTrdTail); iNegRelation == iTrdTail || iNegRelation == iTrdRelation; sndTriple = new Triple(iSndHead, iTrdHead, iNegRelation)) {
                iNegRelation = (int)(Math.random() * (double)this.iNumberOfRelations);
            }

            return new Rule(fstTriple, sndTriple);
        } else {
            fstTriple = this.PositiveRule.fstTriple();
            Triple sndTriple = this.PositiveRule.sndTriple();
            iTrdHead = this.PositiveRule.trdTriple().head();
            iTrdTail = this.PositiveRule.trdTriple().tail();
            iTrdRelation = this.PositiveRule.trdTriple().relation();
            iNegRelation = this.PositiveRule.fstTriple().relation();
            int iSndRelation = this.PositiveRule.sndTriple().relation();
            // iNegRelation = iTrdRelation;

            Triple trdTriple;
            for(trdTriple = new Triple(iTrdHead, iTrdTail, iTrdRelation); iNegRelation == iTrdRelation || iNegRelation == iSndRelation || iNegRelation == iNegRelation; trdTriple = new Triple(iTrdHead, iTrdTail, iNegRelation)) {
                iNegRelation = (int)(Math.random() * (double)this.iNumberOfRelations);
            }

            return new Rule(fstTriple, sndTriple, trdTriple);
        }
    }

    public Rule generateFstNegRule() throws Exception {
        Triple sndTriple = this.PositiveRule.sndTriple();
        int ifstHead = this.PositiveRule.fstTriple().head();
        int ifstTail = this.PositiveRule.fstTriple().tail();
        int iFstRelation = this.PositiveRule.fstTriple().relation();
        int iSndRelation = this.PositiveRule.sndTriple().relation();
        int iNegRelation = iFstRelation;

        Triple fstTriple;
        for(fstTriple = new Triple(ifstHead, ifstTail, iFstRelation); iNegRelation == iSndRelation || iNegRelation == iFstRelation; fstTriple = new Triple(ifstHead, ifstTail, iNegRelation)) {
            iNegRelation = (int)(Math.random() * (double)this.iNumberOfRelations);
        }

        return new Rule(fstTriple, sndTriple);
    }
}
