package cn.kk20.chat.service.impl;

import cn.kk20.chat.service.MessageService;
import cn.kk20.chat.mapper.MessageModelMapper;
import cn.kk20.chat.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019-01-29 17:35
 * @Version: v1.0
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageModelMapper messageModelMapper;

    @Override
    public int save(MessageModel model) {
        return messageModelMapper.insert(model);
    }

    @Override
    public int delete(String id) {
        return messageModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(MessageModel model) {
        return messageModelMapper.updateByPrimaryKey(model);
    }

    @Override
    public MessageModel find(String id) {
        return messageModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MessageModel> selectAll() {
        return messageModelMapper.selectAll();
    }
}
