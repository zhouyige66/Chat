package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.base.message.ChatMessageType;
import org.springframework.stereotype.Component;

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
@Component
public @interface MsgProcessor {

    /**
     * 返回处理器处理的消息类型
     *
     * @return
     */
    ChatMessageType messageType();

}
