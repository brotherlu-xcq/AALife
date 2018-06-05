package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author brother lu
 * @date 2018-06-05
 */
@Repository
public interface CostGroupReposity extends JpaRepository<CostGroup, Integer> {
}
