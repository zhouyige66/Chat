package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.GroupService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

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
    @Transactional(rollbackFor = Exception.class)
    public void create(GroupModel model) throws Exception {
        if (model == null) {
            throw new Exception("参数有误");
        }
        Long creator = model.getCreator();
        UserModel userModel = userModelMapper.selectByPrimaryKey(creator);
        if (userModel == null || userModel.getIsDelete()) {
            throw new Exception("创建群的人员不存在或已注销");
        }

        // 添加默认群成员
        HashSet<Object> memberSet = new HashSet<>();
        memberSet.add(model.getCreator());
        model.setMembers(JSON.toJSONString(memberSet));
        groupModelMapper.insertSelective(model);

        // 添加创建人的群列表
        String groupList = userModel.getGroupList();
        Set<Long> groupSet;
        if(StringUtils.isEmpty(groupList)){
            groupSet =new HashSet<>();
        }else {
            groupSet = JSON.parseObject(groupList,new TypeReference<Set<Long>>(){});
        }
        groupSet.add(model.getId());
        userModel.setGroupList(JSON.toJSONString(groupSet));
        userModel.setModifyDate(null);
        userModelMapper.updateByPrimaryKeySelective(userModel);
    }

}