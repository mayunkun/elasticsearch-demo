package com.aeert.esdemo.service.impl;

import com.aeert.esdemo.bean.Car;
import com.aeert.esdemo.dao.CarRepository;
import com.aeert.esdemo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author l'amour solitaire
 * @Description TODO
 * @Date 2020/12/2 下午5:26
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CarServiceImpl implements CarService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final CarRepository carRepository;

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Boolean batchSave(List<Car> carList) {
        carRepository.saveAll(carList);
        return Boolean.TRUE;
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).get();
    }

    @Override
    public AggregatedPage page(String name, Integer page, Integer size) {
        // 分页
        Pageable pageable = PageRequest.of(page, size);

        // 检索条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));

        // 排序条件
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("price").order(SortOrder.DESC);

        // 聚合条件
        List<BucketOrder> orders = Arrays.asList(BucketOrder.aggregation("avg_price", true));
        TermsAggregationBuilder builder1 = AggregationBuilders.terms("group_by_tag").field("tags").order(orders);
        AvgAggregationBuilder builder2 = AggregationBuilders.avg("avg_price").field("price");
        TermsAggregationBuilder builder = builder1.subAggregation(builder2);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(fieldSortBuilder)
                .addAggregation(builder)
                .withPageable(pageable)
                .build();
        return (AggregatedPage) carRepository.search(query);
    }

    @Override
    public Boolean remove(Long id) {
        carRepository.deleteById(id);
        return Boolean.TRUE;
    }

}
