package com.aalife.web.api;

import com.aalife.bo.LoginBo;
import com.aalife.service.UserService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonEntity<String> login(@RequestBody LoginBo loginBo){
        userService.login(loginBo);
        return ResponseHelper.createInstance("success");
    }
}
