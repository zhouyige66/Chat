package cn.kk20.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.core.handler.business.MessageProcessor;
import cn.kk20.chat.core.handler.business.MsgProcessor;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.service.MessageService;
import cn.kk20.chat.service.impl.MessageServiceImpl;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 处理器管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:00 下午
 * @Version: v1.0
 */
@Component
public class HandlerManager {
    private ConcurrentHashMap<Integer, MessageProcessor> messageProcessorMap;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageService messageService;

    @Bean
    public HandlerManager init() {
        messageProcessorMap = new ConcurrentHashMap<>(10);

        // 获取所有消息处理器
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MsgProcessor.class);
        for (Object object : beansWithAnnotation.values()) {
            MsgProcessor msgProcessor = object.getClass().getAnnotation(MsgProcessor.class);
            ChatMessageType chatMessageType = msgProcessor.messageType();
            if (object instanceof MessageProcessor) {
                messageProcessorMap.put(chatMessageType.getCode(), (MessageProcessor) object);
            }
        }

        return this;
    }

    public void handleMessage(ChannelHandlerContext ctx, ChatMessage chatMessage, boolean isFromWeb) {
        // 消息id统一变更为系统生成
        chatMessage.setId(IdGenerator.generateId());
        // 在此处统一存储，存入数据库或redis
        MessageModel messageModel = new MessageModel();
        messageModel.setId(chatMessage.getId());
        messageModel.setFromUserId(chatMessage.getFromUserId());
        messageModel.setToUserId(chatMessage.getToUserId());
        messageModel.setContent(JSON.toJSONString(chatMessage));
        if (messageService == null) {
            LogUtil.log("未注入messageService");
            messageService = applicationContext.getAutowireCapableBeanFactory().createBean(MessageServiceImpl.class);
        }
        int result = messageService.save(messageModel);
        LogUtil.log("存储消息：" + result);
        // 分配至业务处理器
        int messageType = chatMessage.getType();
        MessageProcessor processor = messageProcessorMap.get(messageType);
        processor.processMessage(ctx, chatMessage, isFromWeb);
    }

}
