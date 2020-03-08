package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.ApplyLogModelMapper;
import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.ApplyLogModelQuery;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.ApplyLogService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/3 15:40
 * @Version: v1.0
 */
@Service
public class ApplyLogServiceImpl implements ApplyLogService {
    @Autowired
    UserModelMapper userModelMapper;
    @Autowired
    GroupModelMapper groupModelMapper;
    @Autowired
    ApplyLogModelMapper applyLogModelMapper;

    @Override
    public List<ApplyLogModel> getApplyLogList(Long verifyUserId) {
        ApplyLogModelQuery query = new ApplyLogModelQuery();
        query.createCriteria().andVerifyUserIdEqualTo(verifyUserId).andIsDeleteEqualTo(false);
        List<ApplyLogModel> applyLogModelList = applyLogModelMapper.selectByCondition(query);
        return applyLogModelList;
    }

    @Override
    public void addApply(ApplyLogModel model) throws Exception {
        // 校验用户是否存在
        Integer type = model.getType();
        if (type == null) {
            throw new Exception("申请类型为0-添加好友，1-添加群，不能为空");
        }

        UserModel applyUserModel = userModelMapper.selectByPrimaryKey(model.getApplyUserId());
        if (applyUserModel == null) {
            throw new Exception("申请发起者不存在");
        }
        if (type == 0) {
            UserModel targetUserModel = userModelMapper.selectByPrimaryKey(model.getTargetUserId());
            if (targetUserModel == null || targetUserModel.getIsDelete()) {
                throw new Exception("申请添加的好友不存在");
            }
            model.setVerifyUserId(targetUserModel.getId());
        } else {
            GroupModel targetGroupModel = groupModelMapper.selectByPrimaryKey(model.getTargetUserId());
            if (targetGroupModel == null || targetGroupModel.getIsDelete()) {
                throw new Exception("申请加入的群不存在");
            }
            model.setVerifyUserId(targetGroupModel.getCreator());
        }

        // 处理申请记录
        ApplyLogModelQuery query = new ApplyLogModelQuery();
        query.setOrderByClause("`id` DESC");
        ApplyLogModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIsDeleteEqualTo(false);
        criteria.andApplyUserIdEqualTo(model.getApplyUserId());
        criteria.andTargetUserIdEqualTo(model.getTargetUserId());
        List<ApplyLogModel> applyLogModelList = applyLogModelMapper.selectByCondition(query);
        if (!CollectionUtils.isEmpty(applyLogModelList)) {
            // 去除冗余的记录
            for (ApplyLogModel existModel : applyLogModelList) {
                existModel.setIsDelete(true);
                applyLogModelMapper.updateByPrimaryKeySelective(existModel);
            }
        }
        applyLogModelMapper.insertSelective(model);
    }

    @Override
    public void verifyApply(ApplyLogModel model) throws Exception {
        ApplyLogModel existModel = applyLogModelMapper.selectByPrimaryKey(model.getId());
        if (existModel == null) {
            String msg = String.format("申请记录数据：记录id=%d的申请记录不存在", model.getId());
            throw new Exception(msg);
        }
        if (!existModel.getVerifyUserId().equals(model.getVerifyUserId())) {
            String msg = String.format("申请记录数据：记录id=%d的申请的审批人不符", model.getId());
            throw new Exception(msg);
        }

        Long applyUserId = existModel.getApplyUserId();
        Long targetUserId = existModel.getTargetUserId();
        Boolean isAgree = model.getIsAgree();
        // 更新申请表数据
        existModel.setIsAgree(isAgree);
        existModel.setModifyDate(new Date());
        existModel.setRemark(model.getRemark());
        applyLogModelMapper.updateByPrimaryKeySelective(existModel);

        // 同意添加好友或加入群
        if (isAgree) {
            UserModel applyUserModel = userModelMapper.selectByPrimaryKey(applyUserId);
            if (model.getType() == 0) {
                // 更新用户的好友信息
                UserModel applyUser = applyUserModel;
                updateUserFriends(applyUser, targetUserId, true);
                UserModel targetUser = userModelMapper.selectByPrimaryKey(targetUserId);
                updateUserFriends(targetUser, applyUserId, true);
            } else {
                // 更新群成员
                GroupModel groupModel = groupModelMapper.selectByPrimaryKey(targetUserId);
                String members = groupModel.getMembers();
                Set<Long> memberSet;
                if (StringUtils.isEmpty(members)) {
                    memberSet = new HashSet<>();
                } else {
                    memberSet = JSON.parseObject(members, new TypeReference<Set<Long>>() {
                    });
                }
                memberSet.add(applyUserId);
                groupModel.setMembers(JSON.toJSONString(memberSet));
                groupModel.setModifyDate(null);
                groupModelMapper.updateByPrimaryKeySelective(groupModel);
                // 更新用户的群列表
                String groups = applyUserModel.getGroupList();
                Set<Long> groupSet;
                if (StringUtils.isEmpty(groups)) {
                    groupSet = new HashSet<>();
                } else {
                    groupSet = JSON.parseObject(members, new TypeReference<Set<Long>>() {
                    });
                }
                groupSet.add(targetUserId);
                applyUserModel.setGroupList(JSON.toJSONString(groupSet));
                userModelMapper.updateByPrimaryKeySelective(applyUserModel);
            }
        }
    }

    private void updateUserFriends(UserModel userModel, Long friendId, boolean add) {
        String friends = userModel.getFriends();
        Set<Long> longs = JSON.parseObject(friends, new TypeReference<Set<Long>>() {
        });
        if (CollectionUtils.isEmpty(longs)) {
            longs = new HashSet<>();
        }
        if (add) {
            longs.add(friendId);
        } else {
            longs.remove(friendId);
        }
        userModel.setFriends(JSON.toJSONString(longs));
        userModelMapper.updateByPrimaryKeySelective(userModel);
    }

}
