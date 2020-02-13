package spring.study.startup.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用程序开始运行
 */
@Component
public class SimpleApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        //获取程序启动设置参数  args 经过包装
        //在动Web容器之后  初始化Spring参数之前
        System.out.println("===== SimpleApplicationRunner");
    }
}
