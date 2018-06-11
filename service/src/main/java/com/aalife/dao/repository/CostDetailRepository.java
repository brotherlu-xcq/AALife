package com.aalife.dao.repository;

import com.aalife.dao.entity.CostClean;
import com.aalife.dao.entity.CostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-08
 */
@Repository
public interface CostDetailRepository extends JpaRepository<CostDetail, Integer>, JpaSpecificationExecutor<CostDetail> {
    /**
     * 根据groupid清算
     * @param groupId
     * @param cleanId
     */
    @Modifying
    @Query("UPDATE CostDetail cd SET cd.costClean.cleanId = :cleanId WHERE cd.costGroup.groupId = :groupId AND cd.deleteId IS NULL")
    void cleanCostDetailByGroup(@Param(value = "groupId") Integer groupId, @Param(value = "cleanId") Integer cleanId);

    /**
     * 根据用户ID和账单查询未结算的总金额
     * @param groupId
     * @param targetUserId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND user_id= :userId AND clean_id IS NULL AND delete_id is NULL", nativeQuery = true)
    BigDecimal findUnCleanTotalCostByUserAndGroup(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer targetUserId);

    /**
     * 获取账单为结算的记录
     * @param groupId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND clean_id IS NULL AND delete_id is NULL", nativeQuery = true)
    BigDecimal findUnCleanTotalCostByGroup(@Param(value = "groupId")Integer groupId);

    /**
     * 根据id假删除
     * @param costId
     * @param userId
     */
    @Modifying
    @Query("UPDATE CostDetail cd SET cd.deleteId = :userId, cd.deleteDate = now() WHERE cd.costId = :costId AND cd.deleteId IS NULL")
    void deleteCostDetailById(@Param(value = "costId")Integer costId, @Param(value = "userId")Integer userId);
}
