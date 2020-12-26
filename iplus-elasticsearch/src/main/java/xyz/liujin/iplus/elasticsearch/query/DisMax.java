package xyz.liujin.iplus.elasticsearch.query;

import java.util.List;

/**
 * disjunction max query
 * 包装多个查询从句，返回匹配从句中分数最大的作为最终的分数
 * score = maxScore(queries) + otherScore * tieBreaker
 */
public class DisMax {
    // 必须；返回的文档必须匹配，其中一个或多个 query 从句；分数最高的从句的分数作为最终的分数
    private List<Query> queries;
    // 可选；其他匹配的文档分数 * tieBreaker；默认 0.0
    private float tieBreaker;
}
