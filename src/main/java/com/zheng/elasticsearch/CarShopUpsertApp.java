package com.zheng.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * upsert操作
 * @Author zhenglian
 * @Date 2018/9/24
 */
public class CarShopUpsertApp {
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true)
                .build();

        String host = "192.168.3.202";
        Integer port = 9300;
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        
        String index = "car_shop";
        String type = "cars";
        String id = "1";
        // insert
        IndexRequest indexRequest = client.prepareIndex(index, type, id)
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                            .field("brand", "宝马")
                            .field("name", "宝马320")
                            .field("price", 320000)
                            .field("produce_date", "2018-09-24")
                        .endObject()
                ).request();
        // update 
        UpdateRequest updateRequest = client.prepareUpdate(index, type, id)
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("price", 310000)
                        .endObject()
                ).request();
        // combine insert and update to upsert
        UpdateRequest upsertRequest = updateRequest.upsert(indexRequest);
        
        UpdateResponse updateResponse = client.update(upsertRequest).get();
        System.out.println(updateResponse.getVersion());
        client.close();
    }
}
