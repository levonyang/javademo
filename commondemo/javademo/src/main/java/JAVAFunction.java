import java.util.function.Function;

public class JAVAFunction {
    public static void main(String[] args) {
        int res1 = compute1(1, e -> e + 1); // 结果：2
        int res2 = compute2(1, e -> e + 1, e -> e * 3); // 结果：4
        int res3 = compute3(1, e -> e + 1, e -> e * 3); // 结果：4
        System.out.println(res1 + ", " + res2 + ", " + res3);
    }

    public static int compute1(int x, Function<Integer, Integer> function) {
        return function.apply(x);
    }

    public static int compute2(int x, Function<Integer, Integer> after, Function<Integer, Integer> before) {
        // x作为before的参数，其结果再作为after的参数
        return after.compose(before).apply(x);
    }

    public static int compute3(int x, Function<Integer, Integer> after, Function<Integer, Integer> before) {
        // x作为after的参数先执行after的计算，其结果再作为before的参数
        return before.andThen(after).apply(x);
    }
}
