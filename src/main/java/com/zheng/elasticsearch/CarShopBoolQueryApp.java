package com.zheng.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author zhenglian
 * @Date 2018/9/26
 */
public class CarShopBoolQueryApp {
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true)
                .build();

        String host = "192.168.3.202";
        Integer port = 9300;
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        String index = "car_sales";
        String type = "sales";

        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name", "宝马"))
                        .mustNot(QueryBuilders.termQuery("name.keyword", "宝马318"))
                        .should(QueryBuilders.termQuery("produce_date", "2017-01-02"))
                        .filter(QueryBuilders.rangeQuery("price").gte("280000").lt("350000"))
                ).get();

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }

        client.close();
    }
}
