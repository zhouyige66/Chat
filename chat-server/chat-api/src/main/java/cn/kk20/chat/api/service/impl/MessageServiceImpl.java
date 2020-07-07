package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.dao.mapper.MessageModelMapper;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.dao.model.MessageModelQuery;
import cn.kk20.chat.api.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:35
 * @Version: v1.0
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageModelMapper messageModelMapper;

    @Override
    @Transactional
    public int insert(MessageModel model) {
        int insertSelective = messageModelMapper.insertSelective(model);
        return insertSelective;
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
        return messageModelMapper.updateByPrimaryKeySelective(model);
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
