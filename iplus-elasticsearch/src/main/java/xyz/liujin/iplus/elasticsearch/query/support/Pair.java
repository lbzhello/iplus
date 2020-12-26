package xyz.liujin.iplus.elasticsearch.query.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Pair {
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        if (Objects.isNull(map)) {
            this.map = new HashMap<>();
        }
        return map;
    }

    public Pair put(String key, Object value) {
        getMap().put(key, value);
        return this;
    }
}
