package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.service.ApplyLogService;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.plus.ApplyLogPlusModel;
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
 * @Author: Roy
 * @Date: 2020/3/9 14:59
 * @Version: v1.0
 */
@RestController
@RequestMapping("apply")
@Api(tags = "申请controller")
public class ApplyLogController {
    @Autowired
    ApplyLogService applyLogService;

    @GetMapping("list")
    @ApiOperation(value = "查询申请记录", notes = "功能：根据用户ID查询待用户申请记录")
    @ApiImplicitParam(name = "userId", value = "审批者ID")
    public ResultData getApplyList(@RequestParam Long userId) throws Exception {
        List<ApplyLogModel> applyLogList = applyLogService.getApplyLogList(userId);
        return ResultData.success(applyLogList);
    }

    @GetMapping("item")
    @ApiOperation(value = "查询申请记录", notes = "功能：根据用户ID查询待用户申请记录")
    @ApiImplicitParam(name = "applyId", value = "审批者ID")
    public ResultData getApplyList2(@RequestParam Long applyId) throws Exception {
        ApplyLogPlusModel applyLogPlusModel = applyLogService.getApplyLogList2(applyId);
        return ResultData.success(applyLogPlusModel);
    }

}
