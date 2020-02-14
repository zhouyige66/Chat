package cn.kk20.chat.service;

import cn.kk20.chat.model.MessageModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:33
 * @Version: v1.0
 */
public interface MessageService {
    int save(MessageModel model);

    int delete(String id);

    int update(MessageModel model);

    MessageModel find(String id);

    List<MessageModel> selectAll();
}
