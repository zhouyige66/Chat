package cn.roy.chat.controller;

import cn.roy.chat.call.CallChatServer;
import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import feign.Feign;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.netty.util.internal.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

public class LoginController extends BaseController {
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label tipLabel;
    @FXML
    private Button loginButton;

    public void login(ActionEvent actionEvent) {
        String userName = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        if (StringUtil.isNullOrEmpty(userName)) {
            tipLabel.setText("用户名不能为空");
            return;
        }

        if (StringUtil.isNullOrEmpty(password)) {
            tipLabel.setText("密码不能为空");
            return;
        }

        doLogin(userName, password);
    }

    private void doLogin(String userName, String password) {
        tipLabel.setText("");
        userNameField.setEditable(false);
        passwordField.setEditable(false);
        loginButton.setText("登录中...");
        loginButton.setDisable(true);

        final LoginEntity entity = new LoginEntity();
        entity.setUserName(userName);
        entity.setPassword(password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallChatServer callChatServer = Feign.builder()
                        .encoder(new JacksonEncoder())
                        .contract(new SpringMvcContract())
                        .client(new OkHttpClient())
                        .target(CallChatServer.class, "http://localhost:8081/");
                final ResultData resultData = callChatServer.login(entity);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (resultData.getCode() == 200) {
                            userNameField.setEditable(true);
                            passwordField.setEditable(true);
                            loginButton.setText("登录");
                            loginButton.setDisable(false);
                            jump2Main();
                        } else {
                            userNameField.setEditable(true);
                            passwordField.setEditable(true);
                            tipLabel.setText(resultData.getMsg());
                            loginButton.setText("登录");
                            loginButton.setDisable(false);
                        }
                    }
                });
            }
        }).start();
    }

    private void jump2Main() {
        jump2NewScene("main.fxml", "main.css");
    }

    @Override
    public Scene config(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setTitle("Chat");
        stage.setMinWidth(300.0);
        stage.setMaxWidth(400.0);
        stage.setMinHeight(550.0);
        stage.setMaxHeight(750.0);
        stage.setWidth(350.0);
        stage.setHeight(700.0);
        stage.setResizable(true);
        return scene;
    }

}
