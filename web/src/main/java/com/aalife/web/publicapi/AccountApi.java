package com.aalife.web.publicapi;

import com.aalife.bo.LoginBo;
import com.aalife.bo.WxUserBo;
import com.aalife.service.UserService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.web.session.HttpServletSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author brother lu
 * @date 2018-06-04
 */
@RestController
@RequestMapping(value = "/public/api")
@Api
public class AccountApi {
    @Autowired
    private UserService userService;
    @Value("${aalife.env}")
    private String env;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonEntity<String> login(@RequestBody WxUserBo wxUser, HttpServletRequest request){
        if (env.equals("DEV")){
            userService.DEVLogin(wxUser);
        } else {
            userService.login(wxUser);
        }
        return ResponseHelper.createInstance(request.getSession().getId());
    }
}
