package com.aalife.service.impl;

import ch.qos.logback.core.net.SyslogConstants;
import com.aalife.bo.LoginBo;
import com.aalife.bo.WxUserBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.UserRepository;
import com.aalife.exception.BizException;
import com.aalife.service.UserService;
import com.aalife.utils.WxUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * @author brother lu
 * @date 2018-06-04
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void login(WxUserBo wxUser) {
        if (StringUtils.isEmpty(wxUser.getWxCode())){
            throw new AuthenticationException("登陆失败");
        }
        User wxUserInfo = WxUtil.getWXUserInfo(wxUser);
        String wxOpenId = wxUserInfo.getWxOpenId();
        // 查询没有该用户则添加一条记录
        User user = userRepository.findUserWithOpenId(wxOpenId);
        if (user == null){
            user = new User();
            user.setEntryId(SystemConstant.SYSTEM_ID);
            user.setEntryDate(new Date());
            user.setWxOpenId(wxOpenId);
        }
        user.setAvatarUrl(wxUserInfo.getAvatarUrl());
        user.setNickName(wxUserInfo.getNickName());
        userRepository.save(user);

        UsernamePasswordToken token = new UsernamePasswordToken(wxOpenId, (String)null);
        try{
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e){
            throw new AuthenticationException(e.getMessage());
        }
    }
}
