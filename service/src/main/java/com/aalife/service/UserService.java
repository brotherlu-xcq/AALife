package com.aalife.service;

import com.aalife.bo.LoginBo;

/**
 * 用户登录类
 * @author brother lu
 * @date 2018-06-04
 */
public interface UserService {
    /**
     * 用户登录
     * @param loginBo
     */
    void login(LoginBo loginBo);
}
