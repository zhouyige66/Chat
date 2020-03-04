package cn.kk20.chat.api.controller;

import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.service.FriendDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:57
 * @Version: v1.0
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin // 跨越支持
public class FriendController {
    @Autowired
    FriendDealService friendDealService;

    @GetMapping("/test")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/add")
    public ResultData addFriend(@RequestParam Long fromId, @RequestParam Long toId) {
        if (fromId == null || toId == null) {
            return ResultData.fail(100, "参数错误");
        }

        try {
            friendDealService.applyAddFriend(fromId,toId);
            return ResultData.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(500, e.getMessage());
        }
    }

    @GetMapping("/agree")
    public ResultData agreeAddFriend(@RequestParam Long applyId, @RequestParam Long targetId) {
        if (applyId == null || targetId == null) {
            return ResultData.fail(100, "参数错误");
        }

        try {
            friendDealService.agreeAddFriend(applyId,targetId);
            return ResultData.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(500, e.getMessage());
        }
    }

}
