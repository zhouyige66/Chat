package cn.kk20.chat.center;

import cn.kk20.chat.center.config.ChatParameterBean;
import cn.kk20.chat.core.config.ChatCoderConfigBean;
import cn.kk20.chat.core.main.Launcher;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Author: Roy
 * @Date: 2020/2/20 18:46
 * @Version: v1.0
 */
@SpringBootApplication
@EnableConfigurationProperties({ChatParameterBean.class})
@ComponentScan("cn.kk20.chat")
@MapperScan(value = "cn.kk20.chat.dao.mapper")
public class CenterApplication implements ApplicationListener<ApplicationContextEvent> {
    private static final Logger logger = LoggerFactory.getLogger(CenterApplication.class);

    @Autowired
    Launcher launcher;

    public static void main(String[] args) {
        SpringApplication.run(CenterApplication.class,args);
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        if (applicationContextEvent instanceof ContextRefreshedEvent) {
            logger.debug("程序启动完成，启动ChatServer");
            launcher.launch();
        } else if (applicationContextEvent instanceof ContextClosedEvent) {
            logger.debug("程序关闭，停止ChatServer");
            launcher.stop();
        } else {
            logger.debug("ApplicationContextEvent==" + applicationContextEvent.getClass().getSimpleName());
        }
    }

}
