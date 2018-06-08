package com.aalife.web.publicapi;

import com.aalife.bo.CostDetailBo;
import com.aalife.bo.NewCostDetailBo;
import com.aalife.framework.annotation.RolePermission;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostDetailService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Api
@RequiresAuthentication
@RestController
@RequestMapping(value = "/public/api")
public class CostDetailApi {
    @Autowired
    private CostDetailService costDetailService;

    @RequestMapping(value = "/costDetail/{groupId}", method = RequestMethod.PUT)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<String> createNewCostDetail(@PathVariable(value = "groupId") Integer groupId, @RequestBody NewCostDetailBo costDetailBo){
        costDetailBo.setGroupId(groupId);
        costDetailService.createNewCostDetail(costDetailBo);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costDetail/{groupId}/clean", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> cleanCostDetail(@PathVariable(value = "groupId") Integer groupId, @RequestBody String comment){
        costDetailService.cleanCostDetail(groupId, comment);
        return ResponseHelper.createInstance("success");
    }
}
