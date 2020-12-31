package cn.roy.chat.core.util;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.BodyType;
import cn.kk20.chat.base.message.chat.body.TextData;
import com.alibaba.fastjson.JSON;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/18 16:57
 * @Version: v1.0
 */
public class ChatMessageUtil {

    public static String getMsg(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return "";
        }

        String msg = "";
        final BodyType bodyType = chatMessage.getBodyType();
        switch (bodyType) {
            case TEXT:
                TextData textData = JSON.parseObject(chatMessage.getBody(), TextData.class);
                msg = textData.getText();
                break;
            case HYBRID:
            case LINK:
                msg = "[link]";
                break;
            case IMG:
                msg = "[图片]";
                break;
            case AUDIO:
                msg = "[音频]";
                break;
            case VIDEO:
                msg = "[视频]";
                break;
            case FILE:
                msg = "[文件]";
                break;
            default:
                break;
        }

        return msg;
    }

}
