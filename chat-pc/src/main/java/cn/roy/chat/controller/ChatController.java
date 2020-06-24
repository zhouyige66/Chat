package cn.roy.chat.controller;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.base.message.chat.body.TextData;
import cn.roy.chat.Main;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.broadcast.NotifyReceiver;
import cn.roy.chat.core.ChatManager;
import cn.roy.chat.core.util.ChatMessageUtil;
import cn.roy.chat.core.util.CommonUtil;
import cn.roy.chat.enity.ContactEntity;
import cn.roy.chat.enity.FriendEntity;
import cn.roy.chat.enity.GroupEntity;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.util.FXMLUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/28 15:21
 * @Version: v1.0
 */
public class ChatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @FXML
    ListView recentContactListView;
    @FXML
    Label currentContactUserLabel;
    @FXML
    ListView messageListView;
    @FXML
    TextArea msgTextArea;

    private ContactEntity contactEntity;
    private Map<Integer, FriendEntity> friendEntityMap = new HashMap<>();
    private Map<Integer, GroupEntity> groupEntityMap = new HashMap<>();
    private ObservableList<ContactEntity> recentContactEntities = FXCollections.observableArrayList();
    private ObservableList<ChatMessage> chatMessages = FXCollections.observableArrayList();
    private NotifyReceiver msgNotifyReceiver;

    @Override
    public void init() {
        // 联系人列表
        recentContactListView.getStylesheets().add(FXMLUtil.getCSSUrl("item"));
        recentContactListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recentContactListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setContactEntity(recentContactEntities.get(newValue.intValue()));
            }
        });
        recentContactListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ContactListCell();
            }
        });
        recentContactListView.setItems(recentContactEntities);
        // 消息列表
        messageListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        messageListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
        messageListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new MsgListCell(contactEntity);
            }
        });
        messageListView.setItems(chatMessages);

        // 注册键盘输入事件
        msgTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                final String text = msgTextArea.getText().trim();
                if (StringUtils.isEmpty(text)) {
                    return;
                }

                final KeyCode code = event.getCode();
                if (code.equals(KeyCode.ENTER)) {
                    final boolean altDown = event.isAltDown();
                    logger.debug("altDown=={}", altDown);
                    logger.debug("event.getCode()=={}", code);
                    if (altDown) {
                        event.consume();
                        logger.debug("不消费事件");
                        msgTextArea.setText(text + "\r\n");
                        msgTextArea.positionCaret(msgTextArea.getText().length());
                    } else {
                        logger.debug("消费事件");
                        // 消费调事件
                        event.consume();
                        // 发送消息
                        TextData textData = new TextData();
                        textData.setText(text);
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setToUserId(contactEntity.getContactId());
                        chatMessage.setChatMessageType(contactEntity.getType() == 0 ? ChatMessageType.SINGLE
                                : ChatMessageType.GROUP);
                        chatMessage.setBody(textData);
                        ChatManager.getInstance().sendMessage(chatMessage);
                        // 情况输入区
                        msgTextArea.setText("");
                    }
                }
            }
        });
        // 注册消息监听器
        msgNotifyReceiver = new NotifyReceiver() {
            @Override
            public String getReceiveEventType() {
                return "update_message_list";
            }

            @Override
            public void onReceiveEvent(NotifyEvent event) {
                recentContactListView.refresh();
                // 更新当前联系人列表
                if (event.getStringValue("key").equals(contactEntity.getIdentifyKey())) {
                    // 更新列表
                    ChatMessage chatMessage = (ChatMessage) event.getSerializableValue("data");
                    chatMessages.add(chatMessage);
                    messageListView.scrollTo(chatMessages.size() - 1);
                }
            }
        };
        NotifyManager.getInstance().register(msgNotifyReceiver);
    }

    @Override
    protected void release() {
        super.release();
        // 取消注册
        msgTextArea.setOnKeyPressed(null);
        NotifyManager.getInstance().unRegister(msgNotifyReceiver);
        ChatManager.getInstance().setChatController(null);
    }

    public void setContactEntity(ContactEntity contactEntity) {
        this.contactEntity = contactEntity;

        int index = 0;
        if (contactEntity.getType() == 0) {
            final FriendEntity friendEntity = contactEntity.getFriendEntity();
            currentContactUserLabel.setText(friendEntity.getName());
            if (friendEntityMap.containsKey(friendEntity.getId())) {
                index = findRecentContactIndex(contactEntity);
            } else {
                friendEntityMap.put(friendEntity.getId(), friendEntity);
                recentContactEntities.add(contactEntity);
                index = recentContactEntities.size() - 1;
            }
        } else {
            final GroupEntity groupEntity = contactEntity.getGroupEntity();
            currentContactUserLabel.setText(groupEntity.getName());
            if (groupEntityMap.containsKey(groupEntity.getId())) {
                index = findRecentContactIndex(contactEntity);
            } else {
                groupEntityMap.put(groupEntity.getId(), groupEntity);
                recentContactEntities.add(contactEntity);
                index = recentContactEntities.size() - 1;
            }
        }
        recentContactListView.getSelectionModel().select(index);

        final List<ChatMessage> chatMessageList =
                ChatManager.getInstance().getChatMessageList(contactEntity.getIdentifyKey());
        chatMessages.clear();
        if (!CollectionUtils.isEmpty(chatMessageList)) {
            chatMessages.addAll(chatMessageList);
        }
    }

    private int findRecentContactIndex(ContactEntity entity) {
        int index = 0;
        for (ContactEntity item : recentContactEntities) {
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

    static class ContactListCell extends ListCell<ContactEntity> {
        @Override
        protected void updateItem(ContactEntity item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                return;
            }

            HBox parent = FXMLUtil.loadFXML("item_recent_contact");
            final VBox vBox = (VBox) parent.getChildren().get(1);
            final VBox vBox2 = (VBox) parent.getChildren().get(2);
            final Label nameLabel = (Label) vBox.getChildren().get(0);
            final Label msgLabel = (Label) vBox.getChildren().get(1);
            final Label onlineLabel = (Label) vBox2.getChildren().get(0);
            nameLabel.setText(item.getContactName());
            msgLabel.setText(item.getLatestMessage());
            onlineLabel.setText(item.isOnline() ? "在线" : "离线");

            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

    static class MsgListCell extends ListCell<ChatMessage> {
        ContactEntity contactEntity;

        public MsgListCell(ContactEntity contactEntity) {
            this.contactEntity = contactEntity;
        }

        @Override
        protected void updateItem(ChatMessage item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                return;
            }

            final Long fromUserId = item.getFromUserId();
            final boolean isSend = Main.currentUser.getId() == fromUserId;
            VBox parent = FXMLUtil.loadFXML(isSend ? "item_msg_send" : "item_msg_receive");
            final Label timeLabel = (Label) parent.getChildren().get(0);
            final HBox hBox = (HBox) parent.getChildren().get(1);
            ImageView headImageView;
            Label msgLabel;
            String headUrl = null;
            boolean isGroup = item.getChatMessageType() == ChatMessageType.GROUP;
            if (isSend) {
                headImageView = (ImageView) hBox.getChildren().get(1);
                msgLabel = (Label) hBox.getChildren().get(0);
                headUrl = Main.currentUser.getHead();
            } else {
                headImageView = (ImageView) hBox.getChildren().get(0);
                msgLabel = (Label) hBox.getChildren().get(1);
                if (isGroup) {
                    final GroupEntity groupEntity = contactEntity.getGroupEntity();
                    for (UserEntity userEntity : groupEntity.getMembers()) {
                        if (userEntity.getId() == item.getFromUserId()) {
                            headUrl = userEntity.getHead();
                            break;
                        }
                    }
                } else {
                    headUrl = contactEntity.getFriendEntity().getHead();
                }
            }
            if (headUrl != null) {
                headImageView.setImage(new Image("http://localhost:9001" + headUrl));
            }
            timeLabel.setText(CommonUtil.getTimeStr(item.getSendTimestamp()));
            msgLabel.setText(ChatMessageUtil.getMsg(item));

            setGraphic(parent);
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(false);
        }
    }

}
