package com.example.springbootes.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxuepei
 * @since 3.0
 */
@RestController
public class DDDD extends abd{
    @GetMapping("/d")
    public String dadad(){
        return "'dasd";
    }
}
