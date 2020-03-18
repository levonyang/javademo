package com.example.springbootes.demo;

import com.example.springbootes.demo.po.DataDomainPO;
import com.example.springbootes.demo.po.MetaDataPO;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 10:41
 * Content:
 */
@RestController
public class TestColl {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    private MetadataRepository metadataRepository;

    @GetMapping("/test")
    public String Test(String indexname) {
        //        RestHighLevelClient restHighLevelClient=new RestHighLevelClient
        //              (RestClient.builder(new HttpHost("10.19.154.149", 9200, "http")));
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexname);
        Boolean dasd = false;
        try {
            dasd = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dasd.toString();
    }

    @GetMapping("/savetest")
    public String Save(String dataDomainID) {
        DataDomainPO dataDomainPO = new DataDomainPO();
        dataDomainPO.setDataDomainID(dataDomainID);
        MetaDataPO metaDataPO = new MetaDataPO();
        metaDataPO.setDataDomainPO(dataDomainPO);
        metadataRepository.save(metaDataPO);
        return "ttttttt";
    }

    @GetMapping("/find")
    public String find(String dataDomainID) {
        List<MetaDataPO> metaDataPOS = metadataRepository.findByDataDomainPO_DataDomainID(dataDomainID);
        return metaDataPOS.size() + "";
    }
}
