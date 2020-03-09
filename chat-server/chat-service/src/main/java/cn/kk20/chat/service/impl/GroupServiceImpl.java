package cn.kk20.chat.service.impl;

import cn.kk20.chat.base.exception.RequestParamException;
import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.GroupModelQuery;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.GroupService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            throw new RequestParamException("参数有误");
        }
        Long creatorId = model.getCreatorId();
        UserModel userModel = userModelMapper.selectByPrimaryKey(creatorId);
        if (userModel == null || userModel.getIsDelete()) {
            throw new RequestParamException("创建群的人员不存在或已注销");
        }

        // 添加默认群成员
        HashSet<Object> memberSet = new HashSet<>();
        memberSet.add(creatorId);
        model.setMemberList(JSON.toJSONString(memberSet));
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

    @Override
    public List<GroupModel> getGroupList(Long userId) throws Exception {
        UserModel userModel = userModelMapper.selectByPrimaryKey(userId);
        if (userModel == null || userModel.getIsDelete()) {
            throw new RequestParamException("查询群列表的人员不存在或已注销");
        }
        String groupList = userModel.getGroupList();
        if(StringUtils.isEmpty(groupList)){
            return new ArrayList<>(0);
        }

        Set<Long> groupSet = JSON.parseObject(groupList, new TypeReference<Set<Long>>() {
        });
        GroupModelQuery query = new GroupModelQuery();
        query.createCriteria().andIdIn(groupSet.stream().collect(Collectors.toList()));
        List<GroupModel> groupModelList = groupModelMapper.selectByCondition(query);
        return groupModelList;
    }

}
