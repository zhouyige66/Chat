package cn.kk20.chat.core.message.body;

import cn.kk20.chat.core.message.MessageBodyType;

/**
 * @Description: 消息内容
 * @Author: Roy
 * @Date: 2020/2/15 11:43 上午
 * @Version: v1.0
 */
public interface MessageBody {

    /**
     * 返回消息体类型
     *
     * @return
     */
    MessageBodyType getMessageType();

}
