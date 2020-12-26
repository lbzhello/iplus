package xyz.liujin.iplus.elasticsearch.query;

public class Query {
    // 查询匹配的文档
    private Bool bool;
    // 用来改变匹配文档得分数
    private Boosting boosting;
    // 所有文档得 1.0 score
    private ConstantScore constantScore;
    // 返回分数最大的从句作为最终的分数
    private DisMax disMax;
}
