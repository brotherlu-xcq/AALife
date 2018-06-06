package com.aalife.service;

import com.aalife.dao.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * @author brother lu
 * @date 2018-06-05
 */
@Component
public class WebContext {
    /**
     * 获取当前的用户
     * @return
     */
    public User getCurrentUser(){
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        return currentUser;
    }
}
