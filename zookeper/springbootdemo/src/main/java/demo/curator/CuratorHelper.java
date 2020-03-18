package demo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * Author: zhangxuepei
 * Date: 2020/3/18 19:11
 * Content:
 */
public class CuratorHelper {
    // 锁节点
    public static final String CURATOR_LOCK = "/curatorLock";
    // 此demo使用的集群，所以有多个ip和端口
    private static String CONNECT_SERVER = "10.19.154.187:2181,10.19.154.188:2181,10.19.154.189:2181,10.19.154.191:2181,10.19.154.192:2181";
    // session过期时间
    private static int SESSION_TIMEOUT = 3000;
    // 连接超时时间
    private static int CONNECTION_TIMEOUT = 3000;

    public static CuratorFramework curatorFramework(){
        // 连接 ZooKeeper
        CuratorFramework framework1 = CuratorFrameworkFactory.
                newClient(CONNECT_SERVER, SESSION_TIMEOUT, CONNECTION_TIMEOUT, new RetryNTimes(10, 5000));
        framework1.start();
        return framework1;
    }
}
