package com.aalife.service.impl;

import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.User;
import com.aalife.dao.entity.UserActionLog;
import com.aalife.dao.repository.UserActionLogRepository;
import com.aalife.exception.BizException;
import com.aalife.service.UserActionLogService;
import com.aalife.service.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author mosesc
 * @date 2018-07-03
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BizException.class)
public class UserActionLogServiceImpl implements UserActionLogService {
    @Autowired
    private WebContext webContext;
    @Autowired
    private UserActionLogRepository userActionLogRepository;

    @Override
    public void saveUserActionLog(String methodName, String requestUrl, String inParams, String outParams, String exception, String sessionId, String ipAddress, Date startDate, Date endDate) {
        UserActionLog userActionLog = new UserActionLog();
        userActionLog.setUser(webContext.getCurrentUser());
        userActionLog.setMethodName(methodName);
        userActionLog.setRequestUrl(requestUrl);
        userActionLog.setInParams(inParams);
        userActionLog.setOutParams(outParams);
        userActionLog.setException(exception);
        userActionLog.setSessionId(sessionId);
        userActionLog.setIpAddress(ipAddress);
        userActionLog.setStartDate(startDate);
        userActionLog.setEndDate(endDate);
        userActionLog.setEntryId(SystemConstant.SYSTEM_ID);
        userActionLog.setEntryDate(new Date());
        userActionLogRepository.save(userActionLog);
    }
}
