package xyz.liujin.iplus.elasticsearch.query;

import java.util.List;

public class Terms extends Leaf {
    public static Terms EMPTY = new Terms();


    public Terms put(String key, List<String> value) {
        super.put(key, value);
        return this;
    }

}
