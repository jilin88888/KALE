package KALE.struct;

public class Rule {
    private Triple FstTriple = null;
    private Triple SndTriple = null;
    private Triple TrdTriple = null;

    public Rule(Triple inFstTriple, Triple inSndTriple) {
        this.FstTriple = inFstTriple;
        this.SndTriple = inSndTriple;
    }

    public Rule(Triple inFstTriple, Triple inSndTriple, Triple inTrdTriple) {
        this.FstTriple = inFstTriple;
        this.SndTriple = inSndTriple;
        this.TrdTriple = inTrdTriple;
    }

    public Triple fstTriple() {
        return this.FstTriple;
    }

    public Triple sndTriple() {
        return this.SndTriple;
    }

    public Triple trdTriple() {
        return this.TrdTriple;
    }
}
