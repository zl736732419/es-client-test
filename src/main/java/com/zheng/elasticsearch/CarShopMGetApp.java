package com.zheng.elasticsearch;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author zhenglian
 * @Date 2018/9/24
 */
public class CarShopMGetApp {
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

        MultiGetResponse response = client.prepareMultiGet()
                .add(index, type, "1")
                .add(index, type, "2")
                .get();
        
        for (MultiGetItemResponse itemResponse : response) {
            if (!itemResponse.isFailed()) {
                System.out.println(itemResponse.getResponse().getSourceAsString());
            }
        }

        client.close();
    }
}
