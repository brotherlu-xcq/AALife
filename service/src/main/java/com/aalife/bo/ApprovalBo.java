package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-06-06
 */
public class ApprovalBo {
    private String groupCode;
    private String comment;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ApprovalBo{" +
                "groupCode='" + groupCode + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
