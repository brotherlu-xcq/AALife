package com.aalife.web.publicapi;

import com.aalife.bo.CostGroupBo;
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

    /**
     * 根据Code去查找对应得group
     * @return
     */
    @RequestMapping(value = "/costGroup/{groupCode}", method = RequestMethod.GET)
    public JsonEntity<CostGroupBo> findCostGroupByCode(@PathVariable("groupCode") String groupCode){
        CostGroupBo costGroupBo = costGroupService.findCostGroupByCode(groupCode);
        return ResponseHelper.createInstance(costGroupBo);
    }
}
