package com.aalife.service;

import com.aalife.bo.ApprovalBo;
import com.aalife.bo.ApprovalInfoBo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-06
 */
public interface CostGroupApprovalService {
    /**
     * 创建新的申请
     * @param approvalBo
     */
    void createNewApproval(ApprovalBo approvalBo);

    /**
     * 根据账单查找申请
     * @param groupId
     * @return
     */
    List<ApprovalInfoBo> listApprovalsByGroup(Integer groupId);

    /**
     * 同意申请
     * @param groupId
     * @param userId
     */
    void approveUserRequest(Integer groupId, Integer userId);
}
