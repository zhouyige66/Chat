package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.bean.ChatMessageType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description:
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
