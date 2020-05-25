package cn.roy.chat.controller;

import cn.roy.chat.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 21/05/2020 16:35
 * @Version: v1.0
 */
public abstract class BaseController implements Initializable {
    private Main application;
    private Stage mainStage;
    private ApplicationContext applicationContext;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public Main getApplication() {
        return application;
    }

    public void setApplication(Main application) {
        this.application = application;
        this.mainStage = application.getMainStage();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void jump2NewScene(String fxmlName) {
        jump2NewScene(fxmlName, null);
    }

    public void jump2NewScene(String fxmlName, String styleName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URL fxml = classLoader.getResource("layout" + File.separator + fxmlName);
            URL css = null;
            if (styleName != null && !styleName.trim().equals("")) {
                css = classLoader.getResource("css" + File.separator + styleName);
            }
            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.setApplication(this.application);

            Scene scene = config(this.mainStage, root);
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }
            mainStage.setScene(scene);
            mainStage.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        application.jump2Login();
    }

    public abstract Scene config(Stage stage, Parent root);

}
