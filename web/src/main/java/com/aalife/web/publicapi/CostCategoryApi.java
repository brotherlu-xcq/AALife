package com.aalife.web.publicapi;

import com.aalife.service.CostCategoryService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-06-11
 */
@Api
@RequiresAuthentication
@RequestMapping(value = "/public/api")
@RestController
public class CostCategoryApi {
    @Autowired
    private CostCategoryService costCategoryService;

    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public JsonEntity listCategory(){
        return ResponseHelper.createInstance(costCategoryService.findAllCategory());
    }
}
