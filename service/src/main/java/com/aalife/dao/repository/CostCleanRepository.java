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
    @Query("SELECT cl FROM CostClean cl WHERE cl.costGroup.groupId = :groupId ORDER BY cl.cleanId DESC ")
    List<CostClean> findCostCleansByGroupId(@Param(value = "groupId")Integer groupId);

    /**
     * 查询每日新建的消费记录总数
     * @return
     */
    @Query(value = "SELECT count(id) FROM cost_clean WHERE datediff(now(), entry_date) = 0", nativeQuery = true)
    Integer findCostCleanDailyReport();
}
