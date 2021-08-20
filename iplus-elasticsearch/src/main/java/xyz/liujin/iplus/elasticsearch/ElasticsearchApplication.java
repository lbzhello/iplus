package xyz.liujin.iplus.elasticsearch;

import org.elasticsearch.index.query.QueryBuilders;
import xyz.liujin.iplus.elasticsearch.query.Must;

public class ElasticsearchApplication {
    public static void main(String[] args) {
        QueryBuilders.boolQuery().should(QueryBuilders.matchAllQuery());
        Must must = new Must();
        must.newTerm().put("user.id", "kimchy");
        System.out.println("hello");
    }
}
