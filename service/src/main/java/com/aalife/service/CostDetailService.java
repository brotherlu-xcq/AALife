package com.aalife.service;

import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.CostDetailBo;
import com.aalife.bo.NewCostDetailBo;
import com.aalife.bo.WxQueryBo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public interface CostDetailService {
    /**
     * 创建新的消费记录
     * @param costDetailBo
     */
    void createNewOrUpdateCostDetail(NewCostDetailBo costDetailBo);

    /**
     * 结算消费
     * @param groupId
     * @param comment
     */
    Integer cleanCostDetail(Integer groupId, String comment);

    /**
     * 查询账单中为结算的记录，分页查询
     * @param wxQueryBo
     * @return
     */
    BaseQueryResultBo<CostDetailBo> listCostDetail(WxQueryBo wxQueryBo);

    /**
     * 删除消费记录
     * @param groupId
     * @param costId
     */
    void deleteCostDetail(Integer groupId, Integer costId);

    /**
     * 用户录音获取消费信息
     * @param groupId
     * @param invoice
     * @return
     */
    CostDetailBo getCostDetailByInvoice(Integer groupId, MultipartFile invoice);
}
