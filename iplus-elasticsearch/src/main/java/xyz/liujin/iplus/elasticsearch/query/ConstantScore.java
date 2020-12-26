package xyz.liujin.iplus.elasticsearch.query;

/**
 *  内部包装一个 filter 查询，每个匹配的文档会返回 boost 指定的分数
 *
 * @see MatchAll
 */
public class ConstantScore {
    private Filter filter;
    // filter 匹配文档，返回的 score
    private float boost;
}
