package com.example.springbootes.demo.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 15:20
 * Content:
 */
@Document(indexName = "medata")
public class MetaDataPO {
    @Id
    private String id;
    @Field(name="data_domain",type = FieldType.Nested)
    private DataDomainPO dataDomainPO;
    @Field(name="vector_metadata",type = FieldType.Nested)
    private VectorMedataPO vectorMedataPO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataDomainPO getDataDomainPO() {
        return dataDomainPO;
    }

    public void setDataDomainPO(DataDomainPO dataDomainPO) {
        this.dataDomainPO = dataDomainPO;
    }

    public VectorMedataPO getVectorMedataPO() {
        return vectorMedataPO;
    }

    public void setVectorMedataPO(VectorMedataPO vectorMedataPO) {
        this.vectorMedataPO = vectorMedataPO;
    }
}
