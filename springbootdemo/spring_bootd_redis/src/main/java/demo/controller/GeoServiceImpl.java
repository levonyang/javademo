package demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.projo.CityInfo;
import demo.servicesapi.IGeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * user:zxp
 * Day:2020,03,08
 **/
@Service
public class GeoServiceImpl implements IGeoService {
    private final static Logger log = LoggerFactory.getLogger(GeoServiceImpl.class);
    private final static ObjectMapper objectMapper=new ObjectMapper();
    private final String GEO_KEY = "ah-cities";
    /**
     * redis 客户端
     */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public GeoServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long saveCityInfoToRedis(Collection<CityInfo> cityInfos) {

        try {
            log.info("start to save city info: {}.", objectMapper.writeValueAsString(cityInfos));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        Set<RedisGeoCommands.GeoLocation<String>> locations = new HashSet<>();
        cityInfos.forEach(ci -> locations.add(new RedisGeoCommands.GeoLocation<String>(
                ci.getCity(), new Point(ci.getLongitude(), ci.getLatitude())
        )));

        log.info("done to save city info.");

        return ops.add(GEO_KEY, locations);
    }

    @Override
    public List<Point> getCityPos(String[] cities) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return ops.position(GEO_KEY, cities);
    }

    @Override
    public Distance getTwoCityDistance(String city1, String city2, Metric metric) {
        GeoOperations<String, String> ops = redisTemplate.opsForGeo();
        return metric == null ? ops.distance(GEO_KEY, city1, city2) : ops.distance(GEO_KEY, city1, city2, metric);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getPointRadius(
            Circle within, RedisGeoCommands.GeoRadiusCommandArgs args
    ) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return args == null ? ops.radius(GEO_KEY, within) : ops.radius(GEO_KEY, within, args);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getMemberRadius(
            String member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args
    ) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return args == null ? ops.radius(GEO_KEY, member, distance) : ops.radius(GEO_KEY, member, distance, args);
    }

    @Override
    public List<String> getCityGeoHash(String[] cities) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return ops.hash(GEO_KEY, cities);
    }
}
