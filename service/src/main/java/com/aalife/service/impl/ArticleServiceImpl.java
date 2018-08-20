package com.aalife.service.impl;

import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.dao.entity.Article;
import com.aalife.dao.repository.ArticleCommentRepository;
import com.aalife.dao.repository.ArticleRepository;
import com.aalife.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author mosesc
 * @date 2018-08-20
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Override
    public BaseQueryResultBo<ArticleOverviewBo> getArticleOverview(WxQueryBo wxQueryBo) {
        return null;
    }

    class ArticleSpecification implements Specification<Article> {

        @Override
        public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            return null;
        }
    }
}
