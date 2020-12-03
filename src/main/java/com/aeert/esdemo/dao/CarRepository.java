package com.aeert.esdemo.dao;

import com.aeert.esdemo.bean.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepository extends ElasticsearchRepository<Car, Long> {
}
