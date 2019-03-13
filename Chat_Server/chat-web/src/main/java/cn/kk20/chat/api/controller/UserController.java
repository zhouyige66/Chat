package cn.kk20.chat.api.controller;

import cn.kk20.chat.model.RestModel;
import cn.kk20.chat.model.UserModel;
import cn.kk20.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019-01-29 16:57
 * @Version: v1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String sayHello() {
        return "hello";
    }

    @PostMapping(value = "/register")
    public RestModel addUser(@RequestBody UserModel userModel) {
        UserModel insertResult = userService.save(userModel);
        if (insertResult == null) {
            return RestModel.fail(101,"该用户名已存在，请使用其他用户名注册");
        }else {
            return RestModel.success(userModel);
        }
    }

    @GetMapping("/login/{name}/{password}")
    public RestModel login(@PathVariable("name") String name, @PathVariable("password") String password) {
        UserModel userModel = userService.find(name, password);
        if (userModel == null) {
            return RestModel.fail(201, "登录用户不存在");
        }
        if (!userModel.getPassword().equals(password)) {
            return RestModel.fail(202, "登录密码错误");
        }

        return RestModel.success(userModel);
    }

    @PostMapping("/login")
    public RestModel login(@RequestBody UserModel user) {
        UserModel userModel = userService.find(user.getName(), user.getPassword());
        if (userModel == null) {
            return RestModel.fail(201, "登录用户不存在");
        }
        if (!userModel.getPassword().equals(user.getPassword())) {
            return RestModel.fail(202, "登录密码错误");
        }

        return RestModel.success(userModel);
    }

}
