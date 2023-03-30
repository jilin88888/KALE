package KALE.struct;

public class Triple {
    private int iHeadEntity;
    private int iRelation;
    private int iTailEntity;


    public Triple() {
    }

    public Triple(int iHeadEntity, int iRelation, int iTailEntity) {
        this.iHeadEntity = iHeadEntity;
        this.iRelation = iRelation;
        this.iTailEntity = iTailEntity;
    }

    public int head() {
        return this.iHeadEntity;
    }

    public int relation() {
        return this.iRelation;
    }

    public int tail() {
        return this.iTailEntity;
    }
}
