package com.aalife.service.realm;

import com.aalife.dao.entity.User;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.UserLoginService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author brother lu
 * @date 2018-05-31
 */
@Transactional(rollbackFor = BizException.class)
public class AALifeShiroRealm extends AuthorizingRealm {
    private static Logger logger = Logger.getLogger(AALifeShiroRealm.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLoginService userLoginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("start get the role and permissions");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String loginKey = (String) token.getPrincipal();
        User user = userRepository.findUserWithOpenId(loginKey);
        if (user == null){
            throw new AuthenticationException("登录失败，用户不存在！");
        }
        userLoginService.createLoginLog(user);
        return new SimpleAuthenticationInfo(user, null, getName());
    }
}
