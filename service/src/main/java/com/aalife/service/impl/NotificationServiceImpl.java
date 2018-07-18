package com.aalife.service.impl;

import com.aalife.bo.WxNotificationBo;
import com.aalife.bo.WxNotificationDetailBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.User;
import com.aalife.dao.entity.UserWxForm;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.dao.repository.UserWxFormRepository;
import com.aalife.exception.BizException;
import com.aalife.service.NotificationService;
import com.aalife.service.UserActionLogService;
import com.aalife.service.WXService;
import com.aalife.service.WebContext;
import com.aalife.utils.DateUtil;
import com.aalife.utils.HttpUtil;
import com.aalife.utils.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-13
 */
@Service
@Transactional(rollbackFor = BizException.class)
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
    @Autowired
    private WXService wxService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.from}")
    private String mailFrom;

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
    public void sendWxNotification(Integer targetUserId, String templateCate, WxNotificationDetailBo content, String tail) {
        logger.info("================== 开始异步发送微信通知 ===================");
        long startTime = System.currentTimeMillis();
        List<UserWxForm> userWxForms = userWxFormRepository.findUserWxFormByUserId(targetUserId);
        boolean sentNotification = false;
        String userName = null;
        if (userWxForms != null && userWxForms.size() > 0){
            User targetUser = null;
            String templateId = appConfigRepository.findAppConfigValueByName(SystemConstant.WX_TEMP, templateCate);
            String pagePath = appConfigRepository.findAppConfigValueByName(SystemConstant.WX_PAGE, templateCate);
            String url = appConfigRepository.findAppConfigValueByName(SystemConstant.WX, SystemConstant.TEMPLATE_HOST);
            String accessToken = wxService.getWXUserAccessToken();
            url = url + accessToken;
            for (UserWxForm userWxForm : userWxForms){
                targetUser = targetUser == null ? userWxForm.getUser() : targetUser;
                String wxOpenId = targetUser.getWxOpenId();
                // 初始化模板信息
                WxNotificationBo wxNotificationBo = new WxNotificationBo();
                wxNotificationBo.setData(content);
                wxNotificationBo.setForm_id(userWxForm.getFormId());
                wxNotificationBo.setTouser(wxOpenId);
                wxNotificationBo.setEmphasis_keyword("keyword1.DATA");
                wxNotificationBo.setPage(pagePath+tail);
                wxNotificationBo.setTemplate_id(templateId);
                String contentTemp = JSONUtil.object2JsonString(wxNotificationBo);
                String data = null;
                String exception = null;
                Date today = new Date();
                userName = targetUser.getNickName();
                // 如果formId超时那么删除它
                Date entryDate = userWxForm.getEntryDate();
                boolean outTime = DateUtil.getHoursGap(entryDate, today)/24 > 7;
                if (outTime){
                    deleteFormId(userWxForm, SystemConstant.NOTIFICATION_MSG1);
                    continue;
                }
                // 开始发送模板信息
                try {
                    data = HttpUtil.doPost(url, contentTemp);
                    JSONObject object = JSON.parseObject(data);
                    // errcode为0代表发送成功
                    if (object.getIntValue("errcode") != 0){
                        throw new BizException("发送微信模板信息失败，返回数据："+data);
                    }
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
                        userActionLogService.saveUserActionLog(NotificationService.class.getName()+".sendWxNotification", "POST "+url, contentTemp, data, exception, null, null, today, new Date());
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

    @Override
    public void sendMailNotification(String to, String cc, String bcc, String subject, String mailContent, String fileToAttach) {
        logger.info("================= 发送邮件信息开始，收件人："+to+" ======================");
        long startTime = System.currentTimeMillis();
        Date today = new Date();
        String exception = null;
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            exception = e.getMessage();
            logger.warn("发送邮件失败", e);
        } finally {
            // 保存发送邮件日志
            try {
                userActionLogService.saveUserActionLog(NotificationService.class.getName()+".sendMailNotification", null, "{from:"+mailFrom+"， to:"+to+", cc:"+cc+", subject: "+subject+", content: "+mailContent+"}", null, exception, null, null, today, new Date());
            } catch (Exception e){
                logger.info("保存邮件日志失败", e);
            }
        }
        long gaps = System.currentTimeMillis() - startTime;
        logger.info("================= 发送邮件信息结束，花费时间："+gaps+"ms ==================");
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
