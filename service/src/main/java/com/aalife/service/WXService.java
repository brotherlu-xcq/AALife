package com.aalife.service;

import com.aalife.bo.WxUserBo;
import com.aalife.dao.entity.User;

/**
 * @author mosesc
 * @date 2018-07-04
 */
public interface WXService {
    /**
     * 请求微信API获取微信用户信息
     * @param wxUser
     * @return
     */
    User getWXUserInfo(WxUserBo wxUser);

    /**
     * 请求获取微信session
     * @return
     */
    String getWXUserAccessToken();
}
