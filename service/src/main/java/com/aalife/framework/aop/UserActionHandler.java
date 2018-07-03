package com.aalife.framework.aop;

import com.aalife.dao.entity.UserActionLog;
import com.aalife.service.UserActionLogService;
import com.aalife.utils.FormatUtil;
import com.alibaba.fastjson.JSON;
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
        String methodName = joinPoint.getTarget().getClass().getName();
        String sessionId = request.getSession().getId();
        String requestUrl = request.getRequestURI();
        Date startDate = new Date();
        String ipAddress = request.getRemoteHost();
        String exception = null;
        Object data = null;
        // 初始化入参
        Object[] params = joinPoint.getArgs();
//        Map<String, Object> inParams = new HashMap<>(4);
//        if (params != null && params.length > 0){
//            for (int i = 0; i < params.length; i++){
//                inParams.put("arg"+i, params[i]);
//            }
//        }
        try {
            data = joinPoint.proceed();
            return data;
        } catch (Throwable throwable) {
            exception =  throwable.getMessage();
            throw throwable;
        } finally {
            try {
                userActionLogService.saveUserActionLog(methodName, requestUrl, JSON.toJSONString(""), JSON.toJSONString(data), exception, sessionId, ipAddress, startDate, new Date());
            } catch (Exception e){
                logger.info("保存请求日志失败", e);
            }
        }
    }

}
