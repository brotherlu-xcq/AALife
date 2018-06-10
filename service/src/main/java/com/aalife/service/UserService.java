package com.aalife.service;

import com.aalife.bo.LoginBo;
import com.aalife.bo.WxUserBo;
import com.aalife.dao.entity.User;

/**
 * 用户登录类
 * @author brother lu
 * @date 2018-06-04
 */
public interface UserService {
    /**
     * 用户登录
     * @param wxUser
     */
    void login(WxUserBo wxUser);
//    User createNewUser();
}
