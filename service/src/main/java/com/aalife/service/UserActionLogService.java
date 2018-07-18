package com.aalife.service;

import java.util.Date;

/**
 * @author mosesc
 * @date 2018-07-03
 */
public interface UserActionLogService {

    /**
     * 保存用户和系统行为Log
     * @param methodName
     * @param requestUrl
     * @param inParams
     * @param outParams
     * @param exception
     * @param sessionId
     * @param ipAddress
     * @param startDate
     * @param endDate
     */
    void saveUserActionLog(String methodName, String requestUrl, String inParams, String outParams, String exception, String sessionId, String ipAddress, Date startDate, Date endDate);
}
