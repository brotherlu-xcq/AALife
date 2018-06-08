package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroupApproval;
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
public interface CostGroupApprovalRepository extends JpaRepository<CostGroupApproval, Integer> {
    /**
     * 根据group和user查找未处理的Approval
     * @param userId
     * @param groupId
     * @return
     */
    @Query("SELECT cga FROM CostGroupApproval cga WHERE cga.user.userId = :userId AND cga.costGroup.groupId = :groupId and cga.status = 0 AND cga.costGroup.deleteId IS NULL")
    CostGroupApproval findApprovalByUserAndGroup(@Param(value = "userId") Integer userId, @Param(value = "groupId") Integer groupId);

    /**
     * 根据group id 查询所有的approval
     * @param groupId
     * @return
     */
    @Query("SELECT cga FROM CostGroupApproval cga WHERE cga.costGroup.groupId = :groupId AND cga.costGroup.deleteId IS NULL")
    List<CostGroupApproval> findApprovalsByGroup(@Param(value = "groupId") Integer groupId);

    /**
     * 同意通过用户
     * @param groupId
     * @param userId
     */
    @Modifying
    @Query("UPDATE CostGroupApproval cga SET cga.status = 1 WHERE cga.costGroup.groupId = :groupId AND cga.user.userId = :userId")
    void approveUserRequest(@Param(value = "groupId")Integer groupId, @Param(value = "userId")Integer userId);
}
