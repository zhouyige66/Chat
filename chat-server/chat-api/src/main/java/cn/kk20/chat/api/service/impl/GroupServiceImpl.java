package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.api.entity.vo.GroupVo;
import cn.kk20.chat.api.entity.vo.UserVo;
import cn.kk20.chat.base.exception.RequestParamException;
import cn.kk20.chat.base.util.ListUtil;
import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.GroupModelQuery;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.dao.model.UserModelQuery;
import cn.kk20.chat.api.service.GroupService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
    public List<GroupModel> selectAll() throws Exception {
        GroupModelQuery query = new GroupModelQuery();
        query.createCriteria().andIdIsNotNull().andIsDeleteEqualTo(false);
        List<GroupModel> groupModelList = groupModelMapper.selectByConditionWithBLOBs(query);
        return groupModelList;
    }

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
        if (StringUtils.isEmpty(groupList)) {
            groupSet = new HashSet<>();
        } else {
            groupSet = JSON.parseObject(groupList, new TypeReference<Set<Long>>() {
            });
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
        if (StringUtils.isEmpty(groupList)) {
            return ListUtil.emptyList();
        }
        Set<Long> groupSet = JSON.parseObject(groupList, new TypeReference<Set<Long>>() {
        });
        GroupModelQuery query = new GroupModelQuery();
        query.createCriteria().andIdIn(new ArrayList<>(groupSet));
        List<GroupModel> groupModelList = groupModelMapper.selectByConditionWithBLOBs(query);
        return groupModelList;
    }

    @Override
    public List<GroupVo> getGroupListWithMember(Long userId) throws Exception {
        List<GroupModel> groupModelList = getGroupList(userId);
        if (CollectionUtils.isEmpty(groupModelList)) {
            return ListUtil.emptyList();
        }
        List<GroupVo> groupVoList = new ArrayList<>();
        // 查找群成员
        for (GroupModel model : groupModelList) {
            GroupVo groupVo = new GroupVo();
            BeanUtils.copyProperties(model,groupVo);
            groupVoList.add(groupVo);

            String memberList = model.getMemberList();
            if (StringUtils.isEmpty(memberList)) {
                continue;
            }

            List<UserVo> userVoList = getGroupMemberList(model.getId());
            groupVo.setMemberList(userVoList);
        }

        return groupVoList;
    }

    @Override
    public List<UserVo> getGroupMemberList(Long groupId) throws Exception {
        if (groupId == null) {
            throw new RequestParamException("群组ID不能为空");
        }
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(groupId);
        if (groupModel == null) {
            throw new RequestParamException("该群组ID不存在");
        }
        String memberList = groupModel.getMemberList();
        if (StringUtils.isEmpty(memberList)) {
            return ListUtil.emptyList();
        }

        Set<Long> memberSet = JSON.parseObject(memberList, new TypeReference<Set<Long>>() {
        });
        UserModelQuery query = new UserModelQuery();
        query.createCriteria().andIdIn(memberSet.stream().collect(Collectors.toList()));
        List<UserModel> userModelList = userModelMapper.selectByCondition(query);
        List<UserVo> userVoList = new ArrayList<>();
        for (UserModel userModel : userModelList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userModel, userVo);
            userVoList.add(userVo);
        }
        return userVoList;
    }

}
