package com.example.kafkatest.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: zhangxuepei
 * Date: 2020/3/19 19:56
 * Content:
 */
@RestController
public class TestProducer {
    @Autowired
    private UserLogProducer userLogProducer;
    @GetMapping("/kafka")
    public void testKafka() throws JsonProcessingException {
        for (int i = 0; i < 10; i++) {
            userLogProducer.sendLog(String.valueOf(i));
        }
    }
}
