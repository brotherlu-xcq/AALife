package com.aalife.service.impl;

import com.aalife.bo.ApprovalBo;
import com.aalife.bo.ApprovalInfoBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.bo.WxNotificationDetailBo;
import com.aalife.constant.ApprovalStatus;
import com.aalife.constant.SystemConstant;
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
import com.aalife.service.CostUserRemarkService;
import com.aalife.service.NotificationService;
import com.aalife.service.WebContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class CostGroupApprovalServiceImpl implements CostGroupApprovalService {
    private static Logger logger = Logger.getLogger(CostGroupApprovalService.class);
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostGroupApprovalRepository costGroupApprovalRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostUserRemarkService costUserRemarkService;
    @Autowired
    private NotificationService notificationService;

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
            costGroupApproval.setStatus(0);
            costGroupApproval.setApprovalUser(null);
            costGroupApproval.setApprovalDate(null);
        }
        costGroupApprovalRepository.save(costGroupApproval);
        // 初始化发送信息的内容
        try{
            Integer groupId = costGroup.getGroupId();
            List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupByGroup(groupId);
            costGroupUsers.forEach(costGroupUser1 -> {
                if (costGroupUser1.getAdmin().equals(SystemConstant.Y)){
                    WxNotificationDetailBo data = new WxNotificationDetailBo();
                    Map<String, Object> groupName = new HashMap<>(2);
                    groupName.put("value", costGroup.getGroupName());
                    data.setKeyword1(groupName);
                    Map<String, Object> userName = new HashMap<>(2);
                    userName.put("value", currentUser.getNickName());
                    data.setKeyword2(userName);
                    Map<String, Object> commentTemp = new HashMap<>(2);
                    commentTemp.put("value", comment);
                    data.setKeyword3(commentTemp);
                    Integer targetUserId = costGroupUser1.getUser().getUserId();
                    notificationService.sendWxNotification(targetUserId, SystemConstant.APPROVAL_REQUEST, data, String.valueOf(groupId));
                }
            });
        } catch (Exception e){
            logger.error("异步发送信息失败", e);
        }
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
        Integer currentUserId = webContext.getCurrentUser().getUserId();
        for (CostGroupApproval costGroupApproval : costGroupApprovals){
            ApprovalInfoBo approvalInfoBo = new ApprovalInfoBo();
            approvalInfoBo.setComment(costGroupApproval.getComment());
            approvalInfoBo.setApprovalId(costGroupApproval.getId());
            approvalInfoBo.setStatus(costGroupApproval.getStatus() == 0 ? ApprovalStatus.PENDING.getStatusName() : ApprovalStatus.APPROVAL.getStatusName());
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
            String nickName = user.getNickName();
            Integer targetUserId = user.getUserId();
            extendUserBo.setUserId(targetUserId);
            extendUserBo.setNickName(nickName);
            extendUserBo.setAvatarUrl(user.getAvatarUrl());
            approvalInfoBo.setUser(extendUserBo);
            // 设置备注名
            String remarkName = costUserRemarkService.getRemarkName(currentUserId, targetUserId, nickName);
            extendUserBo.setRemarkName(remarkName);
            approvalInfoBo.setCostGroup(costGroupBo);
            approvalInfoBos.add(approvalInfoBo);
        }
        return approvalInfoBos;
    }

    @Override
    public void approveUserRequest(Integer groupId, Integer userId) {
        CostGroupApproval costGroupApproval = costGroupApprovalRepository.findApprovalByUserAndGroup(userId, groupId);
        if (costGroupApproval == null){
            throw new BizException("未查询到对应的申请记录");
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
        costGroupUser.setUser(costGroupApproval.getUser());
        costGroupUser.setCostGroup(costGroup);
        costGroupUser.setEntryId(currentUser.getUserId());
        costGroupUser.setEntryDate(new Date());
        costGroupUserRepository.save(costGroupUser);
        // 异步发送信息
        try{
            WxNotificationDetailBo data = new WxNotificationDetailBo();
            Map<String, Object> groupName = new HashMap<>(2);
            groupName.put("value", costGroup.getGroupName());
            data.setKeyword1(groupName);
            Map<String, Object> userName = new HashMap<>(2);
            userName.put("value", currentUser.getNickName());
            data.setKeyword2(userName);
            Map<String, Object> commentTemp = new HashMap<>(2);
            commentTemp.put("value", "欢迎加入哦");
            data.setKeyword3(commentTemp);
            notificationService.sendWxNotification(userId, SystemConstant.APPROVAL_PASS, data, String.valueOf(groupId));
        } catch (Exception e){
            logger.error("异步发送信息失败", e);
        }
    }
}
