package com.aalife.service.impl;

import com.aalife.bo.ReporterBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.repository.*;
import com.aalife.service.CostDetailService;
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
    @Autowired
    private CostDetailRepository costDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private CostCleanRepository costCleanRepository;
    @Autowired
    private UserActionLogRepository userActionLogRepository;
    @Autowired
    private UserLoginRepository userLoginRepository;

    /**
     * 每天23:58分发送一次邮件
     *  *  *  *  *  *  *
     * 秒 分 时 日 月 年
     *  *代表任意时间执行
     *  -代表时间段执行
     *  ,多个值
     *  ?非明确值
     */
    @Override
    @Scheduled(cron = "0 58 23 * * *")
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
        result.put("date", FormatUtil.formatDate2String(new Date(), SystemConstant.DATE_PATTERN));
        List<ReporterBo> data = new ArrayList<>();
        // 消费记录统计
        Integer newCostDetailCount = costDetailRepository.findCostDetailNewDailyReport();
        ReporterBo newCostDetail = new ReporterBo("新增消费记录", newCostDetailCount);
        data.add(newCostDetail);
        Integer deleteCostDetailCount = costDetailRepository.findCostDetailDeleteDailyReport();
        ReporterBo deleteCostDetail = new ReporterBo("删除的消费记录", deleteCostDetailCount);
        data.add(deleteCostDetail);
        Integer cleanCostDetailCount = costDetailRepository.findCostDetailCleanDailyReport();
        ReporterBo cleanCostDetail = new ReporterBo("结算的消费记录", cleanCostDetailCount);
        data.add(cleanCostDetail);
        // 用户增量统计
        Integer newUserCount = userRepository.findDailyUserReport();
        ReporterBo newUser = new ReporterBo("新增用户", newUserCount);
        data.add(newUser);
        // 账单统计
        Integer newCostGroupCount = costGroupRepository.findCostGroupNewDailyReport();
        ReporterBo newCostGroup = new ReporterBo("新增账单", newCostGroupCount);
        data.add(newCostGroup);
        Integer deleteCostGroupCount = costGroupRepository.findCostGroupDeleteDailyReport();
        ReporterBo deleteCostGroup = new ReporterBo("删除账单", deleteCostGroupCount);
        data.add(deleteCostGroup);
        // 结算统计
        Integer costCleanCount = costCleanRepository.findCostCleanDailyReport();
        ReporterBo costClean = new ReporterBo("新增结算", costCleanCount);
        data.add(costClean);
        // 日志增加统计
        Integer userActionLogCount = userActionLogRepository.findUserActionLogDailyReport();
        ReporterBo reporterBo = new ReporterBo("接口调用次数", userActionLogCount);
        data.add(reporterBo);
        // 统计用户登录
        Integer userLoginCount = userLoginRepository.findUserLoginDailyReport();
        ReporterBo userLogin = new ReporterBo("用户登录次数", userLoginCount);
        data.add(userLogin);

        result.put("data", data);
        return result;
    }
}
