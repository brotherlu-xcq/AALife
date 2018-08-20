package com.aalife.dao.repository;

import com.aalife.dao.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-08-20
 */
@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {
}
