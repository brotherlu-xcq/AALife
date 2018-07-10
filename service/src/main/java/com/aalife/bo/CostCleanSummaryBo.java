package com.aalife.bo;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-10
 */
public class CostCleanSummaryBo {
    private CostGroupBo costGroupBo;
    private List<CostCleanCategoryBo> categorySummary;
    private List<CostGroupUserBo> userSummary;

    public CostGroupBo getCostGroupBo() {
        return costGroupBo;
    }

    public void setCostGroupBo(CostGroupBo costGroupBo) {
        this.costGroupBo = costGroupBo;
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
