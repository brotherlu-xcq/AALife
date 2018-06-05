package com.aalife.web.api;

import com.aalife.web.session.WebContext;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther brother lu
 * @date 2018-06-05
 */
@RestController
@RequestMapping(value = "/public/api/user")
@Api
@RequiresAuthentication
public class UserApi {
    @Autowired
    private WebContext webContext;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public JsonEntity getCurrentUser(){
        return ResponseHelper.createInstance(webContext.getCurrentUser());
    }
}
