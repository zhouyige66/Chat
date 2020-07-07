package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.service.UserService;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.dao.model.UserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

        return ResultData.success(searchResult);
    }

}
