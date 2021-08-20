package xyz.liujin.iplus.elasticsearch.query;

/**
 * 表示一个匹配从句
 */
public class Term extends Leaf {
    public static Term EMPTY = new Term();

    public Term put(String key, String value) {
        super.put(key, value);
        return this;
    }

}
