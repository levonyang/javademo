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
@AllArgsConstructor
@NoArgsConstructor
//Spring注解参数  注册一个组成
@Component
public class SimpleBean {
    public String id;
    public String name;
}
