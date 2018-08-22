package com.aalife.dao.repository;

import com.aalife.dao.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-08-20
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {
    @Override
    @Query(value = "FROM Article a WHERE a.articleId = :articleId AND a.active = 'Y' AND a.costGroup IS NULL AND a.deleteId IS NULL")
    Article findOne(@Param(value = "articleId")Integer articleId);
}
