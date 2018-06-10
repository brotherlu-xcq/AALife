package com.aalife.dao.repository;

import com.aalife.dao.entity.CostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Repository
public interface CostDetailRepository extends JpaRepository<CostDetail, Integer> {
    /**
     * 根据groupid清算
     * @param groupId
     * @param cleanId
     */
    @Modifying
    @Query("UPDATE CostDetail cd SET cd.costClean.cleanId = :cleanId WHERE cd.costGroup.groupId = :groupId AND cd.deleteId IS NULL")
    void cleanCostDetailByGroup(Integer groupId, Integer cleanId);

    /**
     * 根据用户ID和账单查询未结算的总金额
     * @param groupId
     * @param targetUserId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND user_id= :userId AND clean_id IS NULL AND delete_id is NULL", nativeQuery = true)
    BigDecimal findUnCleanTotalCostByUserAndGroup(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer targetUserId);
}
