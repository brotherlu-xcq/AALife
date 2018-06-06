package com.aalife.bo;

/**
 * @auther brother lu
 * @date 2018-06-05
 */
public class CostGroupBo {
    private Integer groupNo;
    private String groupCode;
    private String groupName;

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public String toString() {
        return "CostGroupBo{" +
                "groupNo=" + groupNo +
                ", groupCode='" + groupCode + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
