package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
