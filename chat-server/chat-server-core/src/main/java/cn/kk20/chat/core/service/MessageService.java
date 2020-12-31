package cn.kk20.chat.core.service;

import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.MessageModel;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/7/9 10:50
 * @Version: v1.0
 */
public interface MessageService {

    void saveGroupMessage(GroupMessageModel model);

    void saveSingleMessage(MessageModel model);

}
