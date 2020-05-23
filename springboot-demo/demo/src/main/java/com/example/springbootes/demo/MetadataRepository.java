package com.example.springbootes.demo;

import com.example.springbootes.demo.po.MetaDataPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 10:13
 * Content:
 */
//@Repository
public interface MetadataRepository extends ElasticsearchRepository<MetaDataPO,String> {
   List<MetaDataPO> findByDataDomainPO_DataDomainID(String ID);
}
