package com.aeert.esdemo.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author l'amour solitaire
 * @Description HeightLightResultMapper
 * @Date 2020/12/7 上午8:45
 **/
public class HeightLightResultMapper implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> clazz, Pageable pageable) {
        long totalHits = searchResponse.getHits().getTotalHits();
        List<T> list = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        if (hits.getHits().length > 0) {
            for (SearchHit searchHit : hits) {
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                T item = JSON.parseObject(searchHit.getSourceAsString(), clazz);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (highlightFields.containsKey(field.getName())) {
                        try {
                            field.set(item, highlightFields.get(field.getName()).fragments()[0].toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                list.add(item);
            }
        }
        return new AggregatedPageImpl<>(list, pageable, totalHits);
    }

    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
        Map<String, Object> map = searchHit.getSourceAsMap();
        return JSON.parseObject(JSONObject.toJSONString(map), aClass);
    }

}
