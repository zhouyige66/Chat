package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.model.request.LoginBean;
import cn.kk20.chat.api.model.request.RegisterBean;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.http.dto.ListDto;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:57
 * @Version: v1.0
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户Controller")
@CrossOrigin // 跨越支持
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("test")
    @ApiOperation(value = "测试接口", notes = "功能：测试UserController是否可用")
    public String sayHello() {
        return "hello";
    }

    @PostMapping(value = "register")
    @ApiOperation(value = "用户注册接口", notes = "功能：注册新用户")
    @ApiImplicitParam(name = "registerBean", value = "用户信息", dataType = "RegisterBean")
    public ResultData register(@RequestBody RegisterBean registerBean) throws Exception {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(registerBean, userModel);
        userService.register(userModel);
        return ResultData.success("注册成功");
    }

    @PostMapping("login")
    @ApiOperation(value = "登录接口", notes = "用户android、iOS登录使用")
    @ApiImplicitParam(name = "loginBean", value = "登录信息", dataType = "LoginBean")
    public ResultData login(@RequestBody LoginBean loginBean) throws Exception {
        return login(loginBean.getUserName(), loginBean.getPassword());
    }

    @GetMapping("login/{userName}/{password}")
    @ApiOperation(value = "登录接口", notes = "用户web登录使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名或电话或邮箱"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public ResultData login(@PathVariable("userName") String userName, @PathVariable("password") String password)
            throws Exception {
        UserModel userModel = userService.login(userName, password);
        if (!userModel.getPassword().equals(password)) {
            return ResultData.requestError("登录密码错误");
        }
        userModel.setPassword(null);
        userModel.setGroupList(null);
        userModel.setFriendList(null);
        return ResultData.success(userModel);
    }

    @GetMapping("search")
    @ApiOperation(value = "模糊查询用户接口", notes = "功能：根据用户（ID、用户名、电话）模糊搜索用户")
    @ApiImplicitParam(name = "key", value = "ID或用户名或电话")
    public ResultData fuzzySearch(@RequestParam String key) {
        if (StringUtils.isEmpty(key)) {
            return ResultData.requestError("参数错误");
        }

        List<UserModel> searchResult = userService.search(key);
        if (CollectionUtils.isEmpty(searchResult)) {
            return ResultData.success("暂无相关用户");
        }

        return ResultData.success(new ListDto<UserModel>(searchResult));
    }

}
