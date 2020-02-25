package cn.kk20.chat.core.config;

import cn.kk20.chat.core.main.Launcher;
import cn.kk20.chat.core.main.client.ChatClient;
import cn.kk20.chat.core.main.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/22 15:26
 * @Version: v1.0
 */
@Configuration
public class ChatConfiguration {

    @Autowired
    ApplicationContext context;

    @Autowired
    ChatConfigBean chatConfigBean;

    @Bean
    public Launcher launcher() {
        if (chatConfigBean.isRegisterAsServer()) {
            return context.getBean(ChatServer.class);
        } else {
            return context.getBean(ChatClient.class);
        }
    }

}
