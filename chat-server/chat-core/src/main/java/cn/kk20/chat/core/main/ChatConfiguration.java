package cn.kk20.chat.core.main;

import cn.kk20.chat.core.main.client.ChatClient;
import cn.kk20.chat.core.main.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
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
    ChatConfigBean chatConfigBean;

    @Bean
    public Launcher launcher() {
        Launcher launcher;
        if(chatConfigBean.isRegisterAsServer()){
            ChatServer chatServer = new ChatServer();
            chatServer.setChatConfigBean(chatConfigBean);
            launcher = chatServer;
        }else {
            ChatClient chatClient = new ChatClient();
            chatClient.setChatConfigBean(chatConfigBean);
            launcher = chatClient;
        }

        return launcher;
    }

}
