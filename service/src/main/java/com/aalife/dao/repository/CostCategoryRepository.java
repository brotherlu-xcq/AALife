package com.aalife.dao.repository;

import com.aalife.dao.entity.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Repository
public interface CostCategoryRepository extends JpaRepository<CostCategory, Integer> {
}
