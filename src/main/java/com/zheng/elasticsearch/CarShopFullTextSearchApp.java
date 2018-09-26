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
public class CarShopFullTextSearchApp {
    
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
                .setQuery(QueryBuilders.matchQuery("name", "宝马"))
                .get();
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("======================================");
        response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.termQuery("name.keyword", "宝马320"))
                .get();
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("=======================================");
        response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.prefixQuery("name", "宝"))
                .get();
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("=======================================");
        response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.multiMatchQuery("宝", "name", "brand"))
                .get();
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        
        client.close();
    }
}
