package com.aalife.utils;

import com.aalife.dao.entity.AppConfig;
import com.aalife.dao.repository.AppConfigRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-06-26
 */
public class InvoiceUtil {
    private InvoiceUtil(){}
    private static Logger logger = Logger.getLogger(InvoiceUtil.class);
    @Autowired
    private static AppConfigRepository appConfigRepository;

    public static String getToken(){
        AppConfig tokenConfig = appConfigRepository.findAppConfigByName("INVOICE", "TOKEN");
        int leftDay = DateUtil.getHoursGap(tokenConfig.getEntryDate(), new Date())/24;
        if (tokenConfig != null && leftDay < 28){
            return tokenConfig.getConfigValue();
        }
        String secret = appConfigRepository.findAppConfigValueByName("INVOICE", "SECRET");
        String key = appConfigRepository.findAppConfigValueByName("INVOICE", "KEY");
        String tokenUrl = appConfigRepository.findAppConfigValueByName("INVOICE", "TOKEN_HOST");
        String url = tokenUrl+"?grant_type?client_credentials&client_id"+secret+"&client_secret"+key;
        String data = HttpUtil.doPost(url, (Map) null);
        logger.info("token data:"+data);
        String token = "";
        if (tokenConfig == null){
            tokenConfig = new AppConfig();
            tokenConfig.setAppName("INVOICE");
            tokenConfig.setConfigName("TOKEN");
        }
        tokenConfig.setConfigValue(token);
        appConfigRepository.save(tokenConfig);
        return token;
    }
}
