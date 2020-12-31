package cn.kk20.chat.base.message.chat.body;

import cn.kk20.chat.base.message.chat.BodyType;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/2/26 09:37
 * @Version: v1.0
 */
public abstract class BodyData implements Serializable {

    @JSONField(serialize = false,deserialize = false)
    public abstract BodyType getMessageBodyType();

}
