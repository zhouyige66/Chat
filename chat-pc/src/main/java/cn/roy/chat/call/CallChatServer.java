package cn.roy.chat.call;

import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: Roy
 * @Date: 5/25/2020 10:13 AM
 * @Version: v1.0
 */
@FeignClient(name = "ChatServer", url = "localhost:9001")
public interface CallChatServer {

    @PostMapping("/auth/login")
    ResultData login(@RequestBody LoginEntity entity);

    @GetMapping("/netty/getHost")
    ResultData getHost(@RequestParam Long userId);

    @GetMapping("/friend/list")
    ResultData getFriendList(@RequestParam Long userId);

    @GetMapping("/group/list")
    ResultData getGroupList(@RequestParam Long userId);

}
