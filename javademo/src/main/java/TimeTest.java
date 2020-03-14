import sun.security.jca.GetInstance;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * JAVA 8 API 中  时间计算
 * https://www.cnblogs.com/wbxk/p/9598518.html
 */
public class TimeTest {
    public static void main(String[] args) throws InterruptedException {
        LocalDateTime localDateTime=LocalDateTime.now();
        //计算周期性质的时间差值
        long chronoUnit=ChronoUnit.HOURS.between(localDateTime,LocalDateTime.now());
        System.out.println("时间差（纳秒）："+chronoUnit);
        Duration durationOff=Duration.ofHours(1);
        durationOff= durationOff.plusMinutes(50);
        //时间区间  计算时间差
        Duration duration=Duration.between(localDateTime,LocalDateTime.now());
        System.out.println(duration);
        //只会输出整数部分
        System.out.println(durationOff.toHours());
        System.out.println(durationOff);
        //推荐使用
       Instant instant=Instant.now();
        //获取当前 毫秒时间
        System.currentTimeMillis();
        //获取纳秒级别的时间
        System.nanoTime();
    }
}
