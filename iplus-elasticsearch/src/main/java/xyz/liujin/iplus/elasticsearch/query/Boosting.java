package xyz.liujin.iplus.elasticsearch.query;

/**
 * 用来改变匹配文档得分数
 */
public class Boosting {
    // 必须；文档必须匹配的查询
    private Positive positive;
    // 必须；文档不能匹配的查询，匹配后减分
    private Negative negative;
    // 必须；值在 0-1.0 之间；negative 查询匹配后减少的分数；
    private float negativeBoost;

}
