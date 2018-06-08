package com.aalife.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Entity
@Table(name = "cost_detail")
public class CostDetail {
    @GeneratedValue
    @Id
    @Column(name = "id")
    private Integer costId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CostGroup costGroup;

    @JoinColumn(name = "cate_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CostCategory costCategory;

    @Column(name = "cost_money")
    @NotNull
    private BigDecimal costMoney;

    @Column(name = "cost_desc")
    private String costDesc;

    @Column(name = "cost_date")
    @NotNull
    private Date costDate;

    @JoinColumn(name = "clean_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CostClean costClean;

    @Column(name = "entry_id")
    @NotNull
    private Integer entryId;

    @Column(name = "entry_date")
    @NotNull
    private Date entryDate;

    @Column(name = "delete_id")
    private Integer deleteId;

    @Column(name = "delete_date")
    private Date deleteDate;

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CostGroup getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroup costGroup) {
        this.costGroup = costGroup;
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

    public CostClean getCostClean() {
        return costClean;
    }

    public void setCostClean(CostClean costClean) {
        this.costClean = costClean;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public CostCategory getCostCategory() {
        return costCategory;
    }

    public void setCostCategory(CostCategory costCategory) {
        this.costCategory = costCategory;
    }

    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }
}
