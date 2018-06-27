package com.aalife.utils;

import com.aalife.dao.entity.AppConfig;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

/**
 * @author mosesc
 * @date 2018-06-26
 */
public class InvoiceUtil {
    private InvoiceUtil(){}
    private static Logger logger = Logger.getLogger(InvoiceUtil.class);

    public static String getToken(String secret, String key, String tokenHost){
        String url = tokenHost+"?grant_type=client_credentials&client_id="+secret+"&client_secret="+key;
        String data = HttpUtil.doGet(url);
        logger.info("token data:"+data);
        JSONObject object = JSONObject.parseObject(data);
        String token = object.getString("access_token");
        return token;
    }
}
