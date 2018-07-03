package com.aalife.dao.repository;

import com.aalife.dao.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-07-03
 */
@Repository
public interface UserActionLogRepository extends JpaRepository<UserActionLog, Integer> {
}
