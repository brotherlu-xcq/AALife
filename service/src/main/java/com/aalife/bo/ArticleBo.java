package com.aalife.bo;

import java.util.List;
import java.util.Map;

/**
 * @author mosesc
 * @date 2018-08-22
 */
public class ArticleBo {
    private String title;
    private List<ArticleContentBo> content;
    private Integer groupId;
    private Integer typeId;
    private String webUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ArticleContentBo> getContent() {
        return content;
    }

    public void setContent(List<ArticleContentBo> content) {
        this.content = content;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
