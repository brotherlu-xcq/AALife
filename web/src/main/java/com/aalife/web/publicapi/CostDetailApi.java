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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<Map<String, Integer>> cleanCostDetail(@PathVariable(value = "groupId") Integer groupId, @RequestBody String comment){
        Map<String, Integer> data = new HashMap<>(2);
        Integer cleanId = costDetailService.cleanCostDetail(groupId, comment);
        data.put("cleanId", cleanId);
        return ResponseHelper.createInstance(data);
    }

    @RequestMapping(value = "/costDetail/{groupId}/list", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<BaseQueryResultBo<CostDetailBo>> listCostDetailByGroup(@PathVariable(value = "groupId") Integer groupId,@RequestBody WxQueryBo wxQueryBo){
        List<WxQueryCriteriaBo> criteria = wxQueryBo.getCriteria();
        // 如果有groupId那么则从中剔除
        if (wxQueryBo.getCriteria() == null || wxQueryBo.getCriteria().size() == 0){
            criteria = new ArrayList<>();
        } else {
            List<WxQueryCriteriaBo> criteriaTemp = new ArrayList<>();
            criteria.forEach(criteriaItem -> {
                if (!criteriaItem.getFieldName().equals("groupId")){
                    criteriaTemp.add(criteriaItem);
                }
            });
            criteria = criteriaTemp;
        }
        WxQueryCriteriaBo groupCriteria = new WxQueryCriteriaBo();
        groupCriteria.setFieldName("groupId");
        groupCriteria.setValue(groupId);
        criteria.add(groupCriteria);
        wxQueryBo.setCriteria(criteria);
        return ResponseHelper.createInstance(costDetailService.listCostDetail(wxQueryBo));
    }

    /**
     * @param costId
     * @return
     */
    @RequestMapping(value = "/costDetail/costGroup/{groupId}detail/{costId}", method = RequestMethod.DELETE)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<String> deleteCostDetail(@PathVariable(value = "groupId") Integer groupId, @PathVariable(value = "costId") Integer costId){
        costDetailService.deleteCostDetail(groupId, costId);
        return ResponseHelper.createInstance("success");
    }


    @RequestMapping(value = "costDetail/{groupId}/invoice", method = RequestMethod.POST)
    @RolePermission(needPermission = PermissionType.USER)
    public JsonEntity<CostDetailBo> createCostDetailByInvoice(@PathVariable(value = "groupId") Integer groupId, MultipartFile invoice){
        return ResponseHelper.createInstance(costDetailService.getCostDetailByInvoice(groupId, invoice));
    }
}
