package net.digitaltsunami.wheredigo.model;

/**
 * Created by dhagberg on 6/10/17.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "spend-index-v2", type = "spend-type-v2", shards = 1, replicas = 0, refreshInterval = "-1")
/**
 * Data model for a spend transaction.  In addition to the model, the class is annotated to include Elasticsearch
 * indexing instructions.
 */
public class Spend {
    @Id
    public String id;

    @Field(type = FieldType.Date)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public ZonedDateTime transDate;

    @Field(type = FieldType.Double)
    public BigDecimal amount;

    @Field(type = FieldType.Keyword)
    public String category;

    @Field(type = FieldType.Keyword)
    public String subcategory;

    public String note;

    @Field(type = FieldType.Keyword)
    public String vendor;

    @Field(type = FieldType.Keyword)
    public String[] tags;

}
