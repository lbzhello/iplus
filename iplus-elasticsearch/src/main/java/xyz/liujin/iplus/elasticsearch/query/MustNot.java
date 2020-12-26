package xyz.liujin.iplus.elasticsearch.query;

/**
 * 从句一定不能出现在文档中
 * 在 filter context 中执行，因此不会计算 score

 * @see Filter
 * @see Must
 */
public class MustNot {
    private Range range;
}
