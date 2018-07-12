package com.aalife.service.impl;

import com.aalife.bo.CostCleanBo;
import com.aalife.bo.CostCleanCategoryBo;
import com.aalife.bo.CostCleanSummaryBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.CostGroupUserBo;
import com.aalife.bo.ExtendCostCleanBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.CostCategory;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostCleanUser;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostCategoryRepository;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostCleanUserRepository;
import com.aalife.dao.repository.CostDetailRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostCleanService;
import com.aalife.service.CostUserRemarkService;
import com.aalife.service.WebContext;
import com.aalife.utils.FormatUtil;
import com.aalife.utils.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-10
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class CostCleanServiceImpl implements CostCleanService {
    private static Logger logger = Logger.getLogger(CostCleanService.class);
    @Autowired
    private CostCleanRepository costCleanRepository;
    @Autowired
    private CostDetailRepository costDetailRepository;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostCleanUserRepository costCleanUserRepository;
    @Autowired
    private CostUserRemarkService costUserRemarkService;
    @Autowired
    private CostCategoryRepository categoryRepository;
    @Autowired
    private WebContext webContext;

    @Override
    public List<ExtendCostCleanBo> listCostCleans(Integer groupId) {
        List<CostClean> costCleans = costCleanRepository.findCostCleansByGroupId(groupId);
        List<ExtendCostCleanBo> extendCostCleanBos = new ArrayList<>();
        if (costCleans == null || costCleans.size() == 0){
            return extendCostCleanBos;
        }
        Integer currentUserId = webContext.getCurrentUser().getUserId();
        for (CostClean costClean : costCleans){
            Integer cleanId = costClean.getCleanId();
            ExtendCostCleanBo extendCostCleanBo = new ExtendCostCleanBo();
            extendCostCleanBo.setComment(costClean.getComment());
            extendCostCleanBo.setCleanDate(FormatUtil.formatDate2String(costClean.getEntryDate(), SystemConstant.DATEPATTERN));
            extendCostCleanBo.setCleanId(cleanId);
            // 设置账单信息
            CostGroup costGroup = costClean.getCostGroup();
            if (costGroup.getDeleteId() != null){
                throw new BizException("账单不存在");
            }
            CostGroupBo costGroupBo = new CostGroupBo();
            costGroupBo.setGroupNo(costGroup.getGroupId());
            costGroupBo.setGroupName(costGroup.getGroupName());
            costGroupBo.setGroupCode(costGroup.getGroupCode());
            extendCostCleanBo.setCostGroup(costGroupBo);
            // 设置用户信息
            User user = costClean.getUser();
            String nickName = user.getNickName();
            Integer targetUserId = user.getUserId();
            ExtendUserBo userBo = new ExtendUserBo();
            userBo.setAvatarUrl(user.getAvatarUrl());
            userBo.setNickName(nickName);
            userBo.setUserId(targetUserId);
            // 设置备注名
            userBo.setRemarkName(costUserRemarkService.getRemarkName(currentUserId, targetUserId, nickName));
            extendCostCleanBo.setUser(userBo);
            // 设置总消费
            BigDecimal groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId, cleanId);
            groupTotalCost = groupTotalCost == null ? new BigDecimal(0) : groupTotalCost;
            extendCostCleanBo.setTotalCost(groupTotalCost);
            // 设置平均消费
            List<CostCleanUser> costCleanUsers = costCleanUserRepository.findCostCleanUsersByCleanId(cleanId);
            BigDecimal count = new BigDecimal(costCleanUsers.size());
            extendCostCleanBo.setAverageCost(groupTotalCost.divide(count, 2));
            extendCostCleanBos.add(extendCostCleanBo);
        }
        return extendCostCleanBos;
    }

    @Override
    public CostCleanSummaryBo findCostCleanSummaryById(Integer groupId, Integer cleanId) {
        CostCleanSummaryBo costCleanSummaryBo = new CostCleanSummaryBo();
        // 设置账单信息
        CostGroupBo costGroupBo = new CostGroupBo();
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        costGroupBo.setGroupNo(groupId);
        costGroupBo.setGroupName(costGroup.getGroupName());
        costGroupBo.setGroupCode(costGroup.getGroupCode());
        costCleanSummaryBo.setCostGroup(costGroupBo);
        // 设置结算信息
        if (cleanId != null){
            CostClean costClean = costCleanRepository.findOne(cleanId);
            if (costClean == null){
                throw new BizException("未查询到结算记录");
            }
            CostCleanBo costCleanBo = new CostCleanBo();
            costCleanBo.setCleanId(costClean.getCleanId());
            costCleanBo.setCleanDate(FormatUtil.formatDate2String(costClean.getEntryDate(), SystemConstant.DATEPATTERN));
            costCleanBo.setComment(costClean.getComment());
            // 设置结算用户信息
            User cleanUser = costClean.getUser();
            Integer targetUserId = cleanUser.getUserId();
            String nickName = cleanUser.getNickName();
            ExtendUserBo userBo = new ExtendUserBo();
            userBo.setUserId(targetUserId);
            userBo.setAvatarUrl(cleanUser.getAvatarUrl());
            userBo.setNickName(nickName);
            userBo.setRemarkName(costUserRemarkService.getRemarkName(webContext.getCurrentUser().getUserId(), targetUserId, nickName));
            costCleanBo.setUser(userBo);
            costCleanSummaryBo.setCostClean(costCleanBo);
        }
        // 设置按分类结算信息
        List<CostCleanCategoryBo> categorySummary = new ArrayList<>();

        List<Object> data;
        if (cleanId == null){
            data = costDetailRepository.findTotalCostForCostCateByGroup(groupId);
        } else{
            data = costDetailRepository.findTotalCostForCostCateByGroup(groupId, cleanId);
        }
        if (data != null && data.size() > 0){
            data.forEach(resultSet -> {
                Object[] objects = (Object[]) resultSet;
                CostCleanCategoryBo costCleanCategoryBo = new CostCleanCategoryBo();
                int cateId = (int) objects[0];
                costCleanCategoryBo.setCateId(cateId);
                costCleanCategoryBo.setTotalCost((BigDecimal) objects[1]);
                // 添加消费分类信息
                CostCategory costCategory = categoryRepository.findOne(cateId);
                costCleanCategoryBo.setCateIcon(costCategory.getCateIcon());
                costCleanCategoryBo.setCateName(costCategory.getCateName());
                categorySummary.add(costCleanCategoryBo);
            });
        }
        costCleanSummaryBo.setCategorySummary(categorySummary);
        // 按用户设置消费信息
        List<CostGroupUserBo> userSummary = new ArrayList<>();
        BigDecimal groupTotalCost;
        if (cleanId == null){
            groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId);
        } else {
            groupTotalCost = costDetailRepository.findTotalCostByGroup(groupId, cleanId);
        }
        groupTotalCost = groupTotalCost == null ? new BigDecimal(0) : groupTotalCost;
        // 获取未结算的用户信息
        if (cleanId == null){
            List<CostGroupUser> costGroupUsers = costGroupUserRepository.findCostGroupByGroup(groupId);
            BigDecimal userCount = new BigDecimal(costGroupUsers.size());
            BigDecimal groupAverageCost = groupTotalCost.divide(userCount, 2);
            costGroupUsers.forEach(costGroupUser -> {
                User user = costGroupUser.getUser();
                userSummary.add(createCostGroupUserBo(user, groupId, cleanId, groupAverageCost));
                costCleanSummaryBo.setUserSummary(userSummary);
            });
        } else {
            List<CostCleanUser> costCleanUsers = costCleanUserRepository.findCostCleanUsersByCleanId(cleanId);
            BigDecimal userCount = new BigDecimal(costCleanUsers.size());
            BigDecimal groupAverageCost = groupTotalCost.divide(userCount, 2);
            costCleanUsers.forEach(costCleanUser -> {
                User user = costCleanUser.getUser();
                userSummary.add(createCostGroupUserBo(user, groupId, cleanId, groupAverageCost));
                costCleanSummaryBo.setUserSummary(userSummary);
            });
        }
        return costCleanSummaryBo;
    }

    private CostGroupUserBo createCostGroupUserBo(User user, Integer groupId, Integer cleanId, BigDecimal groupAverageCost){
        Integer currentUserId = webContext.getCurrentUser().getUserId();
        Integer targetUserId = user.getUserId();
        String nickName = user.getNickName();
        CostGroupUserBo costGroupUserBo = new CostGroupUserBo();
        costGroupUserBo.setUserId(targetUserId);
        costGroupUserBo.setAvatarUrl(user.getAvatarUrl());
        //todo costGroupUserBo.setAdmin(costCleanUser.getAdmin());
        costGroupUserBo.setNickName(nickName);
        costGroupUserBo.setAverageCost(groupAverageCost);
        costGroupUserBo.setRemarkName(costUserRemarkService.getRemarkName(currentUserId, targetUserId, nickName));
        BigDecimal userTotalCost;
        if (cleanId == null){
            userTotalCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, targetUserId);
        } else {
            userTotalCost = costDetailRepository.findTotalCostByUserAndGroup(groupId, targetUserId, cleanId);
        }
        userTotalCost = userTotalCost == null ? new BigDecimal(0) : userTotalCost;
        BigDecimal leftCost = userTotalCost.subtract(groupAverageCost);
        costGroupUserBo.setLeftCost(leftCost);
        costGroupUserBo.setTotalCost(userTotalCost);
        return costGroupUserBo;
    }
}
