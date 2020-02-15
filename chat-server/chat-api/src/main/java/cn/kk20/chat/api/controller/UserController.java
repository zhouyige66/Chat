package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.base.ResultData;
import cn.kk20.chat.api.base.dto.SimpleDto;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultData addUser(@RequestBody UserModel userModel) {
        UserModel insertResult = userService.save(userModel);
        if (insertResult == null) {
            return ResultData.fail(101, "该用户名已存在，请使用其他用户名注册");
        } else {
            return ResultData.success(new SimpleDto().setValue("注册成功"));
        }
    }

    @GetMapping("/login/{name}/{password}")
    public ResultData login(@PathVariable("name") String name, @PathVariable("password") String password) {
        UserModel userModel = userService.find(name, password);
        if (userModel == null) {
            return ResultData.fail(201, "登录用户不存在");
        }
        if (!userModel.getPassword().equals(password)) {
            return ResultData.fail(202, "登录密码错误");
        }

        return ResultData.success(new SimpleDto().setValue("登录成功"));
    }

    @PostMapping("/login")
    public ResultData login(@RequestBody UserModel user) {
        UserModel userModel = userService.find(user.getName(), user.getPassword());
        if (userModel == null) {
            return ResultData.fail(201, "登录用户不存在");
        }
        if (!userModel.getPassword().equals(user.getPassword())) {
            return ResultData.fail(202, "登录密码错误");
        }

        return ResultData.success(new SimpleDto().setValue("登录成功"));
    }

}
