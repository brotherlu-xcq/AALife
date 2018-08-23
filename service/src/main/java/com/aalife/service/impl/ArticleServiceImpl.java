package com.aalife.service.impl;

import com.aalife.bo.ArticleBo;
import com.aalife.bo.ArticleContentBo;
import com.aalife.bo.ArticleDetailBo;
import com.aalife.bo.ArticleOverviewBo;
import com.aalife.bo.ArticleTypeBo;
import com.aalife.bo.BaseQueryBo;
import com.aalife.bo.BaseQueryResultBo;
import com.aalife.bo.CostGroupBo;
import com.aalife.bo.WxQueryBo;
import com.aalife.bo.WxQueryCriteriaBo;
import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.Article;
import com.aalife.dao.entity.CostGroup;
import com.aalife.dao.entity.User;
import com.aalife.dao.repository.ArticleCommentRepository;
import com.aalife.dao.repository.ArticleRepository;
import com.aalife.dao.repository.CostGroupRepository;
import com.aalife.exception.BizException;
import com.aalife.service.ArticleService;
import com.aalife.service.WebContext;
import com.aalife.utils.FormatUtil;
import com.aalife.utils.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CostGroupRepository costGroupRepository;
    @Autowired
    private WebContext webContext;

    @Override
    public BaseQueryResultBo<ArticleOverviewBo> getArticleOverview(WxQueryBo wxQueryBo) {
        ArticleSpecification specification = new ArticleSpecification(wxQueryBo);
        PageRequest page = new PageRequest(wxQueryBo.getPage()-1, wxQueryBo.getSize());
        Page<Article> articlePage = articleRepository.findAll(specification, page);
        List<Article> articles = articlePage.getContent();
        return null;
    }

    @Override
    public ArticleDetailBo getArticleById(Integer articleId) {
        Article article = articleRepository.findOne(articleId);
        if (article == null){
            throw new BizException("未查询到对应得文章");
        }
        ArticleDetailBo articleDetailBo = new ArticleDetailBo();
        articleDetailBo.setArticleId(articleId);
        ArticleTypeBo articleTypeBo = new ArticleTypeBo(article.getTypeId(), "SYSTEM");
        articleDetailBo.setArticleType(articleTypeBo);
        articleDetailBo.setTitle(article.getTitle());
        articleDetailBo.setActive(article.getActive());
        articleDetailBo.setTop(article.getTop());
        String contentStr = article.getContent();
        List<ArticleContentBo> content = JSON.parseArray(contentStr, ArticleContentBo.class);
        articleDetailBo.setContent(content);
        articleDetailBo.setViewCount(article.getViewCount());
        articleDetailBo.setEntryDate(FormatUtil.formatDate2SpecialString(article.getEntryDate()));
        return articleDetailBo;
    }

    @Override
    public Map<String, Object> createNewArticle(ArticleBo articleBo) {
        Integer groupId = articleBo.getGroupId();
        CostGroup costGroup = costGroupRepository.findGroupById(groupId);
        User user = webContext.getCurrentUser();
        Article article =  new Article();
        article.setViewCount(0);
        article.setTop('N');
        article.setActive('Y');
        article.setTitle(articleBo.getTitle());
        article.setWebUrl(articleBo.getWebUrl());
        article.setCostGroup(costGroup);
        article.setUser(user);
        article.setEntryId(user.getUserId());
        article.setEntryDate(new Date());
        article.setTypeId(articleBo.getTypeId());
        article.setContent(JSONUtil.object2JsonString(articleBo.getContent()));
        articleRepository.save(article);

        Map<String, Object> result = new HashMap<>(1);
        result.put("articleId", article.getArticleId());
        return result;
    }

    class ArticleSpecification implements Specification<Article> {
        private BaseQueryBo baseQueryBo;

        public ArticleSpecification(BaseQueryBo baseQueryBo) {
            this.baseQueryBo = baseQueryBo;
        }

        @Override
        public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            WxQueryBo wxQueryBo = (WxQueryBo) baseQueryBo;
            List<WxQueryCriteriaBo> criterias = wxQueryBo.getCriteria();
            return null;
        }
    }
}
