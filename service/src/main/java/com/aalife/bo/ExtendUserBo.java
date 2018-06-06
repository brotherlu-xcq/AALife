package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-06-06
 */
public class ExtendUserBo extends UserBo {
    private Integer userId;
    private String remarkName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    @Override
    public String toString() {
        return "ExtendUserBo{" +
                "userId=" + userId +
                ", remarkName='" + remarkName + '\'' +
                '}';
    }
}
