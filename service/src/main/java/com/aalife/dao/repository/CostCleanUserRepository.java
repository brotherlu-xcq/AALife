package com.aalife.dao.repository;

import com.aalife.dao.entity.CostCleanUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-12
 */
@Repository
public interface CostCleanUserRepository extends JpaRepository<CostCleanUser, Integer> {
    /**
     * 根据cleanId获取当时结算时的用户
     * @param cleanId
     * @return
     */
    @Query(value = "SELECT ccu FROM CostCleanUser ccu WHERE ccu.costClean.cleanId = :cleanId")
    List<CostCleanUser> findCostCleanUsersByCleanId(@Param(value = "cleanId") Integer cleanId);
}
