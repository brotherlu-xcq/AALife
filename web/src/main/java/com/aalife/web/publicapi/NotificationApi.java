package com.aalife.web.publicapi;

import com.aalife.constant.SystemConstant;
import com.aalife.dao.repository.AppConfigRepository;
import com.aalife.service.NotificationService;
import com.aalife.service.ReporterService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-07-13
 */
@Api
@RequiresAuthentication
@RequestMapping(value = "/public/api")
@RestController
public class NotificationApi {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ReporterService reporterService;
    @Autowired
    private AppConfigRepository appConfigRepository;

    @RequestMapping(value = "/notification/collect/formId", method = RequestMethod.POST)
    public JsonEntity<String> collectFormId(@RequestBody String formId){
        notificationService.collectFormId(formId);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/notification/send/dailyReport")
    public JsonEntity<String> sendDailyReport(){
        String env = appConfigRepository.findAppConfigValueByName(SystemConstant.AALIFE, SystemConstant.ENV);
        if (!StringUtils.isEmpty(env) && env.equalsIgnoreCase(SystemConstant.DEV)){
            reporterService.sendDailyBizNotification();
        } else {
            throw new UnauthorizedException();
        }
        return ResponseHelper.createInstance("success");
    }
}
