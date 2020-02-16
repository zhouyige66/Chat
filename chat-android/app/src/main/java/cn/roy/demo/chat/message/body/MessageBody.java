package cn.roy.demo.chat.message.body;


import cn.roy.demo.chat.message.MessageBodyTypeEnum;

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
    MessageBodyTypeEnum getMessageType();

}
