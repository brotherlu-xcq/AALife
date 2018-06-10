package com.aalife.bo;

import java.math.BigDecimal;

/**
 * @author brother lu
 * @date 2018-06-10
 */
public class NewCostDetailBo {
    private Integer groupId;

    private Integer cateId;

    private String costDesc;

    private String costDate;

    private BigDecimal costMoney;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }
}
