package spring.study.startup.listeners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 创建应用程序启动监听事件
 * 创建Spring启动的监听事件 监听应用程序事件
 */
public class SimpleApplicationListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationStartingEvent) {
            //Spring 程序启动事件  这个事件比较早 在所有启动最先完成
            //在启动Web容器和加载配置之前
            System.out.println("===== custom started event in initializer");
        } else if(event instanceof ApplicationReadyEvent) {
            //Spring 程序已经准备就绪
            System.out.println("===== custom ready event in initializer");
        }
    }
}