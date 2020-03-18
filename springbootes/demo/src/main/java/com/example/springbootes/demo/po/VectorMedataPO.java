package com.example.springbootes.demo.po;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 15:12
 * Content:
 */
public class VectorMedataPO {
    @Field(type = FieldType.Text)
    private String tableName;
    @Field(type = FieldType.Text)
    private String geometryType;
    @Field(type = FieldType.Text)
    private String srid;
    @Field(type = FieldType.Text)
    private String dataDamonID;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public String getDataDamonID() {
        return dataDamonID;
    }

    public void setDataDamonID(String dataDamonID) {
        this.dataDamonID = dataDamonID;
    }
}
