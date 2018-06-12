package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 2018-06-12
 */
public class UserOverviewBo extends UserBo {
    private BigDecimal totalCost;

    private BigDecimal leftCost;

    private Integer notificationCount;

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getLeftCost() {
        return leftCost;
    }

    public void setLeftCost(BigDecimal leftCost) {
        this.leftCost = leftCost;
    }

    public Integer getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Integer notificationCount) {
        this.notificationCount = notificationCount;
    }
}
