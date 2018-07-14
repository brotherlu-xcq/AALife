package com.aalife.service;

import com.aalife.bo.WxNotificationDetailBo;

import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-13
 */
public interface NotificationService {
    /**
     * 收集当前用户的formId用于发送模板消息
     * @param formId
     */
    void collectFormId(String formId);

    /**
     * 发送微信模板信息
     * @param targetUserId
     * @param templateCate
     * @param content
     * @param tail
     */
    void sendWxNotification(Integer targetUserId, String templateCate, WxNotificationDetailBo content, String tail);
}
