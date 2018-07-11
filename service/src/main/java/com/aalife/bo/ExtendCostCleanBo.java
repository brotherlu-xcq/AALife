package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 2018-07-10
 */
public class ExtendCostCleanBo extends CostCleanBo {
    private CostGroupBo costGroupBo;
    private BigDecimal totalCost;

    public CostGroupBo getCostGroupBo() {
        return costGroupBo;
    }

    public void setCostGroupBo(CostGroupBo costGroupBo) {
        this.costGroupBo = costGroupBo;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
