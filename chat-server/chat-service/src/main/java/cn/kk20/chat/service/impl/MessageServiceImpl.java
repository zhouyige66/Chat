package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.MessageModelMapper;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.dao.model.MessageModelQuery;
import cn.kk20.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:35
 * @Version: v1.0
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageModelMapper messageModelMapper;

    @Override
    public int save(MessageModel model) {
        return messageModelMapper.insertSelective(model);
    }

    @Override
    public int delete(Long id) {
        return messageModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelete(List<Long> ids) {
        MessageModelQuery query = new MessageModelQuery();
        query.createCriteria().andIdIn(ids);
        int i = messageModelMapper.deleteByCondition(query);
        return i;
    }

    @Override
    public int update(MessageModel model) {
        return messageModelMapper.updateByPrimaryKey(model);
    }

    @Override
    public MessageModel find(Long id) {
        return messageModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MessageModel> selectAll() {
        MessageModelQuery query = new MessageModelQuery();
        MessageModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIsNotNull();
        List<MessageModel> messageModels = messageModelMapper.selectByCondition(query);
        return messageModels;
    }

}
