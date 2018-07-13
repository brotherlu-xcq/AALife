package com.aalife.service.impl;

import com.aalife.bo.WxNotificationBo;
import com.aalife.bo.WxNotificationDetailBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.User;
import com.aalife.dao.entity.UserWxForm;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.dao.repository.UserWxFormRepository;
import com.aalife.service.NotificationService;
import com.aalife.service.UserActionLogService;
import com.aalife.service.WXService;
import com.aalife.service.WebContext;
import com.aalife.utils.DateUtil;
import com.aalife.utils.HttpUtil;
import com.aalife.utils.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-13
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private static Logger logger = Logger.getLogger(NotificationService.class);
    @Autowired
    private WebContext webContext;
    @Autowired
    private UserWxFormRepository userWxFormRepository;
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private UserActionLogService userActionLogService;

    @Override
    public void collectFormId(String formId) {
        if (StringUtils.isEmpty(formId) || formId.trim().length() == 0){
            logger.warn("保存微信formId失败，传入参数不存在");
            return;
        }
        User currentUser = webContext.getCurrentUser();
        UserWxForm userWxForm = new UserWxForm();
        userWxForm.setUser(currentUser);
        userWxForm.setFormId(formId);
        userWxForm.setEntryId(currentUser.getUserId());
        userWxForm.setEntryDate(new Date());
        userWxFormRepository.save(userWxForm);
    }

    @Override
    @Async
    public void sendWxNotification(Integer targetUserId, String templateCate, WxNotificationDetailBo content, Integer targetId) {
        logger.info("================== 开始异步发送微信通知 ===================");
        long startTime = System.currentTimeMillis();
        List<UserWxForm> userWxForms = userWxFormRepository.findUserWxFormByUserId(targetUserId);
        boolean sentNotification = false;
        String userName = null;
        if (userWxForms != null && userWxForms.size() > 0){
            User targetUser = null;
            for (UserWxForm userWxForm : userWxForms){
                targetUser = targetUser == null ? userWxForm.getUser() : targetUser;
                String wxOpenId = targetUser.getWxOpenId();
                // 初始化模板信息
                WxNotificationBo wxNotificationBo = new WxNotificationBo();
                wxNotificationBo.setData(content);
                wxNotificationBo.setForm_id(userWxForm.getFormId());
                wxNotificationBo.setTouser(wxOpenId);
                wxNotificationBo.setEmphasis_keyword("keyword1");
                wxNotificationBo.setPage("");
                wxNotificationBo.setTemplate_id(templateId);
                String url = null;
                String data = null;
                String exception = null;
                Date today = new Date();
                userName = targetUser.getNickName();

                Date entryDate = userWxForm.getEntryDate();
                boolean outTime = DateUtil.getHoursGap(entryDate, today)/24 > 7;
                // 如果formId超时那么删除它
                if (outTime){
                    deleteFormId(userWxForm, SystemConstant.NOTIFICATION_MSG1);
                    continue;
                }
                try {
                    data = HttpUtil.doPost(url, JSONUtil.object2JsonString(wxNotificationBo));
                    deleteFormId(userWxForm, SystemConstant.NOTIFICATION_MSG2);
                    sentNotification = true;
                    logger.info("=============== 成功向用户：{userId:"+targetUserId+", userName: "+userName+"}发送通知 ===============");
                    break;
                } catch (Exception e){
                    exception = e.getMessage();
                    logger.warn("发送通知失败", e);
                    deleteFormId(userWxForm, e.getMessage());
                } finally {
                    // 保存请求日志
                    try {
                        userActionLogService.saveUserActionLog(NotificationService.class.getName()+".sendWxNotification", "POST "+url, content, data, exception, null, null, today, new Date());
                    } catch (Exception e){
                        logger.info("保存请求日志失败", e);
                    }
                }
            }
        }
        if (!sentNotification){
            // 保存未发送通知日志
            try {
                userActionLogService.saveUserActionLog(NotificationService.class.getName()+".sendWxNotification", null, "待发送人信息：{userId: "+targetUserId +", userName:"+userName+"}", null, "该用户未发送微信通知", null, null, new Date(), new Date());
            } catch (Exception e){
                logger.info("保存请求日志失败", e);
            }
        }
        long gaps = System.currentTimeMillis() - startTime;
        logger.info("================== 发送通知结束，花费时间：" + gaps + "ms =================");
    }

    /**
     * 删除formID
     * @param userWxForm
     * @param comment
     */
    private void deleteFormId(UserWxForm userWxForm, String comment){
        Integer currentUserId = webContext.getCurrentUser().getUserId();
        Date today =  new Date();
        userWxForm.setDeleteId(currentUserId);
        userWxForm.setDeleteDate(today);
        userWxForm.setComment(comment);
        userWxFormRepository.save(userWxForm);
    }
}
