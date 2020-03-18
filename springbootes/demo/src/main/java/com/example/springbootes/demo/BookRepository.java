package com.example.springbootes.demo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 10:13
 * Content:
 */
@Repository
public interface BookRepository extends ElasticsearchRepository<BookStore,String> {
    List<BookStore> findByBook_Title(String title);
}
