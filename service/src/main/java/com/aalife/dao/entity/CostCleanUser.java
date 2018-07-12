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

/**
 * @auther mosesc
 * @date 2018-07-12
 */
@Entity
@Table(name = "costCleanUser")
public class CostCleanUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "clean_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CostClean costClean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CostClean getCostClean() {
        return costClean;
    }

    public void setCostClean(CostClean costClean) {
        this.costClean = costClean;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
