package spring.study.startup.bean;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Created by Format on 2017/5/1.
 */
//Lombok注解参数
@Getter
@Setter
@ToString
//Spring注解参数  注册一个组件
@Component
public class SimpleBean {
    public String id;
    public String name;
    public SimpleBean(){
        System.out.println("我是 Component 注册过来的 SimpleBean 空方法初始化");
    }
    public SimpleBean(String id,String name){
        this.name=name;
        this.id=id;
        System.out.println(String.format("SimpleBean id：{%s}和name：{%s}初始化",id,name));
    }
}
