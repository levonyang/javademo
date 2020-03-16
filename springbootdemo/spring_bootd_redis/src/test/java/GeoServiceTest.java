import demo.Application;
import demo.projo.CityInfo;
import demo.servicesapi.IGeoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * user:zxp
 * Day:2020,03,08
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GeoServiceTest {
        /** fake some cityInfos */
        private List<CityInfo> cityInfos;

        @Autowired
        private IGeoService geoService;

        @Before
        public void init() {
            cityInfos = new ArrayList<>();
            cityInfos.add(new CityInfo("hefei", 117.17, 31.52));
            cityInfos.add(new CityInfo("anqing", 117.02, 30.31));
            cityInfos.add(new CityInfo("huaibei", 116.47, 33.57));
            cityInfos.add(new CityInfo("suzhou", 116.58, 33.38));
            cityInfos.add(new CityInfo("fuyang", 115.48, 32.54));
            cityInfos.add(new CityInfo("bengbu", 117.21, 32.56));
            cityInfos.add(new CityInfo("huangshan", 118.18, 29.43));
        }

        /**
         * 测试 saveCityInfoToRedis 方法
         * */
        @Test
        public void testSaveCityInfoToRedis() {
            System.out.println(geoService.saveCityInfoToRedis(cityInfos));
        }

        /**
         * 测试 getCityPos 方法
         * 如果传递的 city 在 Redis 中没有记录, 会返回什么呢 ? 例如, 这里传递的 xxx
         * */
        @Test
        public void testGetCityPos() {
            System.out.println(geoService.getCityPos(Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])));
        }

        /**
         * 测试 getTwoCityDistance 方法
         * */
        @Test
        public void testGetTwoCityDistance() {
            System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", null).getValue());
            System.out.println(geoService.getTwoCityDistance("anqing", "suzhou", Metrics.KILOMETERS).getValue());
        }

        /**
         * 测试 getPointRadius 方法
         * */
        @Test
        public void testGetPointRadius() {
            Point center = new Point(cityInfos.get(0).getLongitude(), cityInfos.get(0).getLatitude());
            Distance radius = new Distance(200, Metrics.KILOMETERS);
            Circle within = new Circle(center, radius);

            System.out.println(geoService.getPointRadius(within, null));

            // order by 距离 limit 2, 同时返回距离中心点的距离
            RedisGeoCommands.GeoRadiusCommandArgs args =
                    RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
            System.out.println(geoService.getPointRadius(within, args));
        }

        /**
         * 测试 getMemberRadius 方法
         * */
        @Test
        public void testGetMemberRadius() {
            Distance radius = new Distance(200, Metrics.KILOMETERS);
            System.out.println(geoService.getMemberRadius("suzhou", radius, null));
            // order by 距离 limit 2, 同时返回距离中心点的距离
            RedisGeoCommands.GeoRadiusCommandArgs args =
                    RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
            System.out.println(geoService.getMemberRadius("suzhou", radius, args));
        }

        /**
         * 测试 getCityGeoHash 方法
         * */
        @Test
        public void testGetCityGeoHash() {
            System.out.println(geoService.getCityGeoHash(
                    Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])));
        }
}
