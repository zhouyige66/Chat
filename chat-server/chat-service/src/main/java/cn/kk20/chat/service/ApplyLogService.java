package cn.kk20.chat.service;

import cn.kk20.chat.base.exception.RequestParamException;
import cn.kk20.chat.dao.model.ApplyLogModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/3 15:31
 * @Version: v1.0
 */
public interface ApplyLogService {

    /**
     * 获取需要审批的记录
     *
     * @param verifyUserId
     * @return
     */
    List<ApplyLogModel> getApplyLogList(Long verifyUserId) throws Exception;

    /**
     * 添加申请
     *
     * @param model
     * @throws Exception
     */
    void addApply(ApplyLogModel model) throws Exception;

    /**
     * 审核申请
     *
     * @param model
     * @throws Exception
     */
    void verifyApply(ApplyLogModel model) throws Exception;

}
