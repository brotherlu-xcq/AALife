package com.aalife.service.impl;

import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.exception.BizException;
import com.aalife.service.InvoiceService;
import com.aalife.service.UserActionLogService;
import com.aalife.utils.HttpUtil;
import com.aalife.utils.JSONUtil;
import com.aalife.utils.UUIDUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mosesc on 07/04/18.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private static Logger logger = Logger.getLogger(InvoiceService.class);
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private UserActionLogService userActionLogService;

    @Override
    public String getToken() {
        String url = null;
        String data = null;
        String exception = null;
        Date startDate = new Date();
        try {
            String secret = appConfigRepository.findAppConfigValueByName("INVOICE", "SECRET");
            String key = appConfigRepository.findAppConfigValueByName("INVOICE", "KEY");
            String tokenHost = appConfigRepository.findAppConfigValueByName("INVOICE", "TOKEN_HOST");
            url = tokenHost+"?grant_type=client_credentials&client_id="+secret+"&client_secret="+key;
            data = HttpUtil.doGet(url);
            logger.info("token data:"+data);
            JSONObject object = JSONObject.parseObject(data);
            String token = object.getString("access_token");
            if (token == null){
                throw new BizException("token请求失败，返回数据："+data);
            }
            return token;
        } catch (Exception e){
            exception = e.getMessage();
            throw new BizException("请求语音API获取语音Token失败", e);
        } finally {
            try {
                userActionLogService.saveUserActionLog(InvoiceService.class.getName()+".getToken", "GET "+url, null, data, exception, null, null, startDate, new Date());
            } catch (Exception e){
                logger.warn("保存日志失败", e);
            }
        }
    }

    @Override
    public String getInvoiceContent(String token, String fileType, byte[] content) {
        String host = null;
        String data = null;
        String exception = null;
        Date startDate = new Date();
        try {
            String speech = Base64.getEncoder().encodeToString(content);
            logger.info("fileType:" + fileType);
            Map<String, Object> params = new HashMap<>(8);
            params.put("dev_pid", 1537);
            params.put("format", fileType);
            params.put("rate", 16000);
            params.put("token", token);
            params.put("cuid", UUIDUtil.get16BitUUID());
            params.put("channel", "1");
            params.put("len", content.length);
            params.put("speech", speech);
            host = appConfigRepository.findAppConfigValueByName("INVOICE", "HOST");
            data = HttpUtil.doPost(host, JSONUtil.object2JsonString(params));
            logger.info("data:" + data);
            JSONObject object = JSONObject.parseObject(data);
            String result = object.getString("result");
            if (result == null){
                throw new BizException("语音信息请求失败，返回数据："+data);
            }
            return result;
        } catch (Exception e){
            exception = e.getMessage();
            throw new BizException("请求语音API获取语音信息失败", e);
        } finally {
            try {
                userActionLogService.saveUserActionLog(InvoiceService.class.getName()+".getInvoiceContent", "POST "+host, null, data, exception, null, null, startDate, new Date());
            } catch (Exception e){
                logger.warn("保存日志失败", e);
            }
        }
    }
}
