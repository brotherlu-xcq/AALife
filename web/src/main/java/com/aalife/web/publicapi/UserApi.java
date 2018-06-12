package com.aalife.web.publicapi;

import com.aalife.bo.UserBo;
import com.aalife.bo.UserOverviewBo;
import com.aalife.dao.entity.User;
import com.aalife.service.CostDetailService;
import com.aalife.service.UserService;
import com.aalife.service.WebContext;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

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
    private UserService userService;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public JsonEntity<UserOverviewBo> getCurrentUser(){
        return ResponseHelper.createInstance(userService.getUserOverview());
    }
}
