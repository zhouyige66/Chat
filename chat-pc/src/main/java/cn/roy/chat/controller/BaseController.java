package cn.roy.chat.controller;

import cn.roy.chat.Main;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 21/05/2020 16:35
 * @Version: v1.0
 */
public abstract class BaseController implements Initializable, ChangeListener<Scene> {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    protected Stage mStage;

    public void setStage(Stage stage) {
        this.mStage = stage;
        this.mStage.sceneProperty().addListener(this);
        if (this.mStage != Main.primaryStage) {
            this.mStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    EventType<WindowEvent> eventType = event.getEventType();
                    if (eventType.equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                        release();
                    }
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerNotifyEventReceiver(new NotifyReceiver() {
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

    @Override
    public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
        logger.info("已经发送了变化，需要回收处理");
        mStage.sceneProperty().removeListener(this);
        release();
    }

    public abstract void init();

    private List<NotifyReceiver> notifyReceiverList;

    public void registerNotifyEventReceiver(NotifyReceiver receiver) {
        if (notifyReceiverList == null) {
            notifyReceiverList = new ArrayList<>();
        }
        notifyReceiverList.add(receiver);
        NotifyManager.getInstance().register(receiver);
    }

    public void unRegisterNotifyEventReceiver(NotifyReceiver receiver) {
        if (notifyReceiverList.contains(receiver)) {
            notifyReceiverList.remove(receiver);
        }
        NotifyManager.getInstance().unRegister(receiver);
    }

    protected void release() {
        // 注销全部观察器
        if (notifyReceiverList != null && notifyReceiverList.size() > 0) {
            for (NotifyReceiver receiver : notifyReceiverList) {
                NotifyManager.getInstance().unRegister(receiver);
            }
            notifyReceiverList.clear();
        }
        notifyReceiverList = null;
    }

}
