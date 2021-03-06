package xyz.liujin.iplus.elasticsearch.query;

/**
 * 严格匹配，在 filter context 中执行，因此不会计算 score
 * @see Must
 */
public class Filter {
    private Term term;

    public Filter term(Term term) {
        this.term = term;
        return this;
    }

    public Term newTerm() {
        this.term = new Term();
        return this.term;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
