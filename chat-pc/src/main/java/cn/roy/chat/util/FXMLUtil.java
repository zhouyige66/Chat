package cn.roy.chat.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
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

    public static <T> T loadFXML(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("layout").append(File.separator).append(name).append(".fxml");
        final URL url = FXMLUtil.class.getClassLoader().getResource(stringBuilder.toString());
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            T t = fxmlLoader.load();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
