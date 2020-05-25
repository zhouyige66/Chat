package cn.roy.chat.call;

import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 5/25/2020 10:13 AM
 * @Version: v1.0
 */
@FeignClient(name = "ChatServer",url = "localhost:8082")
public interface CallChatServer {

    @PostMapping("/user/login")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ResultData login(@RequestBody LoginEntity entity);

}
