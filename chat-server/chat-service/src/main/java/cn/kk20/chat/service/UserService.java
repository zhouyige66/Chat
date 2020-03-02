package cn.kk20.chat.service;

import cn.kk20.chat.dao.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:53
 * @Version: v1.0
 */
@Service
public interface UserService {
    UserModel save(UserModel model) throws Exception;

    int delete(Long id);

    int update(UserModel model);

    UserModel find(Long id);

    UserModel find(String name, String password) throws Exception;

    List<UserModel> selectAll();

    List<UserModel> getFriendList(Long userId);

}
