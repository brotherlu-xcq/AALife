package com.aalife.bo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-10
 */
public class CostCleanSummaryBo {
    private CostGroupBo costGroup;
    private CostCleanBo costClean;
    private List<CostCleanCategoryBo> categorySummary;
    private List<CostGroupUserBo> userSummary;

    public CostGroupBo getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroupBo costGroup) {
        this.costGroup = costGroup;
    }

    public CostCleanBo getCostClean() {
        return costClean;
    }

    public void setCostClean(CostCleanBo costClean) {
        this.costClean = costClean;
    }

    public List<CostCleanCategoryBo> getCategorySummary() {
        return categorySummary;
    }

    public void setCategorySummary(List<CostCleanCategoryBo> categorySummary) {
        this.categorySummary = categorySummary;
    }

    public List<CostGroupUserBo> getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(List<CostGroupUserBo> userSummary) {
        this.userSummary = userSummary;
    }
}
