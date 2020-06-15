package cn.roy.chat.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 15:53
 * @Version: v1.0
 */
public class FXMLUtil {

    private FXMLUtil() {
    }

    public static String getCSSUrl(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("css").append(File.separator).append(name).append(".css");
        final String s = FXMLUtil.class.getClassLoader().getResource(stringBuilder.toString()).toExternalForm();
        return s;
    }

    public static FXMLLoader getFXMLLoader(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("layout").append(File.separator).append(name).append(".fxml");
        final URL url = FXMLUtil.class.getClassLoader().getResource(stringBuilder.toString());
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader;
    }

    public static <T> T loadFXML(String name) {
        try {
            T t = getFXMLLoader(name).load();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <Controller> Controller startNewScene(String fxmlName, Stage stage) {
        try {
            FXMLLoader loader = getFXMLLoader(fxmlName);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Controller controller = loader.getController();
            return controller;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
