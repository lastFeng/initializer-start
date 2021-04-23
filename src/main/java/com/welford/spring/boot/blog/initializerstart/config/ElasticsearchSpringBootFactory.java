package com.welford.spring.boot.blog.initializerstart.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ElasticsearchSpringBootFactory {
    public static int CONNECT_TIMEOUT_MILLIS = 60000;
    public static int SOCKET_TIMEOUT_MILLIS = 60000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 60000;
    public static int MAC_CONN_PER_ROUTE = 10;
    public static int MAX_CONN_TOTAL = 30;

    private static HttpHost[] HTTP_HOSTS;
    private static ElasticsearchSpringBootFactory elasticsearchSpringBootFactory = new ElasticsearchSpringBootFactory();
    private RestClientBuilder builder;
    private RestClient restClient;
    private RestHighLevelClient restHighLevelClient;

    private ElasticsearchSpringBootFactory(){}

    public static ElasticsearchSpringBootFactory build(HttpHost[] httpHosts,
                                                       Integer maxConnectNum, Integer maxConnectPerRoute) {
        HTTP_HOSTS = httpHosts;
        MAX_CONN_TOTAL = maxConnectNum;
        MAC_CONN_PER_ROUTE = maxConnectPerRoute;
        return elasticsearchSpringBootFactory;
    }

    public static ElasticsearchSpringBootFactory build(HttpHost[] httpHosts, Integer connectTimeOut, Integer socketTimeOut,
                                                       Integer connectRequestTime, Integer maxConnectNum, Integer maxConnectPerRoute) {
        HTTP_HOSTS = httpHosts;
        CONNECT_TIMEOUT_MILLIS = connectTimeOut;
        SOCKET_TIMEOUT_MILLIS = socketTimeOut;
        CONNECTION_REQUEST_TIMEOUT_MILLIS = connectRequestTime;
        MAX_CONN_TOTAL = maxConnectNum;
        MAC_CONN_PER_ROUTE = maxConnectPerRoute;
        return elasticsearchSpringBootFactory;
    }

    public void init() {
        builder = RestClient.builder(HTTP_HOSTS);
        setConnectTimeOutConfig();
        setMultiConnectConfig();
        restClient = builder.build();
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    /**
     * 设置超时
     * */
    public void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(b -> {
            b.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            b.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            b.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return b;
        });
    }

    /***
     * 设置并发连接数
     */
    public void setMultiConnectConfig() {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAC_CONN_PER_ROUTE);
            return httpClientBuilder;
        });
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public RestHighLevelClient getRestHighLevelClient(){
        return restHighLevelClient;
    }

    public void close() {
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (restClient != null) {
            try {
                restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
