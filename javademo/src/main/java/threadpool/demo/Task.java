package threadpool.demo;

import java.util.List;
import java.util.concurrent.Callable;

public class Task implements Callable<String> {
    private String current;
    private List<Integer> listIn;
    public Task(String currentLevel,List<Integer> current){
        this.current=currentLevel;
        listIn=current;
    }
    @Override
    public String call() throws Exception {
        Thread.sleep(listIn.size());
        return current;
    }
}
