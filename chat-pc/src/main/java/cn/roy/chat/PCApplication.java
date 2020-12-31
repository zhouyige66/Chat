package cn.roy.chat;

import cn.roy.chat.enity.UserEntity;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication(scanBasePackages = "cn.roy.chat")
@EnableFeignClients
public class PCApplication implements ApplicationContextAware, ApplicationRunner {
    public static volatile ApplicationContext context;
    public static volatile UserEntity currentUser;
    public static volatile Stage primaryStage;

    public static void main(String[] args) {
        SpringApplication.run(PCApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner");
//        ChatApp.main(args.getSourceArgs());
        ChatTestApp.main(args.getSourceArgs());
    }

}
