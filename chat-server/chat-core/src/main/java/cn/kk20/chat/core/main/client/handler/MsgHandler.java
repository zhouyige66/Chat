package cn.kk20.chat.core.main.client.handler;

import cn.kk20.chat.core.main.ClientComponent;

import java.lang.annotation.*;

/**
 * @Description: 标记一个类为消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:35
 * @Version: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ClientComponent
public @interface MsgHandler {

}
