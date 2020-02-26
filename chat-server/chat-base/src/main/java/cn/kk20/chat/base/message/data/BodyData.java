package cn.kk20.chat.base.message.data;

import cn.kk20.chat.base.message.MessageBodyType;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/26 09:37
 * @Version: v1.0
 */
public abstract class BodyData implements Serializable {

    @JSONField(serialize = false,deserialize = false)
    public abstract MessageBodyType getMessageBodyType();

}
