package xyz.liujin.iplus.elasticsearch.query;

import java.util.Map;

/**
 * 严格匹配，影响 score
 */
public class Must {
    private Term term = Term.EMPTY;

    private Bool bool;

    private Map<String, Object> match_phrase;

    // 所有文档得 1.0 score
    private MatchAll matchAll = MatchAll.EMPTY;

    public Must term(Term term) {
        this.term = term;
        return this;
    }

    public Must bool(Bool bool) {
        this.bool = bool;
        return this;
    }

    public Term newTerm() {
        this.term = new Term();
        return term;
    }

    public Must matchAll(MatchAll matchAll) {
        this.matchAll = matchAll;
        return this;
    }

    public MatchAll matchAll() {
        this.matchAll = new MatchAll();
        return matchAll;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
