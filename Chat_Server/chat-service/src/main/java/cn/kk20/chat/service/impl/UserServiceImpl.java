package cn.kk20.chat.service.impl;

import cn.kk20.chat.service.UserService;
import cn.kk20.chat.mapper.UserModelMapper;
import cn.kk20.chat.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: Roy Z
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
            userModelMapper.insert(model);
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
        UserModel userModel = userModelMapper.selectByPassword(name, password);
        if (userModel == null) {
            userModel = userModelMapper.selectByName(name);
        }
        // 临时调试测试，实际部署会读取数据库数据
//        UserModel userModel = new UserModel();
//        userModel.setName(name);
//        userModel.setPassword(password);
        return userModel;
    }

    @Override
    public List<UserModel> selectAll() {
        return userModelMapper.selectAll();
    }

}
