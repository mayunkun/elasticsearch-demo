package com.aeert.esdemo.dao;

import com.aeert.esdemo.bean.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepository extends ElasticsearchRepository<Car, Long> {

    /**
     * @Author l'amour solitaire
     * @Description 同下
     * @Query("{\n" +
     * "    \"match\": {\n" +
     * "      \"name\": \"?0\"\n" +
     * "    }\n" +
     * "  }")
     * @Date 2020/12/4 上午9:48
     **/
    Page<Car> findByName(String name, Pageable page);
}
