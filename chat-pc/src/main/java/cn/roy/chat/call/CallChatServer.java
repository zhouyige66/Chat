package cn.roy.chat.call;

import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 5/25/2020 10:13 AM
 * @Version: v1.0
 */
public interface CallChatServer {

    @PostMapping("/user/login")
    ResultData login(LoginEntity entity);

}
