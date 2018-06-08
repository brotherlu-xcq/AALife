package com.aalife.dao.repository;

import com.aalife.dao.entity.CostUserRemark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-06-07
 */
@Repository
public interface CostUserRemarkRepository extends JpaRepository<CostUserRemark, Integer> {
    /**
     * 根据用户和标记用户查询备注名
     * @param sourceNo
     * @param targetNo
     * @return
     */
    @Query("SELECT cur FROM CostUserRemark cur WHERE cur.sourceUser.userId = :sourceNo AND cur.targetUser.userId = :targetNo")
    CostUserRemark findRemarkBySourceAndTarget(@Param(value = "sourceNo") Integer sourceNo, @Param(value = "targetNo") Integer targetNo);
}
