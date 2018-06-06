package com.aalife.framework.aop;

import com.aalife.dao.entity.CostGroupUser;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.CostGroupUserRepository;
import com.aalife.dao.repository.UserRepository;
import com.aalife.framework.annotation.RolePermission;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.WebContext;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author brother lu
 * @date 2018-06-06
 */
@Aspect
@Component
public class CheckRolePermission {
    @Autowired
    private WebContext webContext;
    @Autowired
    private CostGroupUserRepository costGroupUserRepository;

    @Before(value = "@annotation(rolePermission)")
    public void checkRole(ProceedingJoinPoint joinPoint, RolePermission rolePermission){
        User user = webContext.getCurrentUser();
        PermissionType permissionType = rolePermission.needPermission();
        Object[] objects = joinPoint.getArgs();
        Integer groupId = (Integer) objects[0];
        CostGroupUser costGroupUser = costGroupUserRepository.findCostGroupByUserAndGroup(user.getUserId(), groupId);
        if (costGroupUser == null){
            throw new UnauthorizedException();
        }

        if (permissionType == PermissionType.ADMIN && costGroupUser.getAdmin().equals('N')){
            throw new UnauthorizedException();
        }

    }
}
