package xyz.liujin.iplus.elasticsearch.query;

public class Compare {
    private Double gte;
    private Double lte;

    public Compare gte(Double gte) {
        this.gte = gte;
        return this;
    }

    public Compare lte(Double lte) {
        this.lte = lte;
        return this;
    }
}
