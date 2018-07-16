package com.aalife.dao.repository;

import com.aalife.dao.entity.CostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

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
    @Query(value = "UPDATE cost_detail SET clean_id= :cleanId WHERE group_id = :groupId AND clean_id IS NULL AND delete_id IS NULL", nativeQuery = true)
    void cleanCostDetailByGroup(@Param(value = "groupId") Integer groupId, @Param(value = "cleanId") Integer cleanId);

    /**
     * 根据用户ID和账单查询未结算的总金额
     * @param groupId
     * @param targetUserId
     * @param cleanId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND user_id= :userId AND clean_id = :cleanId AND delete_id is NULL", nativeQuery = true)
    BigDecimal findTotalCostByUserAndGroup(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer targetUserId, @Param(value = "cleanId") Integer cleanId);

    /**
     * 根据用户ID和账单查询未结算的总金额，查询未结算的记录
     * @param groupId
     * @param targetUserId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND user_id= :userId AND clean_id IS NULL AND delete_id is NULL", nativeQuery = true)
    BigDecimal findTotalCostByUserAndGroup(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer targetUserId);

    /**
     * 获取账单为结算的记录
     * @param groupId
     * @param cleanId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND clean_id = :cleanId AND delete_id is NULL", nativeQuery = true)
    BigDecimal findTotalCostByGroup(@Param(value = "groupId")Integer groupId, @Param(value = "cleanId") Integer cleanId);

    /**
     * 获取账单为结算的记录，查询未结算的记录
     * @param groupId
     * @return
     */
    @Query(value = "SELECT SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND clean_id IS NULL AND delete_id is NULL", nativeQuery = true)
    BigDecimal findTotalCostByGroup(@Param(value = "groupId")Integer groupId);

    /**
     * 根据id假删除
     * @param costId
     * @param userId
     */
    @Modifying
    @Query("UPDATE CostDetail cd SET cd.deleteId = :userId, cd.deleteDate = now() WHERE cd.costId = :costId AND cd.deleteId IS NULL")
    void deleteCostDetailById(@Param(value = "costId")Integer costId, @Param(value = "userId")Integer userId);

    /**
     * 查询消费分类的花费情况
     * @param groupId
     * @param cleanId
     * @return
     */
    @Query(value = "SELECT cate_id, SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND clean_id = :cleanId AND delete_id IS NULL GROUP BY cate_id", nativeQuery = true)
    List<Object> findTotalCostForCostCateByGroup(@Param(value = "groupId")Integer groupId, @Param(value = "cleanId")Integer cleanId);

    /**
     * 查询消费分类的花费情况，查询未结算的记录
     * @param groupId
     * @return
     */
    @Query(value = "SELECT cate_id, SUM(cost_money) FROM cost_detail WHERE group_id = :groupId AND clean_id IS NULL AND delete_id IS NULL GROUP BY cate_id", nativeQuery = true)
    List<Object> findTotalCostForCostCateByGroup(@Param(value = "groupId")Integer groupId);

    /**
     * 获取未计算记录的数量
     * @param groupId
     * @return
     */
    @Query(value = "SELECT count(id) FROM cost_detail WHERE group_id = :groupId AND clean_id IS NULL AND delete_id IS NULL", nativeQuery = true)
    int findUnCleanDetailCount(@Param(value = "groupId") Integer groupId);

    /**
     * 查询每日新建的消费记录总数
     * @return
     */
    @Query(value = "SELECT count(id) FROM cost_detail WHERE datediff(entry_date, now()) = 0", nativeQuery = true)
    Integer findCostDetailNewDailyReport();

    /**
     * 查询每日新建的消费记录总数
     * @return
     */
    @Query(value = "SELECT count(id) FROM cost_detail WHERE datediff(entry_date, now()) = 0 WHERE datediff(now(), delete_date) = 0", nativeQuery = true)
    Integer findCostDetailDeleteDailyReport();

    /**
     * 查询每日新建的消费记录总数
     * @return
     */
    @Query(value = "SELECT count(cd.id) FROM cost_detail cd WHERE datediff(cdentry_date, now()) = 0 WHERE ", nativeQuery = true)
    Integer findCostDetailCleanDailyReport();
}
