package spring.study.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import spring.study.startup.bean.SimpleBean;

/**
 * Spring 应用程序启动
 */
@SpringBootApplication
public class MySpringApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MySpringApplication.class, args);
        SimpleBean sb = applicationContext.getBean("testBean", SimpleBean.class);
        System.out.println("id: " + sb.id + ", name: " + sb.name);
    }
}
