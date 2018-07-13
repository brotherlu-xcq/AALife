package com.aalife.dao.repository;

import com.aalife.dao.entity.UserWxForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mosesc
 * @date 2018-07-13
 */
@Repository
public interface UserWxFormRepository extends JpaRepository<UserWxForm, Integer> {
    /**
     * 查询未被删除的可用的formId
     * @param userId
     * @return
     */
    @Query(value = "SELECT uxf FROM UserWxForm uxf WHERE uxf.user.userId = :userId AND uxf.deleteId IS NULL")
    List<UserWxForm> findUserWxFormByUserId(@Param(value = "userId")Integer userId);
}
