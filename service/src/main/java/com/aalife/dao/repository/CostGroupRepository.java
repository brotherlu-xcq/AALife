package com.aalife.dao.repository;

import com.aalife.dao.entity.CostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
