import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestTimer {
    public static void main(String[] args) throws InterruptedException {
        LocalDateTime localDateTime=LocalDateTime.now();
        long chronoUnit=ChronoUnit.CENTURIES.between(localDateTime,LocalDateTime.now());
        Thread.sleep(1011);
        Duration dsa=Duration.ofHours(1);
        dsa= dsa.plusMinutes(50);
        //时间区间
        Duration duration=Duration.between(localDateTime,LocalDateTime.now());
        //只会输出整数部分
        System.out.println(dsa.toHours());
        System.out.println(dsa);
    }
}
