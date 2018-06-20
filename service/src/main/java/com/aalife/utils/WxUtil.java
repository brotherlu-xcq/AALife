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

    public static User getWXUserInfo(WxUserBo wxUser) throws Exception {
        final String APPID ="wx9c7abce098df46e1";
        final String SECRET = "9fcee84ff426e4a939833a87e56c32e9";
        final String JSCODE = wxUser.getWxCode();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+JSCODE+"&grant_type=authorization_code";
        String data = HttpUtil.doGet(url);
        logger.info("user info return " + data);
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
