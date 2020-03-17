package cn.kk20.chat.service;

import cn.kk20.chat.dao.model.GroupMessageModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 17:33
 * @Version: v1.0
 */
public interface GroupMessageService {
    int insert(GroupMessageModel model);

    int delete(Long id);

    int batchDelete(List<Long> ids);

    int update(GroupMessageModel model);

    GroupMessageModel find(Long id);

    List<GroupMessageModel> selectAll();
}
