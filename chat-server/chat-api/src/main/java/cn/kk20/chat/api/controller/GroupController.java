package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.entity.request.ApplyBean;
import cn.kk20.chat.api.entity.request.CreateGroupBean;
import cn.kk20.chat.api.entity.request.VerifyBean;
import cn.kk20.chat.api.entity.vo.GroupVo;
import cn.kk20.chat.api.entity.vo.UserVo;
import cn.kk20.chat.api.enums.ApplyLogTypeEnum;
import cn.kk20.chat.api.service.ApplyLogService;
import cn.kk20.chat.api.service.GroupService;
import cn.kk20.chat.base.http.ResultData;
import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.GroupModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/5 14:25
 * @Version: v1.0
 */
@RestController
@RequestMapping("group")
@Api(tags = "群组Controller")
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    ApplyLogService applyLogService;

    @PostMapping("create")
    @ApiOperation(value = "创建群", notes = "功能：新建群组")
    @ApiImplicitParam(name = "createGroupBean", value = "群基本信息", dataType = "CreateGroupBean")
    public ResultData create(@RequestBody CreateGroupBean createGroupBean) throws Exception {
        GroupModel model = new GroupModel();
        model.setCreatorId(createGroupBean.getCreator());
        model.setName(createGroupBean.getName());
        model.setDescription(createGroupBean.getDescription());
        groupService.create(model);
        return ResultData.successWithMsg("创建成功");
    }

    @PostMapping("apply")
    @ApiOperation(value = "申请加入群", notes = "功能：提交加入群申请")
    @ApiImplicitParam(name = "applyBean", value = "申请参数", dataType = "ApplyBean")
    public ResultData addFriend(@RequestBody ApplyBean applyBean) throws Exception {
        if (applyBean == null || applyBean.getApplyUserId() == null || applyBean.getTargetUserId() == null) {
            return ResultData.requestError();
        }

        ApplyLogModel applyLogModel = new ApplyLogModel();
        applyLogModel.setType(ApplyLogTypeEnum.ADD_GROUP.getType());
        applyLogModel.setApplyUserId(applyBean.getApplyUserId());
        applyLogModel.setTargetUserId(applyBean.getTargetUserId());
        applyLogModel.setApplyRemark(applyBean.getRemark());
        applyLogService.addApply(applyLogModel);
        return ResultData.successWithMsg("申请已提交");
    }

    @PostMapping("verify")
    @ApiOperation(value = "审批-加入群申请", notes = "功能：审批加入群组申请")
    @ApiImplicitParam(name = "verifyBean", value = "审批参数", dataType = "VerifyBean")
    public ResultData verify(@RequestBody VerifyBean verifyBean) throws Exception {
        if (verifyBean == null || verifyBean.getApplyId() == null || verifyBean.getVerifyUserId() == null
                || verifyBean.getAgree() == null) {
            return ResultData.requestError();
        }

        ApplyLogModel applyLogModel = new ApplyLogModel();
        applyLogModel.setType(ApplyLogTypeEnum.ADD_GROUP.getType());
        applyLogModel.setId(verifyBean.getApplyId());
        applyLogModel.setVerifyUserId(verifyBean.getVerifyUserId());
        applyLogModel.setIsAgree(verifyBean.getAgree());
        applyLogModel.setVerifyRemark(verifyBean.getRemark());
        applyLogService.verifyApply(applyLogModel);
        return ResultData.successWithMsg("操作成功");
    }

    @GetMapping("list")
    @ApiOperation(value = "查询用户群列表", notes = "功能：根据用户ID查询用户群组列表")
    @ApiImplicitParam(name = "userId", value = "用户ID")
    public ResultData getGroupList(@RequestParam Long userId) throws Exception {
        List<GroupVo> groupListWithMember = groupService.getGroupListWithMember(userId);
        return ResultData.success(groupListWithMember);
    }

    @GetMapping("members")
    @ApiOperation(value = "查询群成员列表", notes = "功能：根据群ID查询成员列表")
    @ApiImplicitParam(name = "groupId", value = "群组ID")
    public ResultData getGroupMemberList(@RequestParam Long groupId) throws Exception {
        List<UserVo> groupMemberList = groupService.getGroupMemberList(groupId);
        return ResultData.success(groupMemberList);
    }
}
