package cn.kk20.chat.service;

import cn.kk20.chat.dao.model.MessageModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:33
 * @Version: v1.0
 */
@Service
public interface MessageService {
    int save(MessageModel model);

    int delete(Long id);

    int update(MessageModel model);

    MessageModel find(Long id);

    List<MessageModel> selectAll();
}
