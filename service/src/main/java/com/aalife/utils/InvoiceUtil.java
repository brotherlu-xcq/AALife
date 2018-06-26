package com.aalife.utils;

import com.aalife.dao.entity.AppConfig;
import com.aalife.dao.repository.AppConfigRepository;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-06-26
 */
@Component
public class InvoiceUtil {
    private InvoiceUtil(){}
    private static Logger logger = Logger.getLogger(InvoiceUtil.class);
    @Autowired
    private static AppConfigRepository appConfigRepository;

    public static String getToken(AppConfig tokenConfig, String secret, String key, String tokenHost){
        if (tokenConfig.getEntryDate() != null){
            int leftDay = DateUtil.getHoursGap(tokenConfig.getEntryDate(), new Date())/24;
            if (leftDay < 28){
                return tokenConfig.getConfigValue();
            }
        }
        String url = tokenHost+"?grant_type=client_credentials&client_id="+secret+"&client_secret="+key;
        String data = HttpUtil.doPost(url, new HashMap(2));
        logger.info("token data:"+data);
        JSONObject object = JSONObject.parseObject(data);
        String token = object.getString("access_token");
        tokenConfig.setConfigValue(token);
        tokenConfig.setEntryDate(new Date());
        return token;
    }
}
