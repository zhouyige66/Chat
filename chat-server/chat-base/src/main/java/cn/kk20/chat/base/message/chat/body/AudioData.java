package cn.kk20.chat.base.message.chat.body;

import cn.kk20.chat.base.message.chat.BodyType;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/13 09:22
 * @Version: v1.0
 */
public class AudioData extends BodyData {
    @Override
    public BodyType getMessageBodyType() {
        return BodyType.AUDIO;
    }
}
