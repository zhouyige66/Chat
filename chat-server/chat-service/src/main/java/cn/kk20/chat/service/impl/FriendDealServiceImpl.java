package cn.kk20.chat.service.impl;

import cn.kk20.chat.dao.mapper.AddFriendLogModelMapper;
import cn.kk20.chat.dao.mapper.UserModelMapper;
import cn.kk20.chat.dao.model.AddFriendLogModel;
import cn.kk20.chat.dao.model.AddFriendLogModelQuery;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.FriendDealService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class FriendDealServiceImpl implements FriendDealService {
    @Autowired
    UserModelMapper userModelMapper;

    @Autowired
    AddFriendLogModelMapper addFriendLogModelMapper;

    @Override
    public List<AddFriendLogModel> getApplyAddFriendLogList(Long targetUserId) {
        AddFriendLogModelQuery query = new AddFriendLogModelQuery();
        query.createCriteria().andToUserIdEqualTo(targetUserId).andIsAgreeEqualTo(false);
        List<AddFriendLogModel> addFriendLogModels = addFriendLogModelMapper.selectByCondition(query);
        return addFriendLogModels;
    }

    @Override
    public void applyAddFriend(Long applyUserId, Long targetUserId) throws Exception {
        // 校验用户是否存在
        UserModel userModel = userModelMapper.selectByPrimaryKey(applyUserId);
        if (userModel == null) {
            throw new Exception("申请添加好友的用户不存在");
        }
        UserModel userModel2 = userModelMapper.selectByPrimaryKey(targetUserId);
        if (userModel2 == null) {
            throw new Exception("申请添加的好友不存在");
        }

        // 查询申请记录表
        AddFriendLogModelQuery query = new AddFriendLogModelQuery();
        query.setOrderByClause("id");
        AddFriendLogModelQuery.Criteria criteria = query.createCriteria();
        criteria.andFromUserIdEqualTo(applyUserId);
        criteria.andToUserIdEqualTo(targetUserId);
        List<AddFriendLogModel> addFriendLogModels = addFriendLogModelMapper.selectByCondition(query);
        if (CollectionUtils.isEmpty(addFriendLogModels)) {
            AddFriendLogModel model = new AddFriendLogModel();
            model.setFromUserId(applyUserId);
            model.setToUserId(targetUserId);
            model.setIsAgree(false);
            addFriendLogModelMapper.insertSelective(model);
        } else {
            // 去除冗余的记录
            if (addFriendLogModels.size() > 1) {
                for (int i = 1; i < addFriendLogModels.size(); i++) {
                    addFriendLogModelMapper.deleteByPrimaryKey(addFriendLogModels.get(i).getId());
                }
            }
            // 更新修改时间
            AddFriendLogModel existModel = addFriendLogModels.get(0);
            existModel.setModifyDate(new Date());
            existModel.setIsAgree(false);
            addFriendLogModelMapper.updateByPrimaryKey(existModel);
        }
    }

    @Override
    public void agreeAddFriend(Long applyId, Long targetUserId) throws Exception {
        AddFriendLogModel addFriendLogModel = addFriendLogModelMapper.selectByPrimaryKey(applyId);
        if (addFriendLogModel == null || !addFriendLogModel.getToUserId().equals(targetUserId)) {
            String msg = String.format("申请添加好友的记录id=%d，目标id=%d，数据有错", applyId, targetUserId);
            throw new Exception(msg);
        }
        // 更新申请表数据
        addFriendLogModel.setIsAgree(true);
        addFriendLogModel.setModifyDate(new Date());
        addFriendLogModelMapper.updateByPrimaryKeySelective(addFriendLogModel);
        // 同时更新自己作为申请人，添加对方为好友的申请，自动更改为同意
        Long applyUserId = addFriendLogModel.getFromUserId();
        AddFriendLogModelQuery query = new AddFriendLogModelQuery();
        query.setOrderByClause("id DESC");
        query.createCriteria().andFromUserIdEqualTo(targetUserId).andToUserIdEqualTo(applyUserId);
        List<AddFriendLogModel> addFriendLogModels = addFriendLogModelMapper.selectByCondition(query);
        if (!CollectionUtils.isEmpty(addFriendLogModels)) {
            if (addFriendLogModels.size() > 1) {
                for (int i = 0; i < addFriendLogModels.size(); i++) {
                    addFriendLogModelMapper.deleteByPrimaryKey(addFriendLogModels.get(i).getId());
                }
            }
            AddFriendLogModel existModel = addFriendLogModels.get(0);
            existModel.setIsAgree(true);
            existModel.setModifyDate(new Date());
            addFriendLogModelMapper.updateByPrimaryKeySelective(existModel);
        }

        // 更新用户的好友信息
        UserModel applyUser = userModelMapper.selectByPrimaryKey(applyUserId);
        updateUserFriends(applyUser, targetUserId, true);
        UserModel targetUser = userModelMapper.selectByPrimaryKey(targetUserId);
        updateUserFriends(targetUser, applyUserId, true);
    }

    private void updateUserFriends(UserModel userModel, Long friendId, boolean add) {
        String friends = userModel.getFriend();
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
        userModel.setFriend(JSON.toJSONString(longs));
        userModelMapper.updateByPrimaryKey(userModel);
    }

    @Override
    public void deleteFriend(Long applyUserId, Long targetUserId) {

    }

}
