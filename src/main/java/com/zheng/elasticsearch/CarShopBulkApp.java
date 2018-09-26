package com.zheng.elasticsearch;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Author zhenglian
 * @Date 2018/9/24
 */
public class CarShopBulkApp {
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
        BulkRequestBuilder request = client.prepareBulk();
        request.add(client.prepareIndex(index, type, "3")
            .setSource(XContentFactory.jsonBuilder()
                .startObject()
                    .field("brand", "奔驰")
                    .field("name", "奔驰C200")
                    .field("price", 350000)
                    .field("produce_date", "2017-01-05")
                    .field("sale_price", 340000)
                    .field("sale_date", "2017-02-03")
                .endObject()
            ).request()
        )
        .add(client.prepareUpdate(index, type, "1")
            .setDoc(XContentFactory.jsonBuilder()
                .startObject()
                    .field("price", 290000)
                .endObject()
            ).request()
        )
        .add(client.prepareDelete(index, type, "2")
            .request()
        );

        BulkResponse response = request.get();
        for (BulkItemResponse itemResponse : response) {
            if (!itemResponse.isFailed()) {
                System.out.println(itemResponse.getResponse().getResult());
            }
        }

        client.close();
    }
}
