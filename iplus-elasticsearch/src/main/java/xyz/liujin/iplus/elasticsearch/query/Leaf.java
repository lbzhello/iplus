package xyz.liujin.iplus.elasticsearch.query;

import xyz.liujin.iplus.elasticsearch.query.support.Pair;

public class Leaf extends Pair {
    /**
     * 给 query 命名，用来跟踪哪个 query 匹配了文档
     */
    private String _name;

    public Leaf _name(String _name) {
        this._name = _name;
        return this;
    }
}
