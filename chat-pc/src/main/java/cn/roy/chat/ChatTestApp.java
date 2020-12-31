package cn.roy.chat;

import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import cn.roy.chat.util.FXMLUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * @Description:
 * @Author: zhouzongyi@cpic.com.cn
 * @Date: 2020/12/30
 * @Version: v1.0
 */
public class ChatTestApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage primaryStage = stage;
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

        try {
            FXMLLoader loader = FXMLUtil.getFXMLLoader("chat_main");
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
