package com.aalife.service.impl;

import com.aalife.bo.LoginBo;
import com.aalife.exception.BizException;
import com.aalife.service.UserService;
import com.aalife.utils.WxUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author brother lu
 * @date 2018-06-04
 */

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void login(String wxCode) {
        if (StringUtils.isEmpty(wxCode)){
            throw new AuthenticationException("登陆失败");
        }

        String openId = WxUtil.getWXOpenId(wxCode);

        UsernamePasswordToken token = new UsernamePasswordToken(wxCode, (String)null);
        try{
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e){
            throw new AuthenticationException(e.getMessage());
        }
    }
}
