package com.example.springes;

/**
 * @since 3.0
 */

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

public class MyElasticsearchRestTemplate extends ElasticsearchRestTemplate {
    private RestHighLevelClient client;
    static final Integer INDEX_MAX_RESULT_WINDOW = 10_000;
    public MyElasticsearchRestTemplate(RestHighLevelClient client) {
        super(client);
        this.client=client;
    }

    public MyElasticsearchRestTemplate(RestHighLevelClient client, EntityMapper entityMapper) {
        super(client, entityMapper);
        this.client=client;
    }

    public MyElasticsearchRestTemplate(RestHighLevelClient client, ElasticsearchConverter elasticsearchConverter, EntityMapper entityMapper) {
        super(client, elasticsearchConverter, entityMapper);
        this.client=client;
    }

    public MyElasticsearchRestTemplate(RestHighLevelClient client, ResultsMapper resultsMapper) {
        super(client, resultsMapper);
        this.client=client;
    }

    public MyElasticsearchRestTemplate(RestHighLevelClient client, ElasticsearchConverter elasticsearchConverter) {
        super(client, elasticsearchConverter);
        this.client=client;
    }

    public MyElasticsearchRestTemplate(RestHighLevelClient client, ElasticsearchConverter elasticsearchConverter, ResultsMapper resultsMapper) {
        super(client, elasticsearchConverter, resultsMapper);
        this.client=client;
    }
    @Override
    public <T> long count(CriteriaQuery criteriaQuery, Class<T> clazz) {
        QueryBuilder elasticsearchQuery = new CriteriaQueryProcessor().createQueryFromCriteria(criteriaQuery.getCriteria());
        QueryBuilder elasticsearchFilter = new CriteriaFilterProcessor()
                .createFilterFromCriteria(criteriaQuery.getCriteria());

        if (elasticsearchFilter == null) {
            return doCount(prepareCount(criteriaQuery, clazz), elasticsearchQuery);
        } else {
            // filter could not be set into CountRequestBuilder, convert request into search request
            return doCount(prepareSearch(criteriaQuery, clazz), elasticsearchQuery, elasticsearchFilter);
        }
    }
    @Override
    public <T> long count(SearchQuery searchQuery, Class<T> clazz) {
        QueryBuilder elasticsearchQuery = searchQuery.getQuery();
        QueryBuilder elasticsearchFilter = searchQuery.getFilter();

        if (elasticsearchFilter == null) {
            return doCount(prepareCount(searchQuery, clazz), elasticsearchQuery);
        } else {
            // filter could not be set into CountRequestBuilder, convert request into search request
            return doCount(prepareSearch(searchQuery, clazz), elasticsearchQuery, elasticsearchFilter);
        }
    }


    private long doCount(SearchRequest countRequest, QueryBuilder elasticsearchQuery) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (elasticsearchQuery != null) {
            sourceBuilder.query(elasticsearchQuery);
        }
        sourceBuilder.size(0);
        countRequest.source(sourceBuilder);

        try {
            return client.search(countRequest, RequestOptions.DEFAULT).getHits().getTotalHits().value;
        } catch (IOException e) {
            throw new ElasticsearchException("Error while searching for request: " + countRequest.toString(), e);
        }
    }

    private long doCount(SearchRequest searchRequest, QueryBuilder elasticsearchQuery, QueryBuilder elasticsearchFilter) {
        if (elasticsearchQuery != null) {
            searchRequest.source().query(elasticsearchQuery);
        } else {
            searchRequest.source().query(QueryBuilders.matchAllQuery());
        }
        if (elasticsearchFilter != null) {
            searchRequest.source().postFilter(elasticsearchFilter);
        }
        searchRequest.source().size(0);
        SearchResponse response;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchException("Error for search request: " + searchRequest.toString(), e);
        }
        return response.getHits().getTotalHits().value;
    }
    private <T> SearchRequest prepareCount(Query query, Class<T> clazz) {
        String[] indexName = !isEmpty(query.getIndices())
                ? query.getIndices().toArray(new String[query.getIndices().size()])
                : retrieveIndexNameFromPersistentEntity(clazz);
        String[] types = !isEmpty(query.getTypes()) ? query.getTypes().toArray(new String[query.getTypes().size()])
                : retrieveTypeFromPersistentEntity(clazz);

        Assert.notNull(indexName, "No index defined for Query");

        SearchRequest countRequestBuilder = new SearchRequest(indexName);

        if (types != null) {
            countRequestBuilder.types(types);
        }
        return countRequestBuilder;
    }
    private <T> SearchRequest prepareSearch(Query query, Class<T> clazz) {
        setPersistentEntityIndexAndType(query, clazz);
        return prepareSearch(query, Optional.empty(), clazz);
    }

    private <T> SearchRequest prepareSearch(SearchQuery query, Class<T> clazz) {
        setPersistentEntityIndexAndType(query, clazz);
        return prepareSearch(query, Optional.ofNullable(query.getQuery()), clazz);
    }
    private SearchRequest prepareSearch(Query query, Optional<QueryBuilder> builder,
                                        @Nullable Class<?> clazz) {
        Assert.notNull(query.getIndices(), "No index defined for Query");
        Assert.notNull(query.getTypes(), "No type defined for Query");

        int startRecord = 0;
        SearchRequest request = new SearchRequest(toArray(query.getIndices()));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        request.types(toArray(query.getTypes()));
        sourceBuilder.version(true);
        sourceBuilder.trackScores(query.getTrackScores());

        if (builder.isPresent()) {
            sourceBuilder.query(builder.get());
        }

        if (query.getSourceFilter() != null) {
            SourceFilter sourceFilter = query.getSourceFilter();
            sourceBuilder.fetchSource(sourceFilter.getIncludes(), sourceFilter.getExcludes());
        }

        if (query.getPageable().isPaged()) {
            long offset = query.getPageable().getOffset();

            if (offset > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(String.format("Offset must not be more than %d", Integer.MAX_VALUE));
            }

            startRecord = (int) offset;
            sourceBuilder.size(query.getPageable().getPageSize());
        } else {
            startRecord = 0;
            sourceBuilder.size(INDEX_MAX_RESULT_WINDOW);
        }
        sourceBuilder.from(startRecord);

        if (!query.getFields().isEmpty()) {
            sourceBuilder.fetchSource(toArray(query.getFields()), null);
        }

        if (query.getIndicesOptions() != null) {
            request.indicesOptions(query.getIndicesOptions());
        }

        if (query.getSort() != null) {
            prepareSort(query, sourceBuilder, getPersistentEntity(clazz));
        }

        if (query.getMinScore() > 0) {
            sourceBuilder.minScore(query.getMinScore());
        }

        if (query.getPreference() != null) {
            request.preference(query.getPreference());
        }

        if (query.getSearchType() != null) {
            request.searchType(query.getSearchType());
        }

        request.source(sourceBuilder);
        return request;
    }

    @Nullable
    private ElasticsearchPersistentEntity<?> getPersistentEntity(@Nullable Class<?> clazz) {
        return clazz != null ? elasticsearchConverter.getMappingContext().getPersistentEntity(clazz) : null;
    }



    private void prepareSort(Query query, SearchSourceBuilder sourceBuilder,
                             @Nullable ElasticsearchPersistentEntity<?> entity) {

        for (Sort.Order order : query.getSort()) {
            SortOrder sortOrder = order.getDirection().isDescending() ? SortOrder.DESC : SortOrder.ASC;

            if (ScoreSortBuilder.NAME.equals(order.getProperty())) {
                ScoreSortBuilder sort = SortBuilders //
                        .scoreSort() //
                        .order(sortOrder);

                sourceBuilder.sort(sort);
            } else {
                ElasticsearchPersistentProperty property = (entity != null) //
                        ? entity.getPersistentProperty(order.getProperty()) //
                        : null;
                String fieldName = property != null ? property.getFieldName() : order.getProperty();

                FieldSortBuilder sort = SortBuilders //
                        .fieldSort(fieldName) //
                        .order(sortOrder);

                if (order.getNullHandling() == Sort.NullHandling.NULLS_FIRST) {
                    sort.missing("_first");
                } else if (order.getNullHandling() == Sort.NullHandling.NULLS_LAST) {
                    sort.missing("_last");
                }

                sourceBuilder.sort(sort);
            }
        }
    }
    private static String[] toArray(List<String> values) {
        String[] valuesAsArray = new String[values.size()];
        return values.toArray(valuesAsArray);
    }
    private void setPersistentEntityIndexAndType(Query query, Class clazz) {
        if (query.getIndices().isEmpty()) {
            query.addIndices(retrieveIndexNameFromPersistentEntity(clazz));
        }
        if (query.getTypes().isEmpty()) {
            query.addTypes(retrieveTypeFromPersistentEntity(clazz));
        }
    }

    private String[] retrieveIndexNameFromPersistentEntity(Class clazz) {
        if (clazz != null) {
            return new String[] { getPersistentEntityFor(clazz).getIndexName() };
        }
        return null;
    }
    private String[] retrieveTypeFromPersistentEntity(Class clazz) {
        if (clazz != null) {
            return new String[] { getPersistentEntityFor(clazz).getIndexType() };
        }
        return null;
    }
}

