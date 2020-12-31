package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.base.exception.RequestParamException;
import cn.kk20.chat.dao.mapper.ApplyLogModelMapper;
import cn.kk20.chat.dao.mapper.GroupModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.ApplyLogModelQuery;
import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.api.service.ApplyLogService;
import cn.kk20.chat.api.service.MessageService;
import cn.kk20.chat.api.service.TransactionEventPublishService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/3 15:40
 * @Version: v1.0
 */
@Service
public class ApplyLogServiceImpl implements ApplyLogService {
    private static final Logger logger = LoggerFactory.getLogger(ApplyLogServiceImpl.class);

    @Autowired
    UserModelMapper userModelMapper;
    @Autowired
    GroupModelMapper groupModelMapper;
    @Autowired
    ApplyLogModelMapper applyLogModelMapper;

    @Override
    public List<ApplyLogModel> getApplyLogList(Long verifyUserId) throws Exception {
        UserModel userModel = userModelMapper.selectByPrimaryKey(verifyUserId);
        if (userModel == null || userModel.getIsDelete()) {
            throw new RequestParamException("审批人不存在或已删除");
        }

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
            throw new RequestParamException("申请类型为0-添加好友，1-添加群，不能为空");
        }
        Long applyUserId = model.getApplyUserId();
        Long targetUserId = model.getTargetUserId();
        UserModel applyUserModel = userModelMapper.selectByPrimaryKey(applyUserId);
        if (applyUserModel == null) {
            throw new RequestParamException("申请发起者不存在");
        }
        if (type == 0) {
            UserModel targetUserModel = userModelMapper.selectByPrimaryKey(targetUserId);
            if (targetUserModel == null || targetUserModel.getIsDelete()) {
                throw new RequestParamException("申请添加的好友不存在");
            }
            // 检查两者是否已经是好友了
            String targetUserFriends = targetUserModel.getFriendList();
            if (!StringUtils.isEmpty(targetUserFriends)) {
                Set<Long> targetUserFriendSet = JSON.parseObject(targetUserFriends, new TypeReference<Set<Long>>() {
                });
                if (targetUserFriendSet.contains(applyUserModel.getId())) {
                    String applyUserFriends = applyUserModel.getFriendList();
                    Set<Long> applyUserFriendSet;
                    if (StringUtils.isEmpty(applyUserFriends)) {
                        applyUserFriendSet = new HashSet<>();
                    } else {
                        applyUserFriendSet = JSON.parseObject(applyUserFriends, new TypeReference<Set<Long>>() {
                        });
                    }
                    if (!applyUserFriendSet.contains(targetUserId)) {
                        // 更新申请者的好友列表
                        applyUserFriendSet.add(targetUserId);
                        applyUserModel.setFriendList(JSON.toJSONString(applyUserFriends));
                        userModelMapper.updateByPrimaryKeySelective(applyUserModel);
                    }
                    throw new RequestParamException("申请添加的好友已经存在，不必重复添加");
                }
            }
            model.setVerifyUserId(targetUserModel.getId());
        } else {
            GroupModel targetGroupModel = groupModelMapper.selectByPrimaryKey(targetUserId);
            if (targetGroupModel == null || targetGroupModel.getIsDelete()) {
                throw new RequestParamException("申请加入的群不存在");
            }
            // 检查申请人是否已经在群成员中
            String groupMembers = targetGroupModel.getMemberList();
            if (StringUtils.isEmpty(groupMembers)) {
                Set<Long> memberSet = JSON.parseObject(groupMembers, new TypeReference<Set<Long>>() {
                });
                if (memberSet.contains(applyUserId)) {
                    String applyUserGroups = applyUserModel.getGroupList();
                    Set<Long> applyUserGroupSet;
                    if (StringUtils.isEmpty(applyUserGroups)) {
                        applyUserGroupSet = new HashSet<>();
                    } else {
                        applyUserGroupSet = JSON.parseObject(applyUserGroups, new TypeReference<Set<Long>>() {
                        });
                    }
                    if (!applyUserGroupSet.contains(targetUserId)) {
                        applyUserGroupSet.add(targetUserId);
                        applyUserModel.setGroupList(JSON.toJSONString(applyUserGroupSet));
                        userModelMapper.updateByPrimaryKeySelective(applyUserModel);
                    }
                    throw new RequestParamException("申请人已经是该群的成员了");
                }
            }
            model.setVerifyUserId(targetGroupModel.getCreatorId());
        }

        // 处理申请记录
        ApplyLogModelQuery query = new ApplyLogModelQuery();
        query.setOrderByClause("`id` DESC");
        ApplyLogModelQuery.Criteria criteria = query.createCriteria();
        criteria.andIsDeleteEqualTo(false);
        criteria.andApplyUserIdEqualTo(applyUserId);
        criteria.andTargetUserIdEqualTo(targetUserId);
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

    @Autowired
    MessageService messageService;
    @Autowired
    TransactionEventPublishService transactionEventPublishService;

    @Override
    @Transactional
    public void verifyApply(ApplyLogModel model) throws Exception {
//        transactionEventPublishService.publish("主事务发送");

        ApplyLogModel existModel = applyLogModelMapper.selectByPrimaryKey(model.getId());
        if (existModel == null) {
            String msg = String.format("审批申请：id=%d的申请申请记录不存在", model.getId());
            throw new RequestParamException(msg);
        }
        if (existModel.getIsDelete()) {
            String msg = String.format("审批申请：id=%d的申请申请记录已经审批过，无需重复审批", model.getId());
            throw new RequestParamException(msg);
        }
        if (!existModel.getVerifyUserId().equals(model.getVerifyUserId())) {
            String msg = String.format("审批申请：id=%d的申请记录的审批人不符", model.getId());
            throw new RequestParamException(msg);
        }

        Long applyUserId = existModel.getApplyUserId();
        Long targetUserId = existModel.getTargetUserId();
        Boolean isAgree = model.getIsAgree();
        // 更新申请表数据
        existModel.setIsAgree(isAgree);
        existModel.setModifyDate(new Date());
        existModel.setVerifyRemark(model.getVerifyRemark());
        existModel.setIsDelete(true);// 审批后，直接置删除标志为1
        existModel.setModifyDate(null);// mysql自动更新时间
        applyLogModelMapper.updateByPrimaryKeySelective(existModel);

        // 同意添加好友或加入群
        if (isAgree) {
//            transactionEventPublishService.publish("主事务发送2");
//            int i = 0;
//            System.out.println("跑出异常：" + 100 / i);

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
                String memberList = groupModel.getMemberList();
                Set<Long> memberSet;
                if (StringUtils.isEmpty(memberList)) {
                    memberSet = new HashSet<>();
                } else {
                    memberSet = JSON.parseObject(memberList, new TypeReference<Set<Long>>() {
                    });
                }
                memberSet.add(applyUserId);
                groupModel.setMemberList(JSON.toJSONString(memberSet));
                groupModel.setModifyDate(null);
                groupModelMapper.updateByPrimaryKeySelective(groupModel);
                // 更新用户的群列表
                String groupList = applyUserModel.getGroupList();
                Set<Long> groupSet;
                if (StringUtils.isEmpty(groupList)) {
                    groupSet = new HashSet<>();
                } else {
                    groupSet = JSON.parseObject(groupList, new TypeReference<Set<Long>>() {
                    });
                }
                groupSet.add(targetUserId);
                applyUserModel.setGroupList(JSON.toJSONString(groupSet));
                userModelMapper.updateByPrimaryKeySelective(applyUserModel);
            }
        }
    }

    private void updateUserFriends(UserModel userModel, Long friendId, boolean add) {
        String friendList = userModel.getFriendList();
        Set<Long> friendSet;
        if (StringUtils.isEmpty(friendList)) {
            friendSet = new HashSet<>();
        } else {
            friendSet = JSON.parseObject(friendList, new TypeReference<Set<Long>>() {
            });
        }
        if (add) {
            friendSet.add(friendId);
        } else {
            friendSet.remove(friendId);
        }
        userModel.setFriendList(JSON.toJSONString(friendSet));
        userModel.setModifyDate(null);
        userModelMapper.updateByPrimaryKeySelective(userModel);
    }

}
