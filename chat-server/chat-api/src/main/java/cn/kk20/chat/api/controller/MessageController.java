package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.base.ResultData;
import cn.kk20.chat.api.base.dto.SimpleDto;
import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.dao.model.MessageModel;
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
    public ResultData login(@RequestBody ChatMessage chatMessage) {
        // 转发
        ClientManager.getInstance().sendMessage(chatMessage.getToUserId(), chatMessage);

        // 存储到数据库
        MessageModel messageModel = new MessageModel();
        messageModel.setId(IdGenerator.generateId());
        messageModel.setFromUserId(chatMessage.getFromUserId());
        messageModel.setToUserId(chatMessage.getToUserId());
        messageModel.setContent(JSON.toJSONString(chatMessage));
        messageService.save(messageModel);

        return ResultData.success(new SimpleDto().setValue(messageModel.getId()));
    }

}
