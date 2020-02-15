package ThreadLoacal;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadLocal {
    static ThreadLocal<Integer> threadLocal=new ThreadLocal<>();

    public static void main(String[] args) {
       TestSer testSer=new TestSer();
        TestAbstract testAbstract=testSer;
        ExecutorService threadPool= Executors.newFixedThreadPool(20);
        threadPool.submit(new Runnable() {
            public void run() {
                testSer.setDsad(23);
                System.out.println(testSer.getDsad());
                testAbstract.setDsad(24);
                System.out.println(testSer.getDsad());
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
                    oos.writeObject(testSer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //                if(threadLocal.get()==null){
//                    System.out.println("test");
//                    threadLocal.set(2);
//                }
//                System.out.printf("%-13.3s",threadLocal.get());
            }
        });
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(testAbstract.getDsad());
                testSer.setDsad(12);
                System.out.println(testSer.getDsad());

//                System.out.printf("%-13.3s",threadLocal.get());
//                System.out.println(threadLocal.get());
//                if(threadLocal.get()==null){
//                    System.out.println("test");
//                    threadLocal.set(2);
//                }
            }
        });
        threadPool.shutdownNow();
    }
}
