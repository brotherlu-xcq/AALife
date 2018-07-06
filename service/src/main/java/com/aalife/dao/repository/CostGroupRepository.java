package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author brother lu
 * @date 2018-06-05
 */
@Repository
public interface CostGroupRepository extends JpaRepository<CostGroup, Integer> {
    /**
     * 根据group code查找group
     * @param groupCode
     * @return
     */
    @Query("SELECT cg FROM CostGroup cg where cg.groupCode = :groupCode AND cg.deleteId IS NULL")
    CostGroup findGroupByGroupCode(@Param(value = "groupCode") String groupCode);

    /**
     * 根据id查询对应得group
     * @param groupId
     * @return
     */
    @Query("SELECT cg FROM CostGroup cg where cg.groupId = :groupId AND cg.deleteId IS NULL")
    CostGroup findGroupById(@Param(value = "groupId") Integer groupId);

    /**
     * 根据id假删除账单
     * @param groupId
     * @param userId
     */
    @Modifying
    @Query("UPDATE CostGroup cg SET cg.deleteId = :userId, cg.deleteDate = now() WHERE cg.groupId = :groupId")
    void deleteCostGroup(@Param(value = "groupId") Integer groupId, @Param(value = "userId") Integer userId);
}
