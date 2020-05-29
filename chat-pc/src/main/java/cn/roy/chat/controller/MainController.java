package cn.roy.chat.controller;

import cn.roy.chat.Main;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import cn.roy.chat.call.CallChatServer;
import cn.roy.chat.core.ChatClient;
import cn.roy.chat.enity.*;
import cn.roy.chat.util.CacheUtil;
import cn.roy.chat.util.FXMLUtil;
import cn.roy.chat.util.http.HttpRequestTask;
import cn.roy.chat.util.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 21/05/2020 14:43
 * @Version: v1.0
 */
public class MainController extends BaseController {
    @FXML
    ImageView userHeadImageView;
    @FXML
    Label connectStatusLabel;
    @FXML
    Label userNameLabel;
    @FXML
    Label userPhoneLabel;
    @FXML
    Label registerTimeLabel;

    @FXML
    RadioButton chatRadio;
    @FXML
    RadioButton contactRadio;
    @FXML
    RadioButton groupRadio;

    @FXML
    StackPane stackPane;
    @FXML
    ListView chatListView;
    @FXML
    ListView friendListView;
    @FXML
    ListView groupListView;
    @FXML
    Label noDataLabel;

    private UserEntity user;
    private ObservableList<RecentContactEntity> recentContactEntities;
    private ObservableList<FriendEntity> friendEntities;
    private ObservableList<GroupEntity> groupEntities;

    @Override
    public void init() {
        user = CacheUtil.getCache("user", UserEntity.class);
        recentContactEntities = FXCollections.observableArrayList();
        friendEntities = FXCollections.observableArrayList();
        groupEntities = FXCollections.observableArrayList();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(userHeadImageView.getFitWidth() / 2);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.color(1.0, 1.0, 1.0));
        userHeadImageView.setEffect(dropShadow);
        connectStatusLabel.setText(ChatClient.getInstance().getStatus().getDes());

        userNameLabel.setText(user.getName());
        userPhoneLabel.setText(user.getPhone());
        registerTimeLabel.setText(user.getCreateDate());

        ToggleGroup toggleGroup = new ToggleGroup();
        chatRadio.setToggleGroup(toggleGroup);
        contactRadio.setToggleGroup(toggleGroup);
        groupRadio.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                ListView frontListView;
                if (newValue == chatRadio) {
                    frontListView = chatListView;
                } else if (newValue == contactRadio) {
                    frontListView = friendListView;
                } else {
                    frontListView = groupListView;
                }

                frontListView.toFront();
                int size = frontListView.getItems().size();
                if (size == 0) {
                    noDataLabel.toFront();
                }
            }
        });

        chatListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        chatListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Stage stage = new Stage();
                stage.setTitle("聊天");
                stage.setMinWidth(800.0);
                stage.setMaxWidth(1200.0);
                stage.setMinHeight(600.0);
                stage.setMaxHeight(900.0);
                final ChatController chatController = FXMLUtil.startNewScene("chat", stage);
                chatController.setStage(stage);
            }
        });
        friendListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new FriendListCell();
            }
        });
        friendListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        friendListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Stage stage = new Stage();
                stage.setTitle("聊天");
                stage.setMinWidth(800.0);
                stage.setMaxWidth(1200.0);
                stage.setMinHeight(600.0);
                stage.setMaxHeight(900.0);
                final ChatController chatController = FXMLUtil.startNewScene("chat", stage);
                chatController.setStage(stage);
            }
        });
        groupListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new GroupListCell();
            }
        });
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        groupListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Stage stage = new Stage();
                stage.setTitle("聊天");
                stage.setMinWidth(800.0);
                stage.setMaxWidth(1200.0);
                stage.setMinHeight(600.0);
                stage.setMaxHeight(900.0);
                final ChatController chatController = FXMLUtil.startNewScene("chat", stage);
                chatController.setStage(stage);
            }
        });

        NotifyManager.getInstance().register(new NotifyReceiver() {
            @Override
            public String getReceiveEventType() {
                return ChatClient.NOTIFY_CHAT_STATUS;
            }

            @Override
            public void onReceiveEvent(NotifyEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        connectStatusLabel.setText(event.getStringValue("statusDes"));
                    }
                });
            }
        });

        HttpUtil.execute(new HttpRequestTask() {
            @Override
            public ResultData doInBackground() {
                final CallChatServer callChatServer = Main.context.getBean(CallChatServer.class);
                final ResultData resultData = callChatServer.getFriendList(Long.valueOf(user.getId()));
                return resultData;
            }

            @Override
            public void success(String data) {
                final JSONObject jsonObject = JSON.parseObject(data);
                final List<FriendEntity> list = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(),
                        FriendEntity.class);

                friendEntities.addAll(list);
                friendListView.setItems(friendEntities);
            }

            @Override
            public void fail(String msg) {
                System.out.println("发生错误：" + msg);
            }
        });

        HttpUtil.execute(new HttpRequestTask() {
            @Override
            public ResultData doInBackground() {
                final CallChatServer callChatServer = Main.context.getBean(CallChatServer.class);
                final ResultData resultData = callChatServer.getGroupList(Long.valueOf(user.getId()));
                return resultData;
            }

            @Override
            public void success(String data) {
                final JSONObject jsonObject = JSON.parseObject(data);
                final List<GroupEntity> list = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(),
                        GroupEntity.class);
                groupEntities.addAll(list);
                groupListView.setItems(groupEntities);
            }

            @Override
            public void fail(String msg) {
                System.out.println("发生错误：" + msg);
            }
        });
    }

    public void logout(MouseEvent mouseEvent) {
        final NotifyEvent notifyEvent = new NotifyEvent();
        notifyEvent.setEventType("logout");
        NotifyManager.getInstance().notifyEvent(notifyEvent);
    }

    static class RecentContactListCell extends ListCell<RecentContactEntity> {
        @Override
        protected void updateItem(RecentContactEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_friend");
            VBox vBox = (VBox) parent.getChildren().get(1);
            final Label label = (Label) vBox.getChildren().get(0);
            label.setText(item.getName());
            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

    static class FriendListCell extends ListCell<FriendEntity> {
        @Override
        protected void updateItem(FriendEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_friend");
            VBox vBox = (VBox) parent.getChildren().get(1);
            final Label label = (Label) vBox.getChildren().get(0);
            final Label label2 = (Label) vBox.getChildren().get(1);
            final Label label3 = (Label) parent.getChildren().get(2);
            label.setText(item.getName());
            label2.setText("在线");
            label3.setText("");
            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

    static class GroupListCell extends ListCell<GroupEntity> {
        @Override
        protected void updateItem(GroupEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_friend");
            final VBox vBox = (VBox) parent.getChildren().get(1);
            final Label label = (Label) vBox.getChildren().get(0);
            final Label label2 = (Label) vBox.getChildren().get(1);
            final Label label3 = (Label) parent.getChildren().get(2);
            label.setText(item.getName());
            label2.setText(item.getCreateDate());
            label3.setText("8");
            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

}
