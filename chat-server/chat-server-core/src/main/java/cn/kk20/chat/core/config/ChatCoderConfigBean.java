package cn.kk20.chat.core.config;

import cn.kk20.chat.core.coder.CoderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: 配置类
 * @Author: Roy
 * @Date: 2020/2/20 20:00
 * @Version: v1.0
 */
@Component
public class ChatCoderConfigBean {

    @Value("${chat.coderType}")
    private String coderType;

    public CoderType getCoderType() {
        return CoderType.valueOf(coderType);
    }

}
