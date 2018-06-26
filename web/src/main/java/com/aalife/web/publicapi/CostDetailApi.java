package com.aalife.web.publicapi;

import com.aalife.bo.BaseQueryBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.CostDetailBo;
import com.aalife.bo.NewCostDetailBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.bo.WxQueryCriteriaBo;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/costDetail/{groupId}", method = RequestMethod.POST)
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

    @RequestMapping(value = "/costDetail/{groupId}/unclean/list", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<BaseQueryResultBo<CostDetailBo>> listUncleanCostDetail(@PathVariable(value = "groupId") Integer groupId, @RequestBody BaseQueryBo baseQueryBo){
        WxQueryBo wxQueryBo = new WxQueryBo();
        wxQueryBo.setSize(baseQueryBo.getSize());
        wxQueryBo.setPage(baseQueryBo.getPage());
        List<WxQueryCriteriaBo> criteria = new ArrayList<>();
        WxQueryCriteriaBo groupCriteria = new WxQueryCriteriaBo();
        groupCriteria.setFieldName("groupId");
        groupCriteria.setValue(groupId);
        criteria.add(groupCriteria);
        // 查询没有结算的
        WxQueryCriteriaBo cleanCriteria = new WxQueryCriteriaBo();
        cleanCriteria.setFieldName("cleanId");
        cleanCriteria.setValue(null);
        criteria.add(cleanCriteria);
        wxQueryBo.setCriteria(criteria);
        return ResponseHelper.createInstance(costDetailService.listUncleanCostDetailByGroup(wxQueryBo));
    }

    /**
     * groupId必传用于校验用户权限
     * @param groupId
     * @param costId
     * @return
     */
    @RequestMapping(value = "/costDetail/{groupId}/detail/{costId}", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.ADMIN)
    public JsonEntity<String> deleteCostDetail(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "costId") Integer costId){
        costDetailService.deleteCostDetail(costId);
        return ResponseHelper.createInstance("success");
    }


    @RequestMapping(value = "costDetail/{groupId}/invoice", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostDetailBo> createCostDetailByInvoice(@PathVariable(value = "groupId") Integer groupId, MultipartFile invoice){
        return ResponseHelper.createInstance(costDetailService.getCostDetailByInvoice(groupId, invoice));
    }
}
