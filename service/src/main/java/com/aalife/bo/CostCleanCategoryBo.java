package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 20198-07-10
 */
public class CostCleanCategoryBo extends CostCategoryBo {
    private BigDecimal totalCost;

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
