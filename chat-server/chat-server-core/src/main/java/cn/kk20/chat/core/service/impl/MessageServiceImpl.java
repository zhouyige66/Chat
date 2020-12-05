package cn.kk20.chat.core.service.impl;

import cn.kk20.chat.core.service.MessageService;
import cn.kk20.chat.dao.mapper.GroupMessageModelMapper;
import cn.kk20.chat.dao.mapper.MessageModelMapper;
import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/7/9 10:52
 * @Version: v1.0
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private GroupMessageModelMapper groupMessageModelMapper;
    @Autowired
    private MessageModelMapper messageModelMapper;

    @Override
    public void saveGroupMessage(GroupMessageModel model) {
        groupMessageModelMapper.insertSelective(model);
    }

    @Override
    public void saveSingleMessage(MessageModel model) {
        messageModelMapper.insertSelective(model);
    }

}
