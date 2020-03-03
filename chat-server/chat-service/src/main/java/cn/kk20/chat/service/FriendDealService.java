package cn.kk20.chat.service;

import cn.kk20.chat.dao.model.AddFriendLogModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/3 15:31
 * @Version: v1.0
 */
public interface FriendDealService {

    /**
     * 获取需要审批的添加好友记录
     *
     * @param targetUserId
     * @return
     */
    List<AddFriendLogModel> getApplyAddFriendLogList(Long targetUserId);

    /**
     * 申请添加好友
     *
     * @param applyUserId 发起人
     * @param targetUserId 添加好友id
     */
    void applyAddFriend(Long applyUserId, Long targetUserId) throws Exception;

    /**
     * 统一添加好友
     *
     * @param applyId 申请记录ID
     * @param targetUserId 同意添加好友的当事人
     */
    void agreeAddFriend(Long applyId,Long targetUserId) throws Exception;

    /**
     * 删除好友
     *
     * @param applyUserId
     * @param targetUserId
     */
    void deleteFriend(Long applyUserId, Long targetUserId);

}
