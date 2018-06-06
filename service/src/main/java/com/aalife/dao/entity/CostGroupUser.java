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
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Entity
@Table(name = "cost_group_user")
public class CostGroupUser {
    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CostGroup costGroup;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @Column(name = "admin")
    @NotNull
    private Character admin;

    @Column(name = "entry_id")
    @NotNull
    private Integer entryId;

    @Column(name = "entry_date")
    @NotNull
    private Date entryDate;

    @Column(name = "deleteId")
    private Integer deleteId;

    @Column(name = "delete_date")
    private Date deleteDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CostGroup getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroup costGroup) {
        this.costGroup = costGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Character getAdmin() {
        return admin;
    }

    public void setAdmin(Character admin) {
        this.admin = admin;
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

    @Override
    public String toString() {
        return "CostGroupUser{" +
                "costGroup=" + costGroup +
                ", user=" + user +
                ", admin=" + admin +
                ", entryId=" + entryId +
                ", entryDate=" + entryDate +
                ", deleteId=" + deleteId +
                ", deleteDate=" + deleteDate +
                '}';
    }
}
