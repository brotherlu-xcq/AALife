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

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Api
@RequestMapping(value = "/public/api")
@RestController
@RequiresAuthentication
public class CostGroupApproval {
    @Autowired
    private CostGroupApprovalService costGroupApprovalService;

    @RequestMapping(value = "/approval", method = RequestMethod.PUT)
    public JsonEntity<String> createApproval(@RequestBody ApprovalBo approvalBo){
        costGroupApprovalService.createNewApproval(approvalBo);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/approval/list/{groupId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<ApprovalInfoBo> listApprovalsByGroup(@PathVariable(value = "groupId") Integer groupId){
        costGroupApprovalService.
    }
}
