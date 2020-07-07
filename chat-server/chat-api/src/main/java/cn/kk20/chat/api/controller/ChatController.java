package cn.kk20.chat.api.controller;

import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.http.dto.ListDto;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.api.service.ApplyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/9 14:59
 * @Version: v1.0
 */
@RestController
@RequestMapping("chat")
@Api(tags = "聊天controller")
public class ChatController {
    @Autowired
    ApplyLogService applyLogService;

    @GetMapping("list")
    @ApiOperation(value = "查询聊天列表", notes = "功能：根据用户ID查询待用户聊天列表")
    @ApiImplicitParam(name = "userId", value = "审批者ID")
    public ResultData getVerifyList(@RequestParam Long userId) throws Exception {
        List<ApplyLogModel> applyLogList = applyLogService.getApplyLogList(userId);
        ListDto<ApplyLogModel> applyLogModelListDto = new ListDto<>(applyLogList);
        return ResultData.success(applyLogModelListDto);
    }

}
