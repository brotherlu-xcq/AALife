package com.aalife.dao.repository;

import com.aalife.dao.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author mosesc
 * @date 2018-07-03
 */
@Repository
public interface UserActionLogRepository extends JpaRepository<UserActionLog, Integer> {

    /**
     * 查询今天请求API数量
     * @return
     */
    @Query(value = "SELECT count(id) FROM user_action_log WHERE datediff(entry_date, now()) = 0", nativeQuery = true)
    Integer findUserActionLogDailyReport();
}
