package com.aalife.dao.repository;

import com.aalife.dao.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author brother lu
 * @date 2018-06-05
 */
@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Integer> {
    /**
     * 获取当日用户登录
     */
    @Query(value = "SELECT count(id) FROM user_login WHERE datediff(entry_date, now()) = 0", nativeQuery = true)
    Integer findUserLoginDailyReport();
}
