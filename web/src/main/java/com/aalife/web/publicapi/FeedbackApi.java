package com.aalife.web.publicapi;

import com.aalife.service.ReporterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-06-12
 */
@Api
@RestController
@RequestMapping(value = "/public/api")
public class FeedbackApi {
    @Autowired
    private ReporterService reporterService;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
    public void testSendEmail(){
        reporterService.sendDailyBizNotification();
    }
}
