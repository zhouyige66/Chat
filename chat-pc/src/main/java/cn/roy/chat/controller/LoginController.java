package cn.roy.chat.controller;

import cn.roy.chat.Main;
import cn.roy.chat.call.CallChatServer;
import cn.roy.chat.core.ChatConfig;
import cn.roy.chat.core.ChatClient;
import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.util.CacheUtil;
import cn.roy.chat.util.FXMLUtil;
import cn.roy.chat.util.http.HttpRequestTask;
import cn.roy.chat.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label tipLabel;
    @FXML
    private Button loginButton;

    @Override
    public void init() {
        final UserEntity user = CacheUtil.getCacheFromProp("user", UserEntity.class);
        if (user != null) {
            userNameField.setText(user.getName());
            passwordField.setText(user.getPassword());

            userNameField.positionCaret(user.getName().length());
        }
    }

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

        HttpUtil.execute(new HttpRequestTask() {
            @Override
            public ResultData doInBackground() {
                final CallChatServer callChatServer = Main.context.getBean(CallChatServer.class);
                final ResultData resultData = callChatServer.login(entity);
                return resultData;
            }

            @Override
            public void success(String data) {
                final UserEntity userEntity = JSON.parseObject(data, UserEntity.class);
                userEntity.setPassword(password);
                Main.currentUser = userEntity;
                CacheUtil.cacheData("user", userEntity);
                CacheUtil.cacheToProp("user", userEntity);

                ChatConfig config = new ChatConfig();
                config.setHost("127.0.0.1");
                config.setPort(20001);
                config.setAutoReconnectTime(5);
                config.setHeartbeatFailCount(6);
                ChatClient.getInstance().setConfig(config);
                ChatClient.getInstance().connectServer();

                userNameField.setEditable(true);
                passwordField.setEditable(true);
                loginButton.setText("登录");
                loginButton.setDisable(false);
                jump2Main();
            }

            @Override
            public void fail(String msg) {
                userNameField.setEditable(true);
                passwordField.setEditable(true);
                tipLabel.setText(msg);
                loginButton.setText("登录");
                loginButton.setDisable(false);
            }
        });
    }

    private void jump2Main() {
        mStage.setTitle("Chat");
        mStage.setMinWidth(300.0);
        mStage.setMaxWidth(400.0);
        mStage.setMinHeight(550.0);
        mStage.setMaxHeight(750.0);
        mStage.setWidth(350.0);
        mStage.setHeight(700.0);
        mStage.setResizable(true);
        final MainController mainController = FXMLUtil.startNewScene("main", mStage);
        mainController.setStage(mStage);
    }

}
