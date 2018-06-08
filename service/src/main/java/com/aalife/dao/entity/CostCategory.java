package com.aalife.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Entity
@Table(name = "cost_category")
public class CostCategory {
    @GeneratedValue
    @Column(name = "id")
    @Id
    private Integer cateId;

    @Column(name = "cate_name")
    @NotNull
    private String cateName;

    @Column(name = "cate_icon")
    @NotNull
    private String cateIcon;

    @Column(name = "entry_id")
    @NotNull
    private Integer entryId;

    @Column(name = "entry_date")
    @NotNull
    private Date entryDate;

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateIcon() {
        return cateIcon;
    }

    public void setCateIcon(String cateIcon) {
        this.cateIcon = cateIcon;
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
}
