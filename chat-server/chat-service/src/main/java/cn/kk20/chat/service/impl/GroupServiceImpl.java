package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/6 16:07
 * @Version: v1.0
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    UserModelMapper userModelMapper;
    @Autowired
    GroupModelMapper groupModelMapper;

    @Override
    public void create(GroupModel model) throws Exception {
        if (model == null) {
            throw new Exception("参数有误");
        }

        Long creator = model.getCreator();
        UserModel userModel = userModelMapper.selectByPrimaryKey(creator);
        if (userModel == null || userModel.getIsDelete()) {
            throw new Exception("创建群的人员不存在或已注销");
        }

        groupModelMapper.insertSelective(model);
    }

}
