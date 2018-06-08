package com.aalife.bo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public class CostGroupOverviewBo {
    private CostGroupBo costGroup;
    private List<CostGroupUserBo> costUsers;
    /**
     * 当前用户的角色，用户展示菜单
     */
    private String myRole;

    public CostGroupBo getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroupBo costGroup) {
        this.costGroup = costGroup;
    }

    public List<CostGroupUserBo> getCostUsers() {
        return costUsers;
    }

    public void setCostUsers(List<CostGroupUserBo> costUsers) {
        this.costUsers = costUsers;
    }

    public String getMyRole() {
        return myRole;
    }

    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }
}
