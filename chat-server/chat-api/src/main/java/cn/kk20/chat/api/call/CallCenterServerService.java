package cn.kk20.chat.api.call;

import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.message.ChatMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/16 11:13
 * @Version: v1.0
 */
@FeignClient(value = "server", path = "server", url = "localhost:10001")
public interface CallCenterServerService {

    @PostMapping("forwardChatMessage")
    ResultData sendChatMessage(@RequestBody ChatMessage chatMessage);

}
