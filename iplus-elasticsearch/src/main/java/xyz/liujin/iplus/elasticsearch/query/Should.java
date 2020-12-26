package xyz.liujin.iplus.elasticsearch.query;

/**
 * 表示从句应该出现在文档中
 */
public class Should {
    private Term term;

    public Should term(Term term) {
        this.term = term;
        return this;
    }

    public Term newTerm() {
        this.term = new Term();
        return term;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
