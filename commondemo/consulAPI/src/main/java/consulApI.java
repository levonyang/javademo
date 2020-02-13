import com.ecwid.consul.v1.ConsulClient;

import java.time.Duration;
import java.time.LocalDateTime;

public class consulApI {
    public static void main(String[] args) {
        LocalDateTime localDateTime= LocalDateTime.now();
        ConsulClient consulClient=new ConsulClient("10.19.154.66", 8500);
        for (int i = 0; i < 10000; i++) {
            consulClient.deleteKVValue("consulClient"+i);
        }
        Duration duration=Duration.between(LocalDateTime.now(),localDateTime);
        System.out.println(duration);
//        //consul 客户端 获取健康的Services。
//        HealthServicesRequest request = HealthServicesRequest.newBuilder()
//                .setTag("EU-West")
//                .setPassing(true)
//                .setQueryParams(QueryParams.DEFAULT)
//                .build();
//        consulClient.getAgentServices();
//        //查询出健康的节点
//        //
//        Response<List<HealthService>> healthServicesRe=consulClient.getHealthServices("server-hi",request);
//
//        //获取健康节点的
//        //
//        List<HealthService> healthServices=healthServicesRe.getValue();
//        for(int i=0;i<healthServices.size();i++){
//            //获取Service 的ID
//            System.out.println(healthServices.get(i).getService().getId());
//        }
//        //查询出所有代理的节点  及所有注册在改注册中心的所有的节点
//        System.out.println( consulClient.getAgentServices().getValue().toString());
    }
}
