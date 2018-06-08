package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public class CostGroupUserBo extends ExtendUserBo {
    private Character admin;
    private BigDecimal totalCost;
    private BigDecimal averageCost;
    private BigDecimal leftCost;

    public Character getAdmin() {
        return admin;
    }

    public void setAdmin(Character admin) {
        this.admin = admin;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public BigDecimal getLeftCost() {
        return leftCost;
    }

    public void setLeftCost(BigDecimal leftCost) {
        this.leftCost = leftCost;
    }
}
