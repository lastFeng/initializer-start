package com.welford.spring.boot.blog.initializerstart.config;

import com.welford.spring.boot.blog.initializerstart.util.Contant;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty({"elasticsearch.ip"})
@ConditionalOnClass(RestClient.class)
public class ElasticsearchConfig {
    @Value("${elasticsearch.ip}")
    private String host;

    @Value("${elasticsearch.connectNum}")
    private Integer connectNum;

    @Value("${elasticsearch.connectPerRoute}")
    private Integer connectPerRoute;

    @Value("${elasticsearch.index.period}")
    private String period;

    @Bean
    public HttpHost[] httpHost() {
        List<HttpHost> hostList = new ArrayList<>();
        String[] addresses = host.split(Contant.COMMA_SPLIT);
        for (String address : addresses) {
            int index = address.lastIndexOf(Contant.COLON_SPLIT);
            hostList.add(new HttpHost(address.substring(0, index), Integer.valueOf(address.substring(index+1))));
        }

        return hostList.toArray(new HttpHost[0]);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public ElasticsearchSpringBootFactory getFactory() {
        return ElasticsearchSpringBootFactory.build(httpHost(), connectNum, connectPerRoute);
    }

    @Bean
    public RestClient getRestClient() {
        return getFactory().getRestClient();
    }

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {
        return getFactory().getRestHighLevelClient();
    }
}
