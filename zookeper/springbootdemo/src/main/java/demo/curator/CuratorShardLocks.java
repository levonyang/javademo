package demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Author: zhangxuepei
 * Date: 2020/3/17 19:53
 * Content:
 *   测试 Curator共享锁，互斥
 *   支持重入
 *
 */
public class CuratorShardLocks {
    // 锁节点
    private static final String CURATOR_LOCK = "/curatorLock";
    // 此demo使用的集群，所以有多个ip和端口
    private static String CONNECT_SERVER = "10.19.154.187:2181,10.19.154.188:2181,10.19.154.189:2181,10.19.154.191:2181,10.19.154.192:2181";
    // session过期时间
    private static int SESSION_TIMEOUT = 3000;
    // 连接超时时间
    private static int CONNECTION_TIMEOUT = 3000;
    private static int ticket=10;
    public static void doLock(CuratorFramework cf) {
        System.out.println(Thread.currentThread().getName() + " 尝试获取锁！");
        // 实例化 zk分布式锁 Mutex互斥
        InterProcessMutex mutex = new InterProcessMutex(cf, CURATOR_LOCK);
        try {
            System.out.println("是否获取到锁"+mutex.isAcquiredInThisProcess());
            // 判断是否获取到了zk分布式锁 五秒之内能否获取锁
            if (mutex.acquire(5, TimeUnit.SECONDS)) {
                System.out.println("是否锁住:"+mutex.isAcquiredInThisProcess());
                System.out.println(Thread.currentThread().getName() + " 获取到了锁！-------");
                ticket--;
                // 业务操作
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //首先获取到锁 才进行释放锁
                if(mutex.isAcquiredInThisProcess()){
                    // 释放锁
                    mutex.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        // 定义线程池
        ExecutorService service = Executors.newCachedThreadPool();
        // 定义信号灯，只能允许10个线程并发操作
        final Semaphore semaphore = new Semaphore(10);
        // 模拟10个客户端
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        // 连接 ZooKeeper
                        CuratorFramework framework = CuratorFrameworkFactory.
                                newClient(CONNECT_SERVER, SESSION_TIMEOUT, CONNECTION_TIMEOUT, new RetryNTimes(10, 5000));
                        // 启动
                        framework.start();
                        doLock(framework);
                        semaphore.release();
                    } catch (Exception e) {
                    }
                }
            };
            service.execute(runnable);
        }
        service.shutdown();
        try {
            service.awaitTermination(1000,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("还剩车票数量："+ticket);
    }
}
