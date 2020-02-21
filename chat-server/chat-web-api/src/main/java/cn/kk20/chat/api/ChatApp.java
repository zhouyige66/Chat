package cn.kk20.chat.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "cn.kk20.chat")
@MapperScan(value = "cn.kk20.chat.mapper")
public class ChatApp extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("执行CommandLineRunner的run()方法");
    }

    /**
     * Web容器中进行部署
     *
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ChatApp.class);
    }

}

