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
 * @date 2018-06-07
 */
@Entity
@Table(name = "cost_user_remark")
public class CostUserRemark {
    @GeneratedValue
    @Id
    @Column
    private Integer id;

    @JoinColumn(name = "source_no")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User sourceUser;

    @JoinColumn(name = "target_no")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User targetUser;

    @Column(name = "remark_name")
    @NotNull
    private String remarkName;

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

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
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
