package cn.kk20.chat.api.listener;

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

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            System.out.println("程序启动完成");
        } else if (event instanceof ContextClosedEvent) {
            System.out.println("程序关闭");
        } else {
            System.out.println("ApplicationContextEvent==" + event.getClass().getSimpleName());
        }
    }

}
