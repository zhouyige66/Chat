package cn.roy.chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;


/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/28 15:21
 * @Version: v1.0
 */
public class ChatController extends BaseController{
    @FXML
    ListView recentContactListView;
    @FXML
    Label currentContactUserLabel;
    @FXML
    TextArea msgTextArea;

    @Override
    public void init() {

    }

}
