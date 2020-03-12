package cn.kk20.chat.service;

import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.UserModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/6 16:06
 * @Version: v1.0
 */
public interface GroupService {

    List<GroupModel> selectAll() throws Exception;

    void create(GroupModel model) throws Exception;

    List<GroupModel> getGroupList(Long userId) throws Exception;

    List<UserModel> getGroupMemberList(Long groupId) throws Exception;

}
