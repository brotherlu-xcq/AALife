package com.aalife.service.impl;

import com.aalife.bo.LoginBo;
import com.aalife.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Service;


/**
 * @author brother lu
 * @date 2018-06-04
 */

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void login(LoginBo loginBo) {
        UsernamePasswordToken token = new UsernamePasswordToken(loginBo.getLoginKey(), (String)null);
        try{
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e){
            e.printStackTrace();
            throw new AuthenticationException("登录失败");
        }
    }
}
