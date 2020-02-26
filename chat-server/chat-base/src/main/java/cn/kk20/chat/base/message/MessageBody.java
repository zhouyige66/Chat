package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.data.BodyData;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Description: 消息体
 * @Author: Roy
 * @Date: 2020/2/16 8:58 下午
 * @Version: v1.0
 */
public class MessageBody implements Serializable {
    /**
     * 说明：为了使用fastjson能正常序列化与反序列化，数据data不能暂不指定为抽象类或接口
     * 使用时候请使用BodyData子类，不要单独设定bodyType，例子：
     */
    private MessageBodyType bodyType;
    private Serializable data;

    public MessageBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(MessageBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

}
