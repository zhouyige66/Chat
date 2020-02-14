package cn.kk20.chat.api.controller;

import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.message.ChatMessage;
import cn.kk20.chat.core.util.IdGeneratorUtil;
import cn.kk20.chat.model.MessageModel;
import cn.kk20.chat.model.RestModel;
import cn.kk20.chat.service.MessageService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:33
 * @Version: v1.0
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping("/send")
    public RestModel login(@RequestBody ChatMessage message) {
        // 转发
        ClientManager.getInstance().sendMessage(message.getToUserId(), message);

        // 存储到数据库
        MessageModel messageModel = new MessageModel();
        messageModel.setId(IdGeneratorUtil.generateId());
        messageModel.setFromUserId(message.getFromUserId());
        messageModel.setToUserId(message.getToUserId());
        messageModel.setContent(JSON.toJSONString(message));
        messageService.save(messageModel);

        return RestModel.success(messageModel.getId());
    }

}
