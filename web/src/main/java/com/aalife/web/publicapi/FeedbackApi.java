package com.aalife.web.publicapi;

import com.aalife.web.util.JsonEntity;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-06-12
 */
@Api
@RequiresAuthentication
@RestController
@RequestMapping(value = "/public/api")
public class FeedbackApi {
//    public JsonEntity<String> createNew
}
