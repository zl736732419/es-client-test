package com.zheng.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhenglian
 * @Date 2018/9/26
 */
public class CarShopTemplateApp {
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

        Map<String, Object> params = new HashMap<>();
        params.put("from", 0);
        params.put("size", 10);
        params.put("brand", "宝马");

        StringBuilder builder = new StringBuilder();
        builder.append(
                "{\n" +
                "  \"from\": \"{{from}}\",\n" +
                "  \"size\": \"{{size}}\",\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"brand.keyword\": \"{{brand}}\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        
        SearchResponse response = new SearchTemplateRequestBuilder(client)
//                .setScript("search_sales_keyword")
//                .setScriptType(ScriptType.STORED)
                
                .setScriptType(ScriptType.INLINE)
                .setScript(builder.toString())
                
                .setScriptParams(params)
                .setRequest(client.prepareSearch(index).request())
                .get()
                .getResponse();

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        
        client.close();
    }
}
