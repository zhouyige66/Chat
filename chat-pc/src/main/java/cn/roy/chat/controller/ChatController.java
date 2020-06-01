package cn.roy.chat.controller;

import cn.kk20.chat.base.message.Message;
import cn.roy.chat.core.ChatManager;
import cn.roy.chat.enity.FriendEntity;
import cn.roy.chat.enity.GroupEntity;
import cn.roy.chat.enity.RecentContactEntity;
import cn.roy.chat.util.FXMLUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/28 15:21
 * @Version: v1.0
 */
public class ChatController extends BaseController {
    @FXML
    ListView recentContactListView;
    @FXML
    Label currentContactUserLabel;
    @FXML
    ListView messageListView;
    @FXML
    TextArea msgTextArea;

    private RecentContactEntity recentContactEntity;
    private Map<Integer, FriendEntity> friendEntityMap = new HashMap<>();
    private Map<Integer, GroupEntity> groupEntityMap = new HashMap<>();

    private ObservableList<RecentContactEntity> recentContactEntities;

    @Override
    public void init() {
        recentContactEntities = FXCollections.observableArrayList();
        recentContactListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recentContactListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
        recentContactListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ContactListCell();
            }
        });
        recentContactListView.setItems(recentContactEntities);
    }

    @Override
    protected void release() {
        super.release();

        ChatManager.getInstance().setChatController(null);
    }

    public void setRecentContactEntity(RecentContactEntity recentContactEntity) {
        this.recentContactEntity = recentContactEntity;

        int index = 0;
        if (recentContactEntity.getType() == 0) {
            final FriendEntity friendEntity = recentContactEntity.getFriendEntity();
            currentContactUserLabel.setText(friendEntity.getName());
            if (friendEntityMap.containsKey(friendEntity.getId())) {
                index = findRecentContactIndex(recentContactEntity);
            } else {
                friendEntityMap.put(friendEntity.getId(), friendEntity);
                recentContactEntities.add(recentContactEntity);
                index = recentContactEntities.size() - 1;
            }
        } else {
            final GroupEntity groupEntity = recentContactEntity.getGroupEntity();
            currentContactUserLabel.setText(groupEntity.getName());
            if (groupEntityMap.containsKey(groupEntity.getId())) {
                index = findRecentContactIndex(recentContactEntity);
            } else {
                groupEntityMap.put(groupEntity.getId(), groupEntity);
                recentContactEntities.add(recentContactEntity);
                index = recentContactEntities.size() - 1;
            }
        }
        recentContactListView.refresh();
        recentContactListView.getSelectionModel().select(index);
    }

    private int findRecentContactIndex(RecentContactEntity entity) {
        int index = 0;
        for (RecentContactEntity item : recentContactEntities) {
            final int type = item.getType();
            if (type == entity.getType()) {
                if (type == 0) {
                    if (entity.getFriendEntity().getId() == item.getFriendEntity().getId()) {
                        break;
                    }
                } else {
                    if (entity.getGroupEntity().getId() == item.getGroupEntity().getId()) {
                        break;
                    }
                }
            }
            index++;
        }
        return index;
    }

    static class ContactListCell extends ListCell<RecentContactEntity> {
        @Override
        protected void updateItem(RecentContactEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_friend");
            final VBox vBox = (VBox) parent.getChildren().get(1);
            final Label label = (Label) vBox.getChildren().get(0);
            final Label label2 = (Label) vBox.getChildren().get(1);
            final Label label3 = (Label) parent.getChildren().get(2);
            if (item.getType() == 0) {
                label.setText(item.getFriendEntity().getName());
            } else {
                label.setText(item.getGroupEntity().getName());
            }
            final Message lastChatMessage = item.getLastChatMessage();
            if (lastChatMessage != null) {
                label2.setText("消息：");
            }

            setGraphic(parent);
        }
    }

}
