package KALE.utils;
import KALE.struct.Rule;
import KALE.struct.Triple;
public class NegativeTripleGeneration {
    public Triple PositiveTriple;
    public int iNumberOfEntities;
    public int iNumberOfRelation;

    public NegativeTripleGeneration(Triple inPositiveTriple, int inNumberOfEntities, int inNumberOfRelation) {
        this.PositiveTriple = inPositiveTriple;
        this.iNumberOfEntities = inNumberOfEntities;
        this.iNumberOfRelation = inNumberOfRelation;
    }

    public Triple generateHeadNegTriple() throws Exception {
        int iPosHead = this.PositiveTriple.head();
        int iPosTail = this.PositiveTriple.tail();
        int iPosRelation = this.PositiveTriple.relation();
        int iNegHead = iPosHead;

        Triple NegativeTriple;
        for(NegativeTriple = new Triple(iPosHead, iPosRelation, iPosTail); iNegHead == iPosHead; NegativeTriple = new Triple(iNegHead, iPosRelation, iPosTail)) {
            iNegHead = (int)(Math.random() * (double)this.iNumberOfEntities);
        }

        return NegativeTriple;
    }

    public Triple generateTailNegTriple() throws Exception {
        int iPosHead = this.PositiveTriple.head();
        int iPosTail = this.PositiveTriple.tail();
        int iPosRelation = this.PositiveTriple.relation();
        int iNegTail = iPosTail;

        Triple NegativeTriple;
        for(NegativeTriple = new Triple(iPosHead, iPosRelation, iPosTail); iNegTail == iPosTail; NegativeTriple = new Triple(iPosHead, iPosRelation, iNegTail)) {
            iNegTail = (int)(Math.random() * (double)this.iNumberOfEntities);
        }

        return NegativeTriple;
    }

    public Triple generateRelNegTriple() throws Exception {
        int iPosHead = this.PositiveTriple.head();
        int iPosTail = this.PositiveTriple.tail();
        int iPosRelation = this.PositiveTriple.relation();
        int iNegRelation = iPosRelation;

        Triple NegativeTriple;
        for(NegativeTriple = new Triple(iPosHead, iPosRelation, iPosTail); iNegRelation == iPosRelation; NegativeTriple = new Triple(iPosHead, iNegRelation, iPosTail)) {
            iNegRelation = (int)(Math.random() * (double)this.iNumberOfRelation);
        }

        return NegativeTriple;
    }
}
