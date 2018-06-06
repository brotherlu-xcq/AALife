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
@Table(name = "cost_group_approval")
public class CostGroupApproval {
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

    @Column(name = "comment")
    @NotNull
    private String comment;

    @Column(name = "status")
    @NotNull
    private Integer status;

    @JoinColumn(name = "approval_id")
    @ManyToOne
    private User approvalUser;

    @Column(name = "approval_date")
    private Date approvalDate;

    @Column(name = "entry_id")
    @NotNull
    private Integer entryId;

    @Column(name = "entry_date")
    @NotNull
    private Date entryDate;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(User approvalUser) {
        this.approvalUser = approvalUser;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
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

    @Override
    public String toString() {
        return "CostGroupApproval{" +
                "id=" + id +
                ", costGroup=" + costGroup +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", approvalUser=" + approvalUser +
                ", approvalDate=" + approvalDate +
                ", entryId=" + entryId +
                ", entryDate=" + entryDate +
                '}';
    }
}
