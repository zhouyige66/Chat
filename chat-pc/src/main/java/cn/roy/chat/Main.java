package cn.roy.chat;

import cn.roy.chat.controller.LoginController;
import cn.roy.chat.enity.UserEntity;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication(scanBasePackages = "cn.roy.chat")
@EnableFeignClients
public class Main extends Application implements ApplicationContextAware, ApplicationRunner {
    private Stage mainStage;
    public static ApplicationContext context;
    public static UserEntity currentUser;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner");
        launch(args.getSourceArgs());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainStage = primaryStage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                EventType<WindowEvent> eventType = event.getEventType();
                if (eventType.equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                    event.consume();
                    mainStage.setIconified(true);
                }
            }
        });

        jump2Login();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void jump2Login() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URL fxml = classLoader.getResource("layout" + File.separator + "login.fxml");
            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setApplication(this);
            controller.setApplicationContext(context);

            Scene scene = new Scene(root);
            Image image = new Image(classLoader.getResourceAsStream("img" + File.separator + "logo.png"));
            mainStage.getIcons().add(image);
            mainStage.setTitle("登录");
            mainStage.setScene(scene);
            mainStage.setMinWidth(0.0);
            mainStage.setMinHeight(0.0);
            mainStage.setWidth(400);
            mainStage.setHeight(300);
            mainStage.setResizable(false);
            mainStage.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
