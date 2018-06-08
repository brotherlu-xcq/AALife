package com.aalife.service;

import com.aalife.bo.NewCostDetailBo;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public interface CostDetailService {
    /**
     * 创建新的消费记录
     * @param costDetailBo
     */
    void createNewCostDetail(NewCostDetailBo costDetailBo);

    /**
     * 结算消费
     * @param groupId
     * @param comment
     */
    void cleanCostDetail(Integer groupId, String comment);
}
