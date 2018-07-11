package com.aalife.web.publicapi;

import com.aalife.bo.CostCleanSummaryBo;
import com.aalife.bo.ExtendCostCleanBo;
import com.aalife.framework.annotation.RolePermission;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostCleanService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-10
 */
@Api
@RequestMapping(value = "/public/api")
@RestController
@RequiresAuthentication
public class CostCleanApi {
    @Autowired
    private CostCleanService costCleanService;

    @RequestMapping(value = "/costClean/costGroup/{groupId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<List<ExtendCostCleanBo>> listCostCleans(@PathVariable(value = "groupId") Integer groupId){
        return ResponseHelper.createInstance(costCleanService.listCostCleans(groupId));
    }

    @RequestMapping(value = "/costClean/{cleanId}/costGroup/{groupId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostCleanSummaryBo> listCostCleanSummary(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "cleanId") Integer cleanId){
        return ResponseHelper.createInstance(costCleanService.findCostCleanSummaryById(groupId, cleanId));
    }
    @RequestMapping(value = "/costClean/unClean/costGroup/{groupId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostCleanSummaryBo> listCostCleanSummary(@PathVariable(value = "groupId") Integer groupId){
        return ResponseHelper.createInstance(costCleanService.findCostCleanSummaryById(groupId, null));
    }
}
