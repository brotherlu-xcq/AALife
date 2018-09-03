package com.aalife.bo;

/**
 * @author mosesc
 * @date 2018-08-28
 */
public class ArticleCommentBo {
    private Integer commentId;
    private String comment;
    private ExtendUserBo user;
    private String entryDate;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
