import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Stream {
    public static void main(String[] args) {
        List<String> testString= Arrays.asList("abc", "bc", "bc", "efg", "abcd","", "jkl");
        long dsa=testString.parallelStream().filter(a->{return a.equals("bc");}).count();
        testString=testString.parallelStream().filter(a->{return a.equals("bc");}).collect(Collectors.toList());
        int dsad=2;
        System.out.println(getMin(dsad,1));
    }
    public static <T extends Comparable<T>> T getMin(T a, T b) {
        System.out.println(a.getClass());
        return (a.compareTo(b) < 0) ? a : b;
    }
}
