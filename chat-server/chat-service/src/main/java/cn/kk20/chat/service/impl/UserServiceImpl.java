package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.dao.model.UserModelQuery;
import cn.kk20.chat.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    public int delete(Long id) {
        return userModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(UserModel model) {
        return userModelMapper.updateByPrimaryKey(model);
    }

    @Override
    public UserModel find(Long id) {
        return userModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserModel> selectAll() {
        UserModelQuery query = new UserModelQuery();
        UserModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIsNotNull();
        return userModelMapper.selectByCondition(query);
    }

    @Override
    public UserModel register(UserModel model) throws Exception {
        UserModelQuery query = new UserModelQuery();
        query.createCriteria().andNameEqualTo(model.getName());
        List<UserModel> userModelList = userModelMapper.selectByCondition(query);
        if (!CollectionUtils.isEmpty(userModelList)) {
            throw new Exception("该用户名已经被占用，请使用其他用户名");
        }
        query.getOredCriteria().clear();
        query.createCriteria().andPhoneEqualTo(model.getPhone());
        List<UserModel> userModelList2 = userModelMapper.selectByCondition(query);
        if (!CollectionUtils.isEmpty(userModelList2)) {
            throw new Exception("该手机已被注册，请检查后重试");
        }
        query.getOredCriteria().clear();
        query.createCriteria().andEmailEqualTo(model.getEmail());
        List<UserModel> userModelList3 = userModelMapper.selectByCondition(query);
        if (!CollectionUtils.isEmpty(userModelList3)) {
            throw new Exception("该邮箱已被注册，请检查后重试");
        }

        userModelMapper.insertSelective(model);
        return model;
    }

    @Override
    public UserModel login(String name, String password) throws Exception {
        UserModelQuery query = new UserModelQuery();
        UserModelQuery.Criteria criteria = query.createCriteria();
        UserModelQuery.Criteria criteria2 = query.or();
        UserModelQuery.Criteria criteria3 = query.or();
        criteria.andNameEqualTo(name);
        criteria2.andPhoneEqualTo(name);
        criteria3.andEmailEqualTo(name);
        List<UserModel> userModels = userModelMapper.selectByCondition(query);
        if (CollectionUtils.isEmpty(userModels)) {
            throw new Exception("登录用户不存在，请先注册");
        }

        return userModels.get(0);
    }

    @Override
    public List<UserModel> search(String key) {
        UserModelQuery query = new UserModelQuery();
        query.createCriteria().andNameLike(key);
        query.or().andPhoneLike(key);
        try {
            long l = Long.parseLong(key);
            query.or().andIdEqualTo(l);
        } catch (Exception e) {
            // 不能转换
        }
        List<UserModel> userModelList = userModelMapper.selectByCondition(query);
        return userModelList;
    }

    @Override
    public List<UserModel> getFriendList(Long userId) {
        UserModel userModel = userModelMapper.selectByPrimaryKey(userId);
        String friends = userModel.getFriends();
        List<Long> friendIdList = JSON.parseObject(friends, new TypeReference<List<Long>>() {
        });
        UserModelQuery query = new UserModelQuery();
        UserModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIn(friendIdList);
        List<UserModel> userModelList = userModelMapper.selectByCondition(query);
        List<UserModel> friendList = userModelList.stream().map(e -> {
            // 去除不必要属性
            e.setPassword(null);
            e.setGroups(null);
            e.setFriends(null);
            e.setCreateDate(null);
            e.setModifyDate(null);
            return e;
        }).collect(Collectors.toList());

        return friendList;
    }

}
