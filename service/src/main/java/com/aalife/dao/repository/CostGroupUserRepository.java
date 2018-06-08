package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Repository
public interface CostGroupUserRepository extends JpaRepository<CostGroupUser, Integer> {
    /**
     * 根据用户的ID获取其对应得组
     * @param userId
     * @return
     */
    @Query("SELECT cgu FROM CostGroupUser cgu where cgu.user.userId = :userId AND cgu.deleteId IS NULL")
    List<CostGroupUser> findCostGroupUserByUser(@Param(value = "userId")Integer userId);

    /**
     * 根据用户Id和Group Id获取group
     * @param userId
     * @param groupId
     * @return
     */
    @Query("SELECT cgu FROM CostGroupUser cgu WHERE cgu.user.userId = :userId AND cgu.costGroup.groupId = :groupId AND cgu.deleteId IS NULL")
    CostGroupUser findCostGroupByUserAndGroup(@Param(value = "userId") Integer userId, @Param(value = "groupId") Integer groupId);

    /**
     * 根据Costgrou查找所有的用户
     * @param groupId
     * @return
     */
    @Query("SELECT cgu FROM CostGroupUser cgu WHERE cgu.costGroup.groupId = :groupId AND cgu.deleteId IS NULL")
    List<CostGroupUser> findCostGroupByGroup(@Param(value = "groupId") Integer groupId);

    /**
     * 删除成员
     * @param userId
     * @param groupId
     * @param deleteId
     */
    @Modifying
    @Query("UPDATE CostGroupUser cgu SET cgu.deleteId = :deleteId, cgu.deleteDate = now() WHERE cgu.costGroup.groupId = :groupId and cgu.user.userId = :userId")
    void deleteCostGroupUser(@Param(value = "userId") Integer userId, @Param(value = "groupId") Integer groupId, @Param(value = "deleteId") Integer deleteId);

    /**
     * 分配admin角色
     * @param groupId
     * @param userId
     */
    @Query("UPDATE CostGroupUser cgu SET cgu.admin = 'Y' WHERE cgu.costGroup.groupId = :groupId AND cgu.user.userId = :userId")
    void assignCostGroupAdmin(Integer groupId, Integer userId);

    /**
     * 删除所有账单的用户
     * @param groupId
     * @param userId
     */
    @Modifying
    @Query("UPDATE CostGroupUser cgu SET cgu.deleteId = :userId, cgu.deleteDate = now() WHERE cgu.costGroup.groupId = :groupId AND cgu.deleteId IS NULL")
    void deleteCostGroupUserByGroupId(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer userId);
}
