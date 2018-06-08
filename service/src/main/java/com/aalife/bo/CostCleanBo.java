package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public class CostCleanBo {
    private Integer cleanId;

    private String comment;

    private UserBo user;

    private String cleanDate;

    public Integer getCleanId() {
        return cleanId;
    }

    public void setCleanId(Integer cleanId) {
        this.cleanId = cleanId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserBo getUser() {
        return user;
    }

    public void setUser(UserBo user) {
        this.user = user;
    }

    public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }
}
