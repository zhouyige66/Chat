package cn.kk20.chat.core.main;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: server组件
 * @Author: Roy Z
 * @Date: 2020/2/25 09:41
 * @Version: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ConditionalOnProperty(name = "chat.registerAsServer",havingValue = "false")
public @interface ServerComponent {
    
}
