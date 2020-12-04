package com.aeert.esdemo.bean;

import lombok.Data;
import lombok.experimental.Accessors;
import org.elasticsearch.index.VersionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author l'amour solitaire
 * @Description Car
 * @Date 2020/12/2 下午4:58
 **/
@Data
@Accessors(chain = true)
@Document(indexName = "vehicle", type = "car", shards = 1, replicas = 1, versionType = VersionType.EXTERNAL)
public class Car implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Float)
    private BigDecimal price;

    @Field(type = FieldType.Text, fielddata = true)
    private String[] tags;

    @Version
    private Long version;

}
