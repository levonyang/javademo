package demo.controller;

import demo.projo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * user:zxp
 * Day:2020,03,08
 **/
@RequestMapping("/redis/hash")
@RestController
public class RedisHashController {
    //处理 Object 到redis的序列化值
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/put")
    public void putHash() {
        //创建两个pojo对象
        User u = new User();
        u.setUsername("张三");
        u.setId(1);
        User u2 = new User();
        u2.setUsername("李四");
        u2.setId(2);
        Map<String, Object> map = new HashMap<>();
        //把list集合存进map中
        map.put("key1", u);
        map.put("key2", u2);
        //将数据写入redis
        redisTemplate.opsForHash().putAll("map1", map);
    }
}
