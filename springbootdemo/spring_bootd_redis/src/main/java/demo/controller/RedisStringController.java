package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;


/**
 * user:zxp
 * Day:2020,03,07
 **/
@RestController
@RequestMapping("/redis")
public class RedisStringController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PutMapping("/string/put")
    public  void  put(String key , @RequestParam(required = false,defaultValue = "default") String value){
        stringRedisTemplate.opsForValue().set(key, value,15, TimeUnit.SECONDS);
        stringRedisTemplate.opsForCluster();
        stringRedisTemplate.opsForHyperLogLog();
            

    }

    @GetMapping("/string/get")
    public  Object get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

}
