package com.welford.spring.boot.blog.initializerstart.utils.elasticsearch;

import com.mysql.cj.util.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//@Configuration
public class ElasticsearchService {
    @Autowired
    private RestClient restClient;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Value("${elasticsearch.index.period}")
    private String period;

    /**
     * 创建索引
     * */
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    public boolean isExistIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /***
     * 计数查询
     * @param indices
     * @param filterParams
     * @param timeRange
     * @return
     * @throws IOException
     */
    public long count(String[] indices, Map<String, Object[]> filterParams, Map<String, String[]> timeRange) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        setTimeRange(boolBuilder, timeRange);
        setFilterParams(boolBuilder, filterParams);
        sourceBuilder.query(boolBuilder);

        SearchRequest request = new SearchRequest(indices);
        request.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return response.getHits().getTotalHits().value;
    }

    public long count(String[] indices) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        SearchRequest request = new SearchRequest(indices);
        request.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return response.getHits().getTotalHits().value;
    }

    /***
     * 去重计数查询
     * @return
     */
    public long disCount(String[] indices, String disField, Map<String, Object[]> filterParams,
                         Map<String, String[]> timeRange) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        if (StringUtils.isNullOrEmpty(disField)) {
            return 0L;
        } else {
            CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality(disField).field(disField);
            sourceBuilder.aggregation(aggregationBuilder);
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        setFilterParams(boolQueryBuilder, filterParams);
        setTimeRange(boolQueryBuilder, timeRange);
        sourceBuilder.query(boolQueryBuilder);

        SearchRequest request = new SearchRequest(indices);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        if (aggregations != null) {
            ParsedCardinality aggregation = aggregations.get(disField);
            return aggregation.getValue();
        }

        return 0L;
    }

    private void setFilterParams(BoolQueryBuilder boolBuilder, Map<String, Object[]> filterParams) {
        if (filterParams != null && filterParams.size() > 0) {
            filterParams.forEach((field, params) -> {
                boolBuilder.filter(QueryBuilders.termsQuery(field, params));
            });
        }
    }

    private void setTimeRange(BoolQueryBuilder boolBuilder, Map<String, String[]> timeRange) {
        if (timeRange != null && timeRange.size() > 0) {
            timeRange.forEach((dateField, range) -> {
                if (range != null && range.length == 2) {
                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(dateField);
                    if (range[0] != null) {
                        rangeQueryBuilder.from(range[0]);
                    }
                    if (range[1] != null) {
                        rangeQueryBuilder.to(range[1]);
                    }
                    boolBuilder.filter(rangeQueryBuilder);
                }
            });
        }
    }

    public List<Map<String, Object>> queryDocsByIds(String[] indices, String[] ids, Map<String, SortOrder> sortParams,
                                                    int size) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);

        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(ids);
        sourceBuilder.query(queryBuilder);

        if (sortParams != null) {
            sortParams.forEach((field, order) -> sourceBuilder.sort(field, order));
        }

        SearchRequest request = new SearchRequest(indices);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return processQueryResult(response);
    }

    private List<Map<String, Object>> processQueryResult(SearchResponse response) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (response != null) {
            Iterator<SearchHit> iterator = response.getHits().iterator();
            while (iterator.hasNext()) {
                SearchHit hit = iterator.next();
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                sourceMap.put("id", hit.getId());
                sourceMap.put("esIndex", hit.getIndex());
                result.add(sourceMap);
            }
        }
        return result;
    }

    public boolean insertData(String index, String id, String jsonData) throws IOException {
        UpdateRequest request = new UpdateRequest(index, id);
        request.doc(jsonData, XContentType.JSON);
        request.upsert(jsonData, XContentType.JSON);

        // 设置超时时间
        request.timeout(TimeValue.timeValueMinutes(1));
        // 设置请求刷新策略 - 等待返回
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        // 重发
        request.retryOnConflict(3);

        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        return response.getShardInfo().getFailed() > 0 ? false : true;
    }

    public boolean deleteDataById(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        request.timeout(TimeValue.timeValueMinutes(1));
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return response.getShardInfo().getFailed() > 0 ? false : true;
    }

    public boolean deleteIndex(String index) throws IOException {
        DeleteRequest request = new DeleteRequest(index);
        request.timeout(TimeValue.timeValueMinutes(1));
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return response.getShardInfo().getFailed() > 0 ? false : true;
    }
}
