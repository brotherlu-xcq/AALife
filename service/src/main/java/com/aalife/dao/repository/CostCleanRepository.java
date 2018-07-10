package com.aalife.dao.repository;

import com.aalife.dao.entity.CostClean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Repository
public interface CostCleanRepository extends JpaRepository<CostClean, Integer> {
    /**
     * 获取group下的所有结算记录
     * @param groupId
     * @return
     */
    @Query("SELECT cl FROM CostClean cl WHERE cl.costGroup.groupId = :groupId")
    List<CostClean> findCostCleansByGroupId(@Param(value = "groupId")Integer groupId);
}
