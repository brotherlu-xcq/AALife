package com.aalife.service.impl;

import com.aalife.bo.CostGroupBo;
import com.aalife.bo.ExtendCostCleanBo;
import com.aalife.bo.ExtendUserBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostUserRemark;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostCleanRepository;
import com.aalife.dao.repository.CostUserRemarkRepository;
import com.aalife.exception.BizException;
import com.aalife.service.CostCleanService;
import com.aalife.service.WebContext;
import com.aalife.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private CostCleanRepository costCleanRepository;
    @Autowired
    private CostUserRemarkRepository costUserRemarkRepository;
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
        Map<String, String> userRemarkNames = new HashMap<>(4);
        for (CostClean costClean : costCleans){
            ExtendCostCleanBo extendCostCleanBo = new ExtendCostCleanBo();
            extendCostCleanBo.setComment(costClean.getComment());
            extendCostCleanBo.setCleanDate(FormatUtil.formatDate2String(costClean.getEntryDate(), SystemConstant.DATEPATTERN));
            extendCostCleanBo.setCleanId(costClean.getCleanId());
            // 设置账单信息
            CostGroup costGroup = costClean.getCostGroup();
            if (costGroup.getDeleteId() != null){
                throw new BizException("账单不存在");
            }
            CostGroupBo costGroupBo = new CostGroupBo();
            costGroupBo.setGroupNo(costGroup.getGroupId());
            costGroupBo.setGroupName(costGroup.getGroupName());
            costGroupBo.setGroupCode(costGroup.getGroupCode());
            // 设置用户信息
            User user = costClean.getUser();
            Integer targetUserId = user.getUserId();
            ExtendUserBo userBo = new ExtendUserBo();
            userBo.setAvatarUrl(user.getAvatarUrl());
            userBo.setNickName(user.getNickName());
            userBo.setUserId(targetUserId);
            // 设置备注名
            String key = currentUserId + "-" +targetUserId;
            String remarkName = userRemarkNames.get(key);
            if (remarkName == null){
                CostUserRemark costUserRemark = costUserRemarkRepository.findRemarkBySourceAndTarget(currentUserId, targetUserId);
                remarkName = costUserRemark == null ? user.getNickName() : costUserRemark.getRemarkName();
                userRemarkNames.put(key, remarkName);
            }
            userBo.setRemarkName(remarkName);
            extendCostCleanBo.setUser(userBo);
            extendCostCleanBos.add(extendCostCleanBo);
        }
        return extendCostCleanBos;
    }
}
