package com.aalife.service.impl;

import com.aalife.bo.WxUserBo;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.exception.BizException;
import com.aalife.service.InvoiceService;
import com.aalife.service.UserActionLogService;
import com.aalife.service.WXService;
import com.aalife.utils.AesCbcUtil;
import com.aalife.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author mosesc
 * @date 2018-07-04
 */
@Service
@Transactional
public class WXServiceImpl implements WXService {
    private static Logger logger = Logger.getLogger(WXService.class);
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private UserActionLogService userActionLogService;

    @Override
    public User getWXUserInfo(WxUserBo wxUser) {

        String appId = appConfigRepository.findAppConfigValueByName("WX", "APPID");
        String secret = appConfigRepository.findAppConfigValueByName("WX", "SECRET");
        String host = appConfigRepository.findAppConfigValueByName("WX", "HOST");
        final String jsCode = wxUser.getWxCode();
        String data = null;
        String url = null;
        String exception = null;
        Date startDate = new Date();
        try {
            url = host+"?appid="+appId+"&secret="+secret+"&js_code="+jsCode+"&grant_type=authorization_code";
            data = HttpUtil.doGet(url);
        } catch (Exception e){
            throw new BizException("请求微信API获取用户信息失败", e);
        } finally {
            try {
                userActionLogService.saveUserActionLog(InvoiceService.class.getName()+".getWXUserInfo", "GET "+url, null, data, exception, null, null, startDate, new Date());
            } catch (Exception e){
                logger.warn("保存日志失败", e);
            }
        }
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
