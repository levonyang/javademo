package com.example.springbootes.demo.po;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 15:08
 * Content:
 */
public class DataDomainPO implements Serializable {

    @Field(name="domainId",type = FieldType.Text,includeInParent = true)
    private String dataDomainID;

    public String getDataDomainID() {
        return dataDomainID;
    }

    public void setDataDomainID(String dataDomainID) {
        this.dataDomainID = dataDomainID;
    }
}
