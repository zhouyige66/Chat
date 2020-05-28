package cn.roy.chat.controller;

import cn.roy.chat.Main;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 21/05/2020 16:35
 * @Version: v1.0
 */
public abstract class BaseController implements Initializable {
    protected Stage mStage;

    public void setStage(Stage mStage) {
        this.mStage = mStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        NotifyManager.getInstance().register(new NotifyReceiver() {
            @Override
            public String getReceiveEventType() {
                return "logout";
            }

            @Override
            public void onReceiveEvent(NotifyEvent event) {
                if (mStage != Main.primaryStage) {
                    mStage.close();
                }
                NotifyManager.getInstance().unRegister(this);
            }
        });

        init();
    }

    public abstract void init();

}
