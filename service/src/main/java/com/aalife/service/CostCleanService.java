package com.aalife.service;

import com.aalife.bo.CostCleanSummaryBo;
import com.aalife.bo.ExtendCostCleanBo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-10
 */
public interface CostCleanService {
    /**
     * 列出该分组下的结算记录
     * @param groupId
     * @return
     */
    List<ExtendCostCleanBo> listCostCleans(Integer groupId);

    /**
     * 根据结算Id和账单Id查询对应得
     * @param groupId
     * @param cleanId
     * @return
     */
    CostCleanSummaryBo findCostCleanSummaryById(Integer groupId, Integer cleanId);
}
