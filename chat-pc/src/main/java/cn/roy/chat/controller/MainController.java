package cn.roy.chat.controller;

import cn.roy.chat.util.CacheUtil;
import cn.roy.chat.call.CallChatServer;
import cn.roy.chat.enity.FriendEntity;
import cn.roy.chat.enity.ResultData;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.util.http.HttpRequestTask;
import cn.roy.chat.util.http.HttpUtil;
import cn.roy.chat.util.FXMLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    ListView listView;
    @FXML
    TreeView treeView;
    @FXML
    TreeView treeView2;

    private UserEntity user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = CacheUtil.getCache("user", UserEntity.class);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(userHeadImageView.getFitWidth() / 2);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.color(1.0, 1.0, 1.0));
        userHeadImageView.setEffect(dropShadow);

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
                if (newValue == chatRadio) {
                    listView.toFront();
                } else if (newValue == contactRadio) {
                    treeView.toFront();
                } else {
                    treeView2.toFront();
                }
            }
        });

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Stage stage = new Stage();
                stage.setTitle("聊天");

                final Parent parent = FXMLUtil.loadFXML("chat");
                final Scene scene = new Scene(parent, 800, 600);
                stage.setScene(scene);
                stage.setMinWidth(800.0);
                stage.setMaxWidth(1200.0);
                stage.setMinHeight(600.0);
                stage.setMaxHeight(900.0);
                stage.show();
            }
        });

        BookCategory catJava = new BookCategory("JAVA-00", "Java");
        BookCategory catJSP = new BookCategory("JAVA-01", "Jsp");
        BookCategory catSpring = new BookCategory("JAVA-02", "Spring");
        // Root Item
        TreeItem<BookCategory> rootItem = new TreeItem<BookCategory>(catJava);
        rootItem.setExpanded(true);
        // JSP Item
        TreeItem<BookCategory> itemJSP = new TreeItem<BookCategory>(catJSP);
        // Spring Item
        TreeItem<BookCategory> itemSpring = new TreeItem<>(catSpring);
        // Add to Root
        rootItem.getChildren().addAll(itemJSP, itemSpring);
        treeView.setRoot(rootItem);

        HttpUtil.execute(new HttpRequestTask() {
            @Override
            public ResultData doInBackground() {
                final CallChatServer callChatServer = getApplicationContext()
                        .getBean(CallChatServer.class);
                final ResultData resultData = callChatServer.getFriendList(Long.valueOf(user.getId()));
                return resultData;
            }

            @Override
            public void success(String data) {
                final JSONObject jsonObject = JSON.parseObject(data);
                final List<FriendEntity> list = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(),
                        FriendEntity.class);

                ObservableList<FriendEntity> dataList = FXCollections.observableArrayList();
                dataList.addAll(list);
                listView.setItems(dataList);
                listView.setCellFactory(new Callback<ListView, ListCell>() {
                    @Override
                    public ListCell call(ListView param) {
                        return new ContactListCell();
                    }
                });
            }

            @Override
            public void fail(String msg) {
                System.out.println("发生错误：" + msg);
            }
        });
    }

    @Override
    public Scene config(Stage stage, Parent root) {
        return null;
    }

    static class ContactListCell extends ListCell<FriendEntity> {
        @Override
        protected void updateItem(FriendEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_friend");
            final Label label = (Label) parent.getChildren().get(2);
            label.setText(item.getName());
            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

    static class BookCategory {
        private String code;
        private String name;

        public BookCategory() {

        }

        public BookCategory(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

}
