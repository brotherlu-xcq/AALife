package com.aalife.utils;

import com.aalife.bo.WxUserBo;
import com.aalife.dao.entity.User;
import com.aalife.exception.BizException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * @author brother lu
 * @date 2018-06-06
 */
public class WxUtil {
    private WxUtil(){}
    private static Logger logger = Logger.getLogger(WxUtil.class);

    public static User getWXUserInfo(WxUserBo wxUser, String appId, String secret, String host) throws Exception {
        final String jsCode = wxUser.getWxCode();
        String url = host+"?appid="+appId+"&secret="+secret+"&js_code="+jsCode+"&grant_type=authorization_code";
        String data = HttpUtil.doGet(url);
        JSONObject object = JSON.parseObject(data);
        String sessionKey = object.getString("session_key");
        String openId = object.getString("openid");
        try {
            String result = AesCbcUtil.decrypt(wxUser.getEncryptedData(), sessionKey, wxUser.getIv(), "UTF-8");
            logger.info("parse user info result: " +result);
            JSONObject userInfo = JSON.parseObject(result);
            User user = new User();
            user.setNickName(userInfo.getString("nickName"));
            user.setWxOpenId(openId);
            user.setAvatarUrl(userInfo.getString("avatarUrl"));
            return user;
        } catch (Exception e){
            logger.error("解析用户信息出错", e);
            throw new BizException("解析用户信息出错");
        }
    }
}
