package cn.kk20.chat.client.processor;

import cn.kk20.chat.base.message.chat.ChatMessageType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
//@ConditionalOnProperty(name = "chat.registerAsServer",havingValue = "false")
public @interface ChatMsgProcessor {

    /**
     * 返回处理器处理的消息类型
     *
     * @return
     */
    ChatMessageType messageType();

}
