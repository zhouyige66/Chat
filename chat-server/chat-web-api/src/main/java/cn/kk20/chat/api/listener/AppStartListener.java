package cn.kk20.chat.api.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-28 14:50
 * @Version: v1.0
 */
@Component
public class AppStartListener implements ApplicationListener<ApplicationContextEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AppStartListener.class);

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            logger.info("监听到事件：ContextRefreshedEvent");
        } else if (event instanceof ContextClosedEvent) {
            logger.info("监听到事件：ContextClosedEvent");
        } else {
            logger.info("ApplicationContextEvent=" + event.getClass().getSimpleName());
        }
    }

}
