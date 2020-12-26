package xyz.liujin.iplus.elasticsearch;

import xyz.liujin.iplus.elasticsearch.query.Must;

public class ElasticsearchApplication {
    public static void main(String[] args) {
        Must must = new Must();
        must.newTerm().put("user.id", "kimchy");
        System.out.println("hello");
    }
}
