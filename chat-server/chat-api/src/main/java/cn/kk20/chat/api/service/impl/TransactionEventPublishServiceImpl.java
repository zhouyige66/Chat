package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.api.entity.TransactionEvent;
import cn.kk20.chat.api.service.TransactionEventPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/30 09:46
 * @Version: v1.0
 */
@Service
public class TransactionEventPublishServiceImpl implements TransactionEventPublishService {

    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void publish(String msg) {
        publisher.publishEvent(new TransactionEvent(msg));
    }

}
