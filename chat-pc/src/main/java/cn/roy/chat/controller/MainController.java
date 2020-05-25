package cn.roy.chat.controller;

import cn.roy.chat.cache.CacheUtil;
import cn.roy.chat.enity.UserEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 21/05/2020 14:43
 * @Version: v1.0
 */
public class MainController extends BaseController {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final UserEntity user = CacheUtil.getCache("user", UserEntity.class);
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

        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll("A", "B", "C", "D", "E");
        listView.setItems(data);
        listView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ColorRectCell();
            }
        });
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Stage stage = new Stage();
                stage.setTitle("聊天");
                BorderPane root = new BorderPane();
                stage.setScene(new Scene(root,400,400));
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
    }

    @Override
    public Scene config(Stage stage, Parent root) {
        return null;
    }

    static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            Label label = new Label();
            label.setText(item);
            setGraphic(label);
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
        public String toString()  {
            return this.name;
        }

    }

}
