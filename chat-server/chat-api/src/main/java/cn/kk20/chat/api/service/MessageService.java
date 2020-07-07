package cn.kk20.chat.api.service;

import cn.kk20.chat.dao.model.MessageModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:33
 * @Version: v1.0
 */
public interface MessageService {
    int insert(MessageModel model);

    int delete(Long id);

    int batchDelete(List<Long> ids);

    int update(MessageModel model);

    MessageModel find(Long id);

    List<MessageModel> selectAll();
}
