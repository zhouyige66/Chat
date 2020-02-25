package cn.kk20.chat.core;

import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.util.LogUtil;
import cn.kk20.chat.core.main.Launcher;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Description: 程序入口
 * @Author: Roy Z
 * @Date: 2020/2/20 18:46
 * @Version: v1.0
 */
@SpringBootApplication
@EnableConfigurationProperties({ChatConfigBean.class})
@ComponentScan("cn.kk20.chat")
@MapperScan(basePackages = "cn.kk20.chat")
public class ServerApplication implements ApplicationListener<ApplicationContextEvent> {

    @Autowired
    Launcher launcher;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        if (applicationContextEvent instanceof ContextRefreshedEvent) {
            LogUtil.log("程序启动完成，启动ChatServer");
            launcher.launch();
        } else if (applicationContextEvent instanceof ContextClosedEvent) {
            LogUtil.log("程序关闭，停止ChatServer");
            launcher.stop();
        } else {
            LogUtil.log("ApplicationContextEvent==" + applicationContextEvent.getClass().getSimpleName());
        }
    }

}
