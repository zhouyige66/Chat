package cn.kk20.chat.core.main;

import cn.kk20.chat.core.common.ChatConfigBean;
import cn.kk20.chat.core.main.client.ChatClient;
import cn.kk20.chat.core.main.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description:  聊天系统启动器
 * @Author: Roy Z
 * @Date: 2020/2/21 10:56
 * @Version: v1.0
 */
@Component
public class Launcher {

    @Autowired
    ApplicationContext context;

    @Autowired
    ChatConfigBean chatConfigBean;

    public void launch(){
        System.out.println("系统启动了");

        if(chatConfigBean.isRegisterAsServer()){
            ChatServer chatServer= context.getBean(ChatServer.class);
            chatServer.start();
        }else {
            ChatClient chatClient= context.getBean(ChatClient.class);
            chatClient.launch();
        }
    }

    public void stop(){
        System.out.println("系统停止了");
    }

}
