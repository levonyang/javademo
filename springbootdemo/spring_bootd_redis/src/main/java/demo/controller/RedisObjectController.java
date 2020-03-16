package demo.controller;




import demo.projo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
/**
 * user:zxp
 * Day:2020,03,08
 **/
@RequestMapping("/redis/object")
@RestController
public class RedisObjectController {
    //处理 Object 到redis的序列化值
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/get/{username}")
    public  Object get(@PathVariable("username") String username){
        return  redisTemplate.opsForValue().get(username);
    }

    @PutMapping("/put")
    public void put(String username,Integer age,Integer id){
        User user= new User();
        user.setId(id);
        user.setAge(age);
        user.setUsername(username);
        redisTemplate.opsForValue().set(username,user);
    }

}
