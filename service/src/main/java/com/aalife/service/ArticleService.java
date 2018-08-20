package com.aalife.service;

import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.WxQueryBo;

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
}
