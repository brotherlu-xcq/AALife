package com.aalife.web.publicapi;

import com.aalife.bo.CostUserRemarkBo;
import com.aalife.service.CostUserRemarkService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-06-07
 */
@Api
@RequestMapping(value = "/public/api")
@RestController
@RequiresAuthentication
public class CostUserRemarkApi {
    @Autowired
    private CostUserRemarkService costUserRemarkService;

    @RequestMapping(value = "/remarkName", method = RequestMethod.PUT)
    public JsonEntity<String> createRemarkName(@RequestBody CostUserRemarkBo costUserRemarkBo){
        costUserRemarkService.createRemarkName(costUserRemarkBo);
        return ResponseHelper.createInstance("success");
    }
}
