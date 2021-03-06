package cn.kk20.chat.client.handler;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: 标记一个类为消息处理器
 * @Author: Roy
 * @Date: 2020/2/17 16:35
 * @Version: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MsgHandler {

}
