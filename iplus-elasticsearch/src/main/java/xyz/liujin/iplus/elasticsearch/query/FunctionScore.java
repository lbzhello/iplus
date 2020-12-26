package xyz.liujin.iplus.elasticsearch.query;

import java.util.List;

public class FunctionScore {
    private Query query;
    private String boost;
    private RandomScore randomScore;
    private List<FunctionScore> functions;
    private int maxBoost;
    /**
     * multiply sum avg first max min
     */
    private String scoreMode;
    /**
     * multiply replace sum avg max min
     */
    private String boostMode;
    private int minScore;
    private int weight;
}
