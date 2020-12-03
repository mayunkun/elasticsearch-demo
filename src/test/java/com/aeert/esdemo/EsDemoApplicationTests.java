package com.aeert.esdemo;

import com.aeert.esdemo.bean.Car;
import com.aeert.esdemo.service.CarService;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EsDemoApplicationTests {

    private final CarService carService;

    @Test
    void save() {
        Car car = carService.save(new Car().setId(1L).setName("Benz").setPrice(new BigDecimal(3000000)).setTags(new String[]{"SUV", "Respect"}));
        System.out.println(JSONObject.toJSONString(car));
    }

    @Test
    void batchSave() {
        Boolean result = carService.batchSave(
                Arrays.asList(
                        new Car().setId(1L).setName("Benz").setPrice(new BigDecimal(3000000)).setTags(new String[]{"SUV", "Respect"}),
                        new Car().setId(4L).setName("Benz").setPrice(new BigDecimal(400000)).setTags(new String[]{"Sport"}),
                        new Car().setId(5L).setName("Benz SR").setPrice(new BigDecimal(400000)).setTags(new String[]{"Sport", "Respect"}),
                        new Car().setId(2L).setName("Bmw").setPrice(new BigDecimal(240000)).setTags(new String[]{"Sport"}),
                        new Car().setId(3L).setName("Range Rover").setPrice(new BigDecimal(3500000)).setTags(new String[]{"SUV", "Sport"})
                )
        );
        System.out.println(result);
    }

    @Test
    void update() {
        Car car = carService.save(new Car().setId(1L).setName("Benz1"));
        System.out.println(JSONObject.toJSONString(car));
    }

    @Test
    void page() {
        AggregatedPage page = carService.page("Benz", 0, 10);
        ParsedStringTerms parsedStringTerms = (ParsedStringTerms) page.getAggregation("group_by_tag");
        List<? extends Terms.Bucket> bucketList = parsedStringTerms.getBuckets();
        bucketList.stream().forEach(m -> {
            System.out.println(JSONObject.toJSONString(m.getAggregations().getAsMap()));
        });
    }

    @Test
    void remove() {
        carService.remove(1L);
    }

}
