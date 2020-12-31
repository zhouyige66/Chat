package cn.kk20.chat.api.listener;

import cn.kk20.chat.api.entity.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/23 15:49
 * @Version: v1.0
 */
@Component
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void receive(TransactionEvent event) {
        logger.info("收到commit通知：{}", event.getMsg());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollback(TransactionEvent event) {
        logger.info("收到rollback通知：{}", event.getMsg());
    }

}
