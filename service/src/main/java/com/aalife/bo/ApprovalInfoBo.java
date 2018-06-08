package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-06-06
 */
public class ApprovalInfoBo {
    private Integer approvalId;
    private String comment;
    private ExtendUserBo user;
    private CostGroupBo costGroup;
    private String status;

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ExtendUserBo getUser() {
        return user;
    }

    public void setUser(ExtendUserBo user) {
        this.user = user;
    }

    public CostGroupBo getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroupBo costGroup) {
        this.costGroup = costGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
