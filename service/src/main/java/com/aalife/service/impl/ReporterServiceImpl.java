package com.aalife.service.impl;

import com.aalife.constant.SystemConstant;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.service.NotificationService;
import com.aalife.service.ReporterService;
import com.aalife.service.TemplateService;
import com.aalife.utils.FormatUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-07-16
 */
@Service
public class ReporterServiceImpl implements ReporterService {
    private static Logger logger = Logger.getLogger(ReporterService.class);
    @Autowired
    private TemplateService templateService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AppConfigRepository appConfigRepository;

    /**
     *  *  *  *  *  *  *
     * 秒 分 时 日 月 年
     *  *代表任意时间执行
     *  -代表时间段执行
     *  ,多个值
     *  ?非明确值
     */
    @Override
    @Scheduled(cron = "* 0/10 * * * *")
    public void sendDailyBizNotification() {
        logger.info("===================== 开始每日报告分析 ===================");
        long startTime = System.currentTimeMillis();
//        String templateFile = SystemConstant.DAILY_BIZ_REPORT_TEMPLATE;
        String templateFile = SystemConstant.DAILY_REPORT;
        String to = appConfigRepository.findAppConfigValueByName(SystemConstant.MAIL_TO, SystemConstant.DAILY_REPORT);
        String cc = appConfigRepository.findAppConfigValueByName(SystemConstant.MAIL_CC, SystemConstant.DAILY_REPORT);
        String subject = appConfigRepository.findAppConfigValueByName(SystemConstant.MAIL_SUB, SystemConstant.DAILY_REPORT);
        Map<String, Object> model = initDailyBizData();
        String content = templateService.process(templateFile, model, false);
        notificationService.sendMailNotification(to, cc, cc, subject, content, null);
        long gaps = System.currentTimeMillis() - startTime;
        logger.info("===================== 结束每日报告分析，花费时间："+gaps+"ms ===================");
    }

    /**
     * 初始化发送数据
     * @return
     */
    private Map<String, Object> initDailyBizData(){
        Map<String,Object> result = new HashMap<>(12);
        result.put("date", FormatUtil.formatDate2String(new Date(), SystemConstant.DATEPATTERN));
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);
        return result;
    }
}
