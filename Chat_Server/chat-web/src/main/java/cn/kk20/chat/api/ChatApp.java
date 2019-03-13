package cn.kk20.chat.api;

import cn.kk20.chat.core.util.LogUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.kk20.chat")
@MapperScan(value = "cn.kk20.chat.mapper")
public class ChatApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LogUtil.log("执行CommandLineRunner的run()方法");
    }
}

