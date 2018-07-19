package com.aalife.service.impl;

import com.aalife.bo.CostGroupBo;
import com.aalife.bo.CostGroupOverviewBo;
import com.aalife.bo.CostGroupUserBo;
import com.aalife.bo.ExtendCostCleanBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.*;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupApprovalRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostGroupService;
import com.aalife.service.CostUserRemarkService;
import com.aalife.service.WebContext;
import com.aalife.utils.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.aalife.framework.annotation.RolePermission;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author brother lu
 * @date 2018-06-05
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class CostGroupServiceImpl implements CostGroupService {
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostUserRemarkService costUserRemarkService;
    @Autowired
    private CostDetailRepository costDetailRepository;

    @Autowired
    private WebContext webContext;

    @Override
    public Integer createNewCostGroup(String groupName) {
        if (StringUtils.isEmpty(groupName)){
            throw new BizException("账单名称不能为空");
        }
        User currentUser = webContext.getCurrentUser();
        Integer userId = currentUser.getUserId();
        Date today = new Date();
        // 创建分组
        CostGroup costGroup = new CostGroup();
        costGroup.setGroupName(groupName);
        costGroup.setGroupCode(UUIDUtil.get16BitUUID());
        costGroup.setEntryId(userId);
        costGroup.setEntryDate(new Date());
        costGroupRepository.save(costGroup);
        // 创建管理员记录
        CostGroupUser costGroupUser = new CostGroupUser();
        costGroupUser.setCostGroup(costGroup);
        costGroupUser.setUser(currentUser);
        costGroupUser.setAdmin('Y');
        costGroupUser.setEntryId(userId);
        costGroupUser.setEntryDate(today );
        costGroupUserRepository.save(costGroupUser);
        return costGroup.getGroupId();
    }

    @Override
    public void updateCostGroup(CostGroupBo costGroupBo) {
        String groupName = costGroupBo.getGroupName();
        Integer groupId = costGroupBo.getGroupNo();
        groupName = groupName == null ? null : groupName.trim();
        if (StringUtils.isEmpty(groupName)){
            throw new BizException("账单名不能为空");
        }
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        if (costGroup == null){
            throw new BizException("未查询到账单");
        }
        costGroup.setGroupName(groupName);
        costGroupRepository.save(costGroup);
    }

    @Override
    public CostGroupBo findCostGroupByCode(String code) {
        CostGroup costGroup = costGroupRepository.findGroupByGroupCode(code);
        if (costGroup == null){
            throw new BizException("未查询到对应得账单");
        }
        // 如果该用户已经在该账单中则不返回查询结果，用于跳转
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(webContext.getCurrentUser().getUserId(), costGroup.getGroupId());
        if (costGroupUser != null){
            return null;
        }
        CostGroupBo costGroupBo = new CostGroupBo();
        costGroupBo.setGroupNo(costGroup.getGroupId());
        costGroupBo.setGroupName(costGroup.getGroupName());
        costGroupBo.setGroupCode(costGroup.getGroupCode());
        return costGroupBo;
    }

    @Override
    public void deleteCostGroup(Integer groupId) {
        User user = webContext.getCurrentUser();
        Integer userId = user.getUserId();
        // 检查账单消费记录是否都结算
        BigDecimal groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId);
        if (groupTotalCost != null){
            throw new BizException("删除账单前请先结算所有消费");
        }
        // 删除账单
        costGroupRepository.deleteCostGroup(groupId, userId);
        // 删除所有的成员
        costGroupUserRepository.deleteCostGroupUserByGroupId(groupId, userId);
    }

    @Override
    public void leaveCostGroup(Integer groupId) {
        User user = webContext.getCurrentUser();
        Integer userId = user.getUserId();
        // 校验是否有管理员，除了当前用户
        List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupByGroup(groupId);
        boolean hasAnotherAdmin = false;
        for (CostGroupUser costGroupUser : costGroupUsers){
            // 存在除去当前用户的管理员
            if (costGroupUser.getAdmin().equals('Y') && !costGroupUser.getUser().getUserId().equals(userId)){
                hasAnotherAdmin = true;
            }
        }
        if (!hasAnotherAdmin){
            throw new BizException("退出该账单前需指定新的管理员或直接删除该账单");
        }
        // 校验该用户的账单是否已经结算
        BigDecimal groupCost = costDetailRepository.findTotalCostByGroup(groupId);
        if (groupCost != null){
            throw new BizException("所在账单还未结算，请先结算或联系账单管理员");
        }
        costGroupUserRepository.deleteCostGroupUser(userId, groupId, userId);
    }

    @Override
    public void assignGroupAdmin(Integer groupId, Integer userId) {
        costGroupUserRepository.assignCostGroupAdmin(groupId, userId);
    }

    @Override
    public void deleteCostGroupUser(Integer groupId, Integer userId) {
        User user = webContext.getCurrentUser();
        Integer deleteId = user.getUserId();
        if (userId.equals(deleteId)){
            throw new BizException("不能删除自己，可以选择退出操作或联系另一管理员");
        }
        // 删除用户前需结算该用户的消费记录
        BigDecimal userCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, userId);
        if (userCost != null && userCost.intValue() != 0){
            throw new BizException("该用户有未结算的记录");
        }
        costGroupUserRepository.deleteCostGroupUser(userId, groupId, deleteId);
    }

    @Override
    public CostGroupOverviewBo listCostGroupOverview(Integer groupId) {
        CostGroupOverviewBo costGroupOverviewBo = new CostGroupOverviewBo();
        // 设置组信息
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        if (costGroup == null){
            throw new BizException("未查询到账单");
        }
        CostGroupBo costGroupBo = new CostGroupBo();
        costGroupBo.setGroupNo(costGroup.getGroupId());
        costGroupBo.setGroupName(costGroup.getGroupName());
        costGroupBo.setGroupCode(costGroup.getGroupCode());
        costGroupOverviewBo.setCostGroup(costGroupBo);
        //设置 role
        User currentUser = webContext.getCurrentUser();
        Integer userId = currentUser.getUserId();
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(userId, groupId);
        String role = costGroupUser.getAdmin().equals('Y') ? "admin" : "user";
        costGroupOverviewBo.setMyRole(role);
        //设置用户信息
        List<CostGroupUserBo> costGroupUserBos = new ArrayList<>();
        List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupByGroup(groupId);
        BigDecimal groupTotalCost = new BigDecimal(0);
        for (CostGroupUser costGroupUserTemp : costGroupUsers){
            CostGroupUserBo costGroupUserBo = new CostGroupUserBo();
            Integer targetUserId = costGroupUserTemp.getUser().getUserId();
            // 设置备注名
            String nickName = costGroupUserTemp.getUser().getNickName();
            costGroupUserBo.setUserId(targetUserId);
            String remarkName = costUserRemarkService.getRemarkName(userId, targetUserId, nickName);
            costGroupUserBo.setRemarkName(remarkName);
            costGroupUserBo.setNickName(nickName);
            costGroupUserBo.setAvatarUrl(costGroupUserTemp.getUser().getAvatarUrl());
            // 设置角色
            CostGroupUser targetUser = costGroupUserRepository.findCostGroupByUserAndGroup(targetUserId, groupId);
            costGroupUserBo.setAdmin(targetUser.getAdmin());
            // 设置消费状况
            BigDecimal totalCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, targetUserId);
            totalCost = totalCost == null ? new BigDecimal(0) : totalCost;
            groupTotalCost = groupTotalCost.add(totalCost);
            costGroupUserBo.setTotalCost(totalCost);
            costGroupUserBos.add(costGroupUserBo);
        }
        costGroupOverviewBo.setGroupTotalCost(groupTotalCost);
        // 设置消费平均消费和差价
        BigDecimal userCount = new BigDecimal(costGroupUserBos.size());
        // 指定精确的位数，否者报错
        BigDecimal averageCost = groupTotalCost.divide(userCount, 2);
        for (CostGroupUserBo costGroupBoTemp : costGroupUserBos){
            BigDecimal costMoney = costGroupBoTemp.getTotalCost();
            costGroupBoTemp.setAverageCost(averageCost);
            costGroupBoTemp.setLeftCost(averageCost.subtract(costMoney));
        }
        costGroupOverviewBo.setCostUsers(costGroupUserBos);
        // 设置待接受的数量
        return costGroupOverviewBo;
    }

    @Override
    public List<CostGroupOverviewBo> listCostGroupOverview() {
        User user = webContext.getCurrentUser();
        Integer userId = user.getUserId();
        List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupUserByUser(userId);
        List<CostGroupOverviewBo> costGroupOverviewBos = new ArrayList<>();
        if (costGroupUsers == null || costGroupUsers.size() == 0){
            return costGroupOverviewBos;
        }
        for (CostGroupUser costGroupUser : costGroupUsers){
            Integer groupId = costGroupUser.getCostGroup().getGroupId();
            CostGroupOverviewBo costGroupOverviewBo = listCostGroupOverview(groupId);
            costGroupOverviewBos.add(costGroupOverviewBo);
        }
        return costGroupOverviewBos;
    }

    @Override
    public List<CostGroupBo> listMyGroups() {
        User currentUser = webContext.getCurrentUser();
        List<CostGroupUser> costGroups = costGroupUserRepository.findCostGroupUserByUser(currentUser.getUserId());
        List<CostGroupBo> costGroupBos = new ArrayList<>();
        if (costGroups == null || costGroups.size() == 0){
            return costGroupBos;
        }
        for (CostGroupUser costGroupUser : costGroups){
            CostGroupBo costGroupBo = new CostGroupBo();
            CostGroup costGroupTemp = costGroupUser.getCostGroup();
            costGroupBo.setGroupNo(costGroupTemp.getGroupId());
            costGroupBo.setGroupCode(costGroupTemp.getGroupCode());
            costGroupBo.setGroupName(costGroupTemp.getGroupName());
            costGroupBos.add(costGroupBo);
        }
        return costGroupBos;
    }

    @Override
    public CostGroupUserBo findCostGroupUserById(Integer groupId, Integer userId) {
        User currentUser = webContext.getCurrentUser();
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(userId, groupId);
        if (costGroupUser == null){
            throw new BizException("用户或账单不存在");
        }
        User targetUser = costGroupUser.getUser();
        CostGroupUserBo costGroupUserBo = new CostGroupUserBo();
        String nickName = targetUser.getNickName();
        costGroupUserBo.setNickName(nickName);
        costGroupUserBo.setAvatarUrl(targetUser.getAvatarUrl());
        String remarkName = costUserRemarkService.getRemarkName(currentUser.getUserId(), userId, nickName);
        costGroupUserBo.setRemarkName(remarkName);
        costGroupUserBo.setAdmin(costGroupUser.getAdmin());
        return costGroupUserBo;
    }

    @Override
    public CostGroupBo findCostGroupById(Integer groupId) {
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        if (costGroup == null){
            throw new BizException("未查询到对应的账单");
        }
        CostGroupBo costGroupBo = new CostGroupBo();
        costGroupBo.setGroupNo(costGroup.getGroupId());
        costGroupBo.setGroupCode(costGroup.getGroupCode());
        costGroupBo.setGroupName(costGroup.getGroupName());
        return costGroupBo;
    }

    @Override
    public void joinCostGroup(Integer groupId) {
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        if (costGroup == null){
            throw new BizException("账单不存在");
        }
        User currentUser = webContext.getCurrentUser();
        Integer currentUserId = currentUser.getUserId();
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(currentUserId, groupId);
        if (costGroupUser != null){
            throw new BizException("你已经在该账单中了");
        }
        costGroupUser = new CostGroupUser();
        costGroupUser.setEntryId(SystemConstant.SYSTEM_ID);
        costGroupUser.setEntryDate(new Date());
        costGroupUser.setCostGroup(costGroup);
        costGroupUser.setUser(currentUser);
        costGroupUser.setAdmin('N');
        costGroupUserRepository.save(costGroupUser);
    }
}
