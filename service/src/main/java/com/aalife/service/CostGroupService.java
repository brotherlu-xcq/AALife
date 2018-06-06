package com.aalife.service;

import com.aalife.bo.CostGroupBo;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.User;

/**
 * @author brother lu
 * @date 2018-06-05
 */
public interface CostGroupService {
    /**
     * 创建一个新的分组
     * @param groupName
     * @return
     */
    Integer createNewCostGroup(String groupName);

    /**
     * 更新账单
     * @param costGroupBo
     */
    void updateCostGroup(CostGroupBo costGroupBo);

    /**
     * 根据code去寻找对应得group
     * @param code
     * @return
     */
    CostGroupBo findCostGroupByCode(String code);
}
