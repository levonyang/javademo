import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Stream {
    public static void main(String[] args) {
        List<String> testString= Arrays.asList("abc", "bc", "bc", "efg", "abcd","", "jkl");
        //使用流进行统计单词
        System.out.println("统计总数:"+testString.parallelStream().filter(a -> {
            return a.equals("bc");
        }).count());
        //使用并行流进行过滤
        testString=testString.parallelStream().filter(a->{return a.equals("bc");}).collect(Collectors.toList());
        testString.forEach(System.out::println);
    }
}
