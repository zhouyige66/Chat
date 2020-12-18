package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.entity.vo.GroupVo;
import cn.kk20.chat.api.entity.vo.UserVo;
import cn.kk20.chat.api.service.GroupService;
import cn.kk20.chat.api.service.UserService;
import cn.kk20.chat.base.http.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    GroupService groupService;

    @GetMapping("search")
    @ApiOperation(value = "模糊查询用户接口", notes = "功能：根据用户（ID、用户名、电话）模糊搜索用户")
    @ApiImplicitParam(name = "key", value = "ID或用户名或电话")
    public ResultData fuzzySearch(@RequestParam String key) {
        if (StringUtils.isEmpty(key)) {
            return ResultData.requestError("参数错误");
        }

        List<UserVo> searchResult = userService.search(key);
        List<GroupVo> searchResult2 = groupService.search(key);
        Map<String, Object> data = new HashMap<>();
        data.put("friendList",searchResult);
        data.put("groupList",searchResult2);
        return ResultData.success(data);
    }

}
