package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.dao.model.UserModelQuery;
import cn.kk20.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:54
 * @Version: v1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserModelMapper userModelMapper;

    @Override
    public UserModel save(UserModel model) {
        // 判断注册的用户是否存在
        UserModel searchResult = find(model.getName(), model.getPassword());
        if (searchResult == null) {
            model.setId(UUID.randomUUID().toString());
            userModelMapper.insertSelective(model);
            return model;
        } else {
            return null;
        }
    }

    @Override
    public int delete(String id) {
        return userModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(UserModel model) {
        return userModelMapper.updateByPrimaryKey(model);
    }

    @Override
    public UserModel find(String id) {
        return userModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public UserModel find(String name, String password) {
        UserModelQuery query = new UserModelQuery();
        UserModelQuery.Criteria criteria = query.createCriteria();
        criteria.andNameEqualTo(name).andPasswordEqualTo(password);
        List<UserModel> userModels = userModelMapper.selectByCondition(query);
        if (CollectionUtils.isEmpty(userModels)) {
            return null;
        }

        return userModels.get(0);
    }

    @Override
    public List<UserModel> selectAll() {
        UserModelQuery query = new UserModelQuery();
        UserModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIsNotNull();
        return userModelMapper.selectByCondition(query);
    }

}
