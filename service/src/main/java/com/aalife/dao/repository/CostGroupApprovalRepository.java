package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroupApproval;
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
public interface CostGroupApprovalRepository extends JpaRepository<CostGroupApproval, Integer> {
    /**
     * 根据group和user查找未处理的Approval
     * @param userId
     * @param groupId
     * @return
     */
    @Query("SELECT cga FROM CostGroupApproval cga WHERE cga.user.userId = :userId AND cga.costGroup.groupId = :groupId and cga.status = 0")
    CostGroupApproval findApprovalByUserAndGroup(@Param(value = "userId") Integer userId, @Param(value = "groupId") Integer groupId);

    /**
     * 根据group id 查询所有的approval
     * @param groupId
     * @return
     */
    @Query("SELECT cga FROM CostGroupApproval cga WHERE cga.costGroup.groupId = :groupId")
    List<CostGroupApproval> findApprovalsByGroup(@Param(value = "groupId") Integer groupId);
}
