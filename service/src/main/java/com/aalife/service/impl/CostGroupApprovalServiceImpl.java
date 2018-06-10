package com.aalife.service.impl;

import com.aalife.bo.ApprovalBo;
import com.aalife.bo.ApprovalInfoBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.constant.ApprovalStatus;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupApproval;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostGroupApprovalRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostGroupApprovalService;
import com.aalife.service.CostGroupService;
import com.aalife.service.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Service
@Transactional
public class CostGroupApprovalServiceImpl implements CostGroupApprovalService {
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostGroupApprovalRepository costGroupApprovalRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostUserRemarkRepository costUserRemarkRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createNewApproval(ApprovalBo approvalBo) {
        String comment = approvalBo.getComment();
        if (StringUtils.isEmpty(comment)){
            throw new BizException("申请备注不能为空");
        }
        CostGroup costGroup = costGroupRepository.findGroupByGroupCode(approvalBo.getGroupCode());
        if (costGroup == null){
            throw new BizException("未查询到账单");
        }
        User currentUser = webContext.getCurrentUser();
        Integer userId = currentUser.getUserId();
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(userId, costGroup.getGroupId());
        if (costGroupUser != null){
            throw new BizException("你已经在该账单中");
        }
        // 若该记录已存在则更新comment
        CostGroupApproval costGroupApproval = costGroupApprovalRepository.findApprovalByUserAndGroup(userId, costGroup.getGroupId());
        if (costGroupApproval == null){
            costGroupApproval = new CostGroupApproval();
            costGroupApproval.setUser(currentUser);
            costGroupApproval.setCostGroup(costGroup);
            costGroupApproval.setComment(comment);
            costGroupApproval.setStatus(0);
            costGroupApproval.setEntryId(userId);
            costGroupApproval.setEntryDate(new Date());
        } else{
            costGroupApproval.setComment(comment);
        }
        costGroupApprovalRepository.save(costGroupApproval);
    }

    @Override
    public List<ApprovalInfoBo> listApprovalsByGroup(Integer groupId) {
        List<CostGroupApproval> costGroupApprovals = costGroupApprovalRepository.findApprovalsByGroup(groupId);
        List<ApprovalInfoBo> approvalInfoBos = new ArrayList<>();
        if (costGroupApprovals == null || costGroupApprovals.size() == 0){
            return approvalInfoBos;
        }
        CostGroup costGroup = null;
        CostGroupBo costGroupBo = null;
        for (CostGroupApproval costGroupApproval : costGroupApprovals){
            ApprovalInfoBo approvalInfoBo = new ApprovalInfoBo();
            approvalInfoBo.setComment(costGroupApproval.getComment());
            approvalInfoBo.setApprovalId(costGroupApproval.getId());
            approvalInfoBo.setStatus(costGroupApproval.getStatus() == 0 ? ApprovalStatus.PEDDING.getStatusName() : ApprovalStatus.APPROVAL.getStatusName());
            // 设置分组信息
            if (costGroup == null){
                costGroup = costGroupApproval.getCostGroup();
                costGroupBo = new CostGroupBo();
                costGroupBo.setGroupNo(costGroup.getGroupId());
                costGroupBo.setGroupCode(costGroup.getGroupCode());
                costGroupBo.setGroupName(costGroup.getGroupName());
            }
            //设置用户信息
            ExtendUserBo extendUserBo = new ExtendUserBo();
            User user = costGroupApproval.getUser();
            extendUserBo.setUserId(user.getUserId());
            extendUserBo.setNickName(user.getNickName());
            extendUserBo.setAvatarUrl(user.getAvatarUrl());
            approvalInfoBo.setUser(extendUserBo);
            // 设置备注名
            CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(webContext.getCurrentUser().getUserId(), user.getUserId());
            extendUserBo.setRemarkName(costUserRemark == null ? user.getNickName() : costUserRemark.getRemarkName());
            approvalInfoBo.setCostGroup(costGroupBo);
            approvalInfoBos.add(approvalInfoBo);
        }
        return approvalInfoBos;
    }

    @Override
    public void approveUserRequest(Integer groupId, Integer userId) {
        User user = userRepository.findOne(userId);
        if (user == null){
            throw new BizException("未查询到用户");
        }
        User currentUser = webContext.getCurrentUser();
        // 通过审批并且填写审批人信息
        costGroupApprovalRepository.approveUserRequest(groupId, userId, currentUser);
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(userId, groupId);
        if (costGroupUser != null){
            return;
        }
        // 创建新的记录
        CostGroup costGroup = costGroupRepository.findOne(groupId);
        costGroupUser = new CostGroupUser();
        costGroupUser.setAdmin('N');
        costGroupUser.setUser(user);
        costGroupUser.setCostGroup(costGroup);
        costGroupUser.setEntryId(currentUser.getUserId());
        costGroupUser.setEntryDate(new Date());
        costGroupUserRepository.save(costGroupUser);
    }
}
