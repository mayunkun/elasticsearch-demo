package com.aeert.esdemo.service;

import com.aeert.esdemo.bean.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.List;

/**
 * @Author l'amour solitaire
 * @Description TODO
 * @Date 2020/12/2 下午5:19
 **/
public interface CarService {

    /**
     * 新增、编辑
     **/
    public Car save(Car car);

    /**
     * 新增、编辑
     **/
    public Boolean batchSave(List<Car> carList);

    /**
     * 查询
     **/
    public Car findById(Long id);

    /**
     * 查询
     *
     * @return
     */
    public AggregatedPage page(String name, Integer page, Integer size);

    /**
     * 删除
     **/
    public Boolean remove(Long id);

}
