package cn.kk20.chat.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.NotifyMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.base.message.notify.NotifyMessageType;
import cn.kk20.chat.client.MessageSender;
import cn.kk20.chat.core.service.MessageService;
import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.MessageModel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 处理器管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:00 下午
 * @Version: v1.0
 */
@Component
public class ProcessorManager {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorManager.class);
    private ConcurrentHashMap<Integer, MessageProcessor> messageProcessorMap;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MessageSender messageSender;
    @Autowired
    MessageService messageService;

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
        Long originId = chatMessage.getId();
        ChatMessageType chatMessageType = chatMessage.getChatMessageType();
        Long currentId = null;
        if (chatMessageType == ChatMessageType.GROUP) {
            GroupMessageModel groupMessageModel = new GroupMessageModel();
            groupMessageModel.setUserId(chatMessage.getFromUserId());
            groupMessageModel.setGroupId(chatMessage.getToUserId());
            groupMessageModel.setContentType(chatMessage.getBodyType().getCode());
            groupMessageModel.setContent(chatMessage.getBody());
            messageService.saveGroupMessage(groupMessageModel);
            currentId = groupMessageModel.getId();
        } else {
            MessageModel messageModel = new MessageModel();
            messageModel.setFromUserId(chatMessage.getFromUserId());
            messageModel.setToUserId(chatMessage.getToUserId());
            messageModel.setContent(chatMessage.getBody());
            messageModel.setContentType(chatMessage.getBodyType().getCode());
            messageService.saveSingleMessage(messageModel);
            currentId = messageModel.getId();
        }

        // 保存成功，回复客户端，数据库ID
        Map<String, Object> map = new HashMap<>();
        map.put("originId", originId);
        map.put("currentId", currentId);
        map.put("timestamp", System.currentTimeMillis());
        logger.debug("回复消息：originId={}，currentId={}", originId, currentId);
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setNotifyMessageType(NotifyMessageType.CHAT_MESSAGE_ID);
        notifyMessage.setData(map);
        messageSender.sendMessage(ctx.channel(), notifyMessage);

        // 分配至业务处理器
        int messageType = chatMessageType.getCode();
        MessageProcessor processor = messageProcessorMap.get(messageType);
        chatMessage.setId(currentId);
        chatMessage.setSendTimestamp(System.currentTimeMillis());
        processor.processMessage(ctx, chatMessage, isFromWeb);
    }

}
