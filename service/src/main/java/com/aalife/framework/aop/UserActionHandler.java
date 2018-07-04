package com.aalife.framework.aop;

import com.aalife.dao.entity.UserActionLog;
import com.aalife.exception.BizException;
import com.aalife.service.UserActionLogService;
import com.aalife.utils.FormatUtil;
import com.aalife.utils.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-03
 */
@Order(1)
@Aspect
@Component
public class UserActionHandler {
    public static Logger logger = Logger.getLogger(UserActionHandler.class);
    @Autowired
    private UserActionLogService userActionLogService;

    @Around(value = "execution(* com.aalife.web.publicapi.*.*(..))")
    public Object saveUserActionLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String methodName = joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName();
        String sessionId = request.getSession().getId();
        String requestUrl = request.getMethod()+" "+request.getRequestURI();
        Date startDate = new Date();
        String ipAddress = request.getRemoteHost();
        String exception = null;
        Object data = null;
        // 初始化入参
        Object[] params = joinPoint.getArgs();
        StringBuffer inParams = null;
        if (params != null && params.length > 0){
            inParams = new StringBuffer();
            for (int i = 0; i < params.length; i++){
                Object object = params[i];
                try {
                    if (i > 0){
                        inParams.append(",");
                    }
                    if (object instanceof MultipartFile){
                        throw new BizException("文件对象不进行转换");
                    }
                    inParams.append("arg"+i+"="+JSONUtil.object2JsonString(object));
                } catch (Exception e){
                    logger.warn("转化对象"+params[i]+"成JSON字符串失败", e);
                    inParams.append("arg"+i+"="+object.toString());
                }
            }
        }
        try {
            data = joinPoint.proceed();
            return data;
        } catch (Throwable throwable) {
            exception =  throwable.getMessage();
            throw throwable;
        } finally {
            try {
                String in = inParams == null ? null : inParams.toString();
                userActionLogService.saveUserActionLog(methodName, requestUrl, in, JSONUtil.object2JsonString(data), exception, sessionId, ipAddress, startDate, new Date());
            } catch (Exception e){
                logger.info("保存请求日志失败", e);
            }
        }
    }

}
