package com.aalife.constant;

/**
 * @author mosesc
 * @date 2018-06-06
 */
public enum ApprovalStatus {
    PENDING("未处理"),
    APPROVAL("已接受");

    ApprovalStatus(String statusName){
        this.statusName = statusName;
    }

    private String statusName;

    public String getStatusName() {
        return statusName;
    }
}
