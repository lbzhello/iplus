package xyz.liujin.iplus.elasticsearch.query;

import java.util.List;

/**
 * 组合多个 boolean 从句
 * more-matches-is-better; must 和 should 从句的 score 相加得到最终的 _score
 */
public class Bool {
    private List<Must> must;
    private Filter filter;
    private List<Should> should;
    private MustNot mustNot;
    // 表示 {@link Should} 从句必须匹配的文档数
    private Integer minimumShouldMatch;
    private Double boost;
}
