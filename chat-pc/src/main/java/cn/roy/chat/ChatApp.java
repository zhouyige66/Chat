package cn.roy.chat;

import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import cn.roy.chat.controller.LoginController;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.util.FXMLUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;

import java.io.File;

public class ChatApp extends Application {
    public static volatile ApplicationContext context;
    public static volatile UserEntity currentUser;
    public static volatile Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Image image = new Image(getClass().getClassLoader()
                .getResourceAsStream("img" + File.separator + "logo.png"));
        primaryStage.getIcons().add(image);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                EventType<WindowEvent> eventType = event.getEventType();
                if (eventType.equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                    event.consume();

                    // 最小化
                    primaryStage.setIconified(true);
                }
            }
        });

        NotifyManager.getInstance().register(new NotifyReceiver() {
            @Override
            public String getReceiveEventType() {
                return "logout";
            }

            @Override
            public void onReceiveEvent(NotifyEvent event) {
                jump2Login();
            }
        });

        jump2Login();
    }

    public static void jump2Login() {
        primaryStage.setTitle("登录");
        primaryStage.setMinWidth(0.0);
        primaryStage.setMinHeight(0.0);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setResizable(false);
        final LoginController loginController = FXMLUtil.startNewScene("login", primaryStage);
        loginController.setStage(primaryStage);
    }

}
