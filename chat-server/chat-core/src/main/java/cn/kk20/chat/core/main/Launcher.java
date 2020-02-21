package cn.kk20.chat.core.main;

import cn.kk20.chat.core.handler.HandlerManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    HandlerManager handlerManager;

    public void launch(){
        System.out.println("系统启动了");
    }

    public void stop(){
        System.out.println("系统停止了");
    }

}
