package com.aalife.web.publicapi;

import com.aalife.bo.ApprovalBo;
import com.aalife.bo.ApprovalInfoBo;
import com.aalife.framework.annotation.RolePermission;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostGroupApprovalService;
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

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Api
@RequestMapping(value = "/public/api")
@RestController
@RequiresAuthentication
public class CostGroupApprovalApi {
    @Autowired
    private CostGroupApprovalService costGroupApprovalService;

    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    public JsonEntity<String> createApproval(@RequestBody ApprovalBo approvalBo){
        costGroupApprovalService.createNewApproval(approvalBo);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/approval/list/{groupId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<List<ApprovalInfoBo>> listApprovalsByGroup(@PathVariable(value = "groupId") Integer groupId){
        return ResponseHelper.createInstance(costGroupApprovalService.listApprovalsByGroup(groupId));
    }

    @RequestMapping(value = "/approval/{groupId}/user/{userId}", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> approveUserRequest(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "userId") Integer userId){
        costGroupApprovalService.approveUserRequest(groupId, userId);
        return ResponseHelper.createInstance("success");
    }
}
