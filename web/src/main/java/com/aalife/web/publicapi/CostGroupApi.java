package com.aalife.web.publicapi;

import com.aalife.bo.CostGroupBo;
import com.aalife.bo.CostGroupOverviewBo;
import com.aalife.bo.CostGroupUserBo;
import com.aalife.framework.annotation.RolePermission;
import com.aalife.framework.constant.PermissionType;
import com.aalife.service.CostGroupService;
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
import springfox.documentation.spring.web.json.Json;

import java.util.List;

/**
 *
 * @auther brother lu
 * @date 2018-06-06
 */
@RestController
@Api
@RequestMapping(value = "/public/api")
@RequiresAuthentication
public class CostGroupApi {
    @Autowired
    private CostGroupService costGroupService;
    /**
     * 创建新的group
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/costGroup", method = RequestMethod.PUT)
    public JsonEntity createCostGroup(@RequestBody String groupName){
        costGroupService.createNewCostGroup(groupName);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costGroup/{groupId}", method =RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<String> updateCostGroup(@PathVariable(value = "groupId") Integer groupId, @RequestBody String groupName){
        CostGroupBo costGroupBo = new CostGroupBo();
        costGroupBo.setGroupNo(groupId);
        costGroupBo.setGroupName(groupName);
        costGroupService.updateCostGroup(costGroupBo);
        return ResponseHelper.createInstance("success");
    }

    /**
     * 根据Code去查找对应得group
     * @return
     */
    @RequestMapping(value = "/costGroup/byCode/{groupCode}", method = RequestMethod.GET)
    public JsonEntity<CostGroupBo> findCostGroupByCode(@PathVariable(value = "groupCode") String groupCode){
        CostGroupBo costGroupBo = costGroupService.findCostGroupByCode(groupCode);
        return ResponseHelper.createInstance(costGroupBo);
    }

    @RequestMapping(value = "/costGroup/{groupId}", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> deleteCostGroup(@PathVariable(value = "groupId") Integer groupId){
        costGroupService.deleteCostGroup(groupId);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costGroup/{groupId}/leave", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<String> leaveCostGroup(@PathVariable(value = "groupId") Integer groupId){
        costGroupService.leaveCostGroup(groupId);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costGroup/{groupId}/assign/{userId}", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> assignGroupAdmin(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "userId") Integer userId){
        costGroupService.assignGroupAdmin(groupId, userId);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costGroup/{groupId}/delete/{userId}", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> deleteCostGroupUser(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "userId") Integer userId){
        costGroupService.deleteCostGroupUser(groupId, userId);
        return ResponseHelper.createInstance("success");
    }

    @RequestMapping(value = "/costGroup/{groupId}/overview", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostGroupOverviewBo> costGroupOverview(@PathVariable(value = "groupId") Integer groupId){
        return ResponseHelper.createInstance(costGroupService.listCostGroupOverview(groupId));
    }

    @RequestMapping(value = "/costGroup/mine/overview", method = RequestMethod.GET)
    public JsonEntity<List<CostGroupOverviewBo>> listMyGroupOverview(){
        return ResponseHelper.createInstance(costGroupService.listCostGroupOverview());
    }

    @RequestMapping(value = "/costGroup/listMine", method = RequestMethod.GET)
    public JsonEntity<List<CostGroupBo>> listMyCostGroups(){
        return ResponseHelper.createInstance(costGroupService.listMyGroups());
    }

    @RequestMapping(value = "/costGroup/{groupId}/user/{userId}", method = RequestMethod.GET)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostGroupUserBo> findGroupUserById(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "userId") Integer userId){
        return ResponseHelper.createInstance(costGroupService.findCostGroupUserById(groupId, userId));
    }
}
