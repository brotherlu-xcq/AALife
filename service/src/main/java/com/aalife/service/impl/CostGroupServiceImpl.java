package com.aalife.service.impl;

import com.aalife.bo.CostGroupBo;
import com.aalife.bo.CostGroupOverviewBo;
import com.aalife.bo.CostGroupUserBo;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostGroupService;
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
@Transactional
@RequiresAuthentication
public class CostGroupServiceImpl implements CostGroupService {
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostUserRemarkRepository costUserRemarkRepository;
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
        if (StringUtils.isEmpty(groupName)){
            throw new BizException("账单名不能为空");
        }
        costGroupRepository.updateCostGroup(costGroupBo.getGroupNo(), groupName);
    }

    @Override
    public CostGroupBo findCostGroupByCode(String code) {
        CostGroup costGroup = costGroupRepository.findGroupByGroupCode(code);
        if (costGroup == null){
            throw new BizException("未查询到对应得账单");
        }

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
        // todo 需要加入删除前的判断
        costGroupRepository.deleteCostGroup(groupId, userId);
        // 删除所有的成员
        costGroupUserRepository.deleteCostGroupUserByGroupId(groupId, userId);
    }

    @Override
    public void leaveCostGroup(Integer groupId) {
        User user = webContext.getCurrentUser();
        Integer userId = user.getUserId();
        // todo 推出前需要判断，所有消费记录必须结算，必须要有管理员
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
        // todo 推出前需要判断，所有消费记录必须结算，必须要有管理员
        costGroupUserRepository.deleteCostGroupUser(userId, groupId, deleteId);
    }

    @Override
    public CostGroupOverviewBo listCostGroupOverview(Integer groupId) {
        CostGroupOverviewBo costGroupOverviewBo = new CostGroupOverviewBo();
        // 设置组信息
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
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
            costGroupUserBo.setUserId(targetUserId);
            CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(userId, targetUserId);
            // 设置昵称
            if (costUserRemark != null){
                costGroupUserBo.setRemarkName(costUserRemark.getRemarkName());
            } else {
                costGroupUserBo.setRemarkName(costGroupUserTemp.getUser().getNickName());
            }
            costGroupUserBo.setNickName(costGroupUserTemp.getUser().getNickName());
            costGroupUserBo.setAvatarUrl(costGroupUserTemp.getUser().getAvatarUrl());
            // 设置角色
            CostGroupUser targetUser = costGroupUserRepository.findCostGroupByUserAndGroup(targetUserId, groupId);
            costGroupUserBo.setAdmin(targetUser.getAdmin());
            // 设置消费状况
            BigDecimal totalCost = costDetailRepository.findUnCleanTotalCostByUserAndGroup(groupId, targetUserId);
            totalCost = totalCost == null ? new BigDecimal(0) : totalCost;
            groupTotalCost = groupTotalCost.add(totalCost);
            costGroupUserBo.setTotalCost(totalCost);
            costGroupUserBos.add(costGroupUserBo);
        }
        costGroupOverviewBo.setGroupTotalCost(groupTotalCost);
        // 设置消费平均消费和差价
        BigDecimal userCount = new BigDecimal(costGroupUserBos.size());
        BigDecimal averageCost = groupTotalCost.divide(userCount);
        for (CostGroupUserBo costGroupBoTemp : costGroupUserBos){
            BigDecimal costMoney = costGroupBoTemp.getTotalCost();
            costGroupBoTemp.setAverageCost(averageCost);
            costGroupBoTemp.setLeftCost(averageCost.subtract(costMoney));
        }
        costGroupOverviewBo.setCostUsers(costGroupUserBos);
        return costGroupOverviewBo;
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
}
