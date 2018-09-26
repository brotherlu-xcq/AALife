package com.aalife.dao.repository;

import com.aalife.dao.entity.CostDetailUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-09-03
 */
@Repository
public interface CostDetailUserRepository extends JpaRepository<CostDetailUser, Integer> {
}
