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
 *
 * @author brother lu
 * @date 2018-06-05
 */
@Entity
@Table(name = "cost_group")
public class CostGroup {
    @GeneratedValue
    @Id
    @Column(name = "id")
    private Integer groupId;

    @Column(name = "group_name")
    @NotNull
    private String groupName;

    @JoinColumn(name = "admin_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User adminUser;

    @Column(name = "entry_id")
    @NotNull
    private Integer entryId;

    @Column(name = "entry_date")
    @NotNull
    private Date entryDate;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
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
        return "CostGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", adminUser=" + adminUser +
                ", entryId=" + entryId +
                ", entryDate=" + entryDate +
                '}';
    }
}
