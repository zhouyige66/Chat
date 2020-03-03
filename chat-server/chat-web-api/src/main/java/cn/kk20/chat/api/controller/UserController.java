package cn.kk20.chat.api.controller;

import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.http.dto.ListDto;
import cn.kk20.chat.base.http.dto.SimpleDto;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:57
 * @Version: v1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin // 跨越支持
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String sayHello() {
        return "hello";
    }

    @PostMapping(value = "/register")
    public ResultData register(@RequestBody UserModel userModel) {
        UserModel insertResult = null;
        try {
            UserModel result = userService.save(userModel);
            return ResultData.success(new SimpleDto().setValue("注册成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(101, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResultData login(@RequestBody UserModel user) {
        return login(user.getName(), user.getPassword());
    }

    @GetMapping("/login/{name}/{password}")
    public ResultData login(@PathVariable("name") String name, @PathVariable("password") String password) {
        try {
            UserModel userModel = userService.find(name, password);
            if (!userModel.getPassword().equals(password)) {
                return ResultData.fail(202, "登录密码错误");
            }
            userModel.setPassword(null);
            userModel.setCreateDate(null);
            userModel.setModifyDate(null);
            userModel.setGroups(null);
            userModel.setFriends(null);
            return ResultData.success(new SimpleDto().setValue(userModel));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(201, e.getMessage());
        }
    }

    @GetMapping("/getFriendList")
    public ResultData getFriendList(@RequestParam Long userId) {
        List<UserModel> friendList = userService.getFriendList(userId);
        ListDto<UserModel> userModelListDto = new ListDto<UserModel>(friendList);
        return ResultData.success(userModelListDto);
    }

}
