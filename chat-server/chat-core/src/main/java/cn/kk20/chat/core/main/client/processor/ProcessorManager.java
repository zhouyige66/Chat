package cn.kk20.chat.core.main.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.service.GroupMessageService;
import cn.kk20.chat.service.MessageService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 处理器管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:00 下午
 * @Version: v1.0
 */
@ClientComponent
public class ProcessorManager {
    private ConcurrentHashMap<Integer, MessageProcessor> messageProcessorMap;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MessageService messageService;
    @Autowired
    GroupMessageService groupMessageService;

    @Bean
    public ProcessorManager init() {
        messageProcessorMap = new ConcurrentHashMap<>(10);

        // 获取所有消息处理器
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ChatMsgProcessor.class);
        for (Object object : beansWithAnnotation.values()) {
            ChatMsgProcessor chatMsgProcessor = object.getClass().getAnnotation(ChatMsgProcessor.class);
            ChatMessageType chatMessageType = chatMsgProcessor.messageType();
            if (object instanceof MessageProcessor) {
                messageProcessorMap.put(chatMessageType.getCode(), (MessageProcessor) object);
            }
        }

        return this;
    }

    public void handleMessage(ChannelHandlerContext ctx, ChatMessage chatMessage, boolean isFromWeb) {
        // 在此处统一存储，存入数据库或redis
        ChatMessageType chatMessageType = chatMessage.getChatMessageType();
        Long id = null;
        if (chatMessageType == ChatMessageType.GROUP) {
            GroupMessageModel groupMessageModel = new GroupMessageModel();
            groupMessageModel.setUserId(chatMessage.getFromUserId());
            groupMessageModel.setGroupId(chatMessage.getToUserId());
            groupMessageModel.setContentType(chatMessage.getBodyType().getCode());
            groupMessageModel.setContent(chatMessage.getBody());
            groupMessageService.insert(groupMessageModel);
            id = groupMessageModel.getId();
        } else {
            MessageModel messageModel = new MessageModel();
            messageModel.setFromUserId(chatMessage.getFromUserId());
            messageModel.setToUserId(chatMessage.getToUserId());
            messageModel.setContent(chatMessage.getBody());
            messageModel.setContentType(chatMessage.getBodyType().getCode());
            messageService.insert(messageModel);
            id = messageModel.getId();
        }

        // 分配至业务处理器
        int messageType = chatMessageType.getCode();
        MessageProcessor processor = messageProcessorMap.get(messageType);
        chatMessage.setId(id);
        processor.processMessage(ctx, chatMessage, isFromWeb);
    }

}
