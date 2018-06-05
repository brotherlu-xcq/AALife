package com.aalife.service;

import com.aalife.dao.entity.User;

/**
 * @author brother lu
 * @date 2018-06-05
 */
public interface UserLoginService {
    /**
     * 创建一个登陆记录
     * @param user
     */
    void createLoginLog(User user);
}
