package cn.kk20.chat.service;


import cn.kk20.chat.dao.model.UserModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:53
 * @Version: v1.0
 */
public interface UserService {
    UserModel save(UserModel model);

    int delete(String id);

    int update(UserModel model);

    UserModel find(String id);

    UserModel find(String name, String password);

    List<UserModel> selectAll();
}
