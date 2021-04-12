package cn.kk20.chat.api.service;

import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.plus.ApplyLogPlusModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
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

    /**
     * 获取需要审批的记录
     *
     * @param verifyUserId
     * @return
     * @throws Exception
     */
    ApplyLogPlusModel getApplyLogList2(Long verifyUserId) throws Exception;

}
