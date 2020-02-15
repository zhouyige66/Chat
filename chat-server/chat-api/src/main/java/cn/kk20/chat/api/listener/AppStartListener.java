package cn.kk20.chat.api.listener;

import cn.kk20.chat.core.ChatServer;
import cn.kk20.chat.core.util.LogUtil;
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
            LogUtil.log("程序启动完成，启动ChatServer");
            ChatServer.getInstance().launch(event.getApplicationContext());
        } else if (event instanceof ContextClosedEvent) {
            LogUtil.log("程序关闭，停止ChatServer");
            ChatServer.getInstance().stop();
        } else {
            LogUtil.log("ApplicationContextEvent==" + event.getClass().getSimpleName());
        }
    }

}
