package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public class CostDetailBo {
    private UserBo user;

    private BigDecimal costMoney;

    private String costDesc;

    private CostGroupBo costGroup;

    private CostCategoryBo costCategory;

    private String costDate;

    private CostCleanBo costClean;

    private Integer costId;

    public UserBo getUser() {
        return user;
    }

    public void setUser(UserBo user) {
        this.user = user;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }

    public CostGroupBo getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroupBo costGroup) {
        this.costGroup = costGroup;
    }

    public CostCategoryBo getCostCategory() {
        return costCategory;
    }

    public void setCostCategory(CostCategoryBo costCategory) {
        this.costCategory = costCategory;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public CostCleanBo getCostClean() {
        return costClean;
    }

    public void setCostClean(CostCleanBo costClean) {
        this.costClean = costClean;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }
}
