package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-08-20
 */
public class ArticleOverviewBo {
    private Integer articleId;
    private CostGroupBo costGroup;
    private ExtendUserBo user;
    private String title;
    private ArticleTypeBo articleType;
    private Character top;
    private Character active;
    private Integer viewCount;
    private String entryDate;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public CostGroupBo getCostGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroupBo costGroup) {
        this.costGroup = costGroup;
    }

    public ExtendUserBo getUser() {
        return user;
    }

    public void setUser(ExtendUserBo user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleTypeBo getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleTypeBo articleType) {
        this.articleType = articleType;
    }

    public Character getTop() {
        return top;
    }

    public void setTop(Character top) {
        this.top = top;
    }

    public Character getActive() {
        return active;
    }

    public void setActive(Character active) {
        this.active = active;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
