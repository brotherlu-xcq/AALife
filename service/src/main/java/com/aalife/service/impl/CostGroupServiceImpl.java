package com.aalife.service.impl;

import com.aalife.bo.CostGroupBo;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.dao.repository.CostGroupUserRepository;
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

import java.util.Date;

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
    @RolePermission(needPermission = PermissionType.USER)
    public void updateCostGroup(CostGroupBo costGroupBo) {
        String groupName = costGroupBo.getGroupName();

//        costGroupRepository.
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
}
