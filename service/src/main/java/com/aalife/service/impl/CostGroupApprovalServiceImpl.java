package com.aalife.service.impl;

import com.aalife.bo.ApprovalBo;
import com.aalife.bo.ApprovalInfoBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupApproval;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostGroupApprovalRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostGroupApprovalService;
import com.aalife.service.CostGroupService;
import com.aalife.service.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        return null;
    }
}
