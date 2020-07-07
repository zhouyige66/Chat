package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.dao.mapper.GroupMessageModelMapper;
import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.GroupMessageModelQuery;
import cn.kk20.chat.api.service.GroupMessageService;
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
public class GroupMessageServiceImpl implements GroupMessageService {
    @Autowired
    GroupMessageModelMapper groupMessageModelMapper;

    @Override
    public int insert(GroupMessageModel model) {
        return groupMessageModelMapper.insertSelective(model);
    }

    @Override
    public int delete(Long id) {
        return groupMessageModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelete(List<Long> ids) {
        GroupMessageModelQuery query = new GroupMessageModelQuery();
        query.createCriteria().andIdIn(ids);
        int i = groupMessageModelMapper.deleteByCondition(query);
        return i;
    }

    @Override
    public int update(GroupMessageModel model) {
        return groupMessageModelMapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public GroupMessageModel find(Long id) {
        return groupMessageModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<GroupMessageModel> selectAll() {
        GroupMessageModelQuery query = new GroupMessageModelQuery();
        GroupMessageModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIsNotNull();
        List<GroupMessageModel> messageModels = groupMessageModelMapper.selectByCondition(query);
        return messageModels;
    }

}
