package com.aalife.service;

import com.aalife.bo.ArticleBo;
import com.aalife.bo.ArticleDetailBo;
import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.WxQueryBo;

import java.util.Map;

/**
 * @author mosesc
 * @date 2018-08-20
 */
public interface ArticleService {
    /**
     * 分页获取文章
     * @param wxQueryBo
     * @return
     */
    BaseQueryResultBo<ArticleOverviewBo> getArticleOverview(WxQueryBo wxQueryBo);

    /**
     * 根据Id查询文章
     * @param articleId
     * @return
     */
    ArticleDetailBo getArticleById(Integer articleId);

    /**
     * 创建新的文章
     * @param articleBo
     * @return
     */
    Map<String, Object> createNewArticle(ArticleBo articleBo);
}
