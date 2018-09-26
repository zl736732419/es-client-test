package com.zheng.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author zhenglian
 * @Date 2018/9/24
 */
public class CarShopScrollApp {
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
                .setScroll(new TimeValue(60000))
                .setQuery(QueryBuilders.termQuery("brand.keyword", "宝马"))
                .setSize(1)
                .get();
        do {
            for (SearchHit hit : response.getHits().getHits()) {
                System.out.println(hit.getSourceAsString());
            }
            String scrollId = response.getScrollId();
            response = client.prepareSearchScroll(scrollId)
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while (response.getHits().getHits().length != 0);

        client.close();
    }
}

