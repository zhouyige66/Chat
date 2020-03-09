package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.model.request.ApplyBean;
import cn.kk20.chat.api.model.request.VerifyBean;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.base.http.dto.SimpleDto;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.service.ApplyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "FriendController")
@CrossOrigin // 跨越支持
public class FriendController {
    @Autowired
    ApplyLogService applyLogService;

    @PostMapping("/add")
    @ApiOperation(value = "添加好友", notes = "功能：添加好友申请")
    @ApiImplicitParam(name = "applyBean", value = "申请参数", dataType = "ApplyBean")
    public ResultData addFriend(@RequestBody ApplyBean applyBean) throws Exception {
        if (applyBean == null || applyBean.getApplyUserId() == null || applyBean.getTargetUserId() == null) {
            return ResultData.requestError();
        }

        ApplyLogModel applyLogModel = new ApplyLogModel();
        applyLogModel.setType(ApplyLogModel.APPLY_TYPE_ADD_FRIEND);
        applyLogModel.setApplyUserId(applyBean.getApplyUserId());
        applyLogModel.setTargetUserId(applyBean.getTargetUserId());
        applyLogModel.setApplyRemark(applyBean.getRemark());
        applyLogService.addApply(applyLogModel);

        return ResultData.success("申请已提交");
    }

    @PostMapping("/verify")
    @ApiOperation(value = "审批-加好友申请", notes = "功能：审批好友申请")
    @ApiImplicitParam(name = "verifyBean", value = "审批参数", dataType = "VerifyBean")
    public ResultData verify(@RequestBody VerifyBean verifyBean) {
        if (verifyBean == null || verifyBean.getApplyId() == null || verifyBean.getAgree() == null) {
            return ResultData.requestError();
        }

        try {
            ApplyLogModel applyLogModel = new ApplyLogModel();
            applyLogModel.setType(ApplyLogModel.APPLY_TYPE_ADD_FRIEND);
            applyLogModel.setId(verifyBean.getApplyId());
            applyLogModel.setVerifyUserId(verifyBean.getVerifyUserId());
            applyLogModel.setIsAgree(verifyBean.getAgree());
            applyLogModel.setVerifyRemark(verifyBean.getRemark());
            applyLogService.verifyApply(applyLogModel);
            return ResultData.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.requestError(e.getMessage());
        }
    }

}
