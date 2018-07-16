package com.aalife.dao.repository;

import com.aalife.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author brother lu
 * @date 2018-06-04
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据用户的open id获取用户信息
     * @param openId
     * @return
     */
    @Query("SELECT u from User u where u.wxOpenId = :openId")
    User findUserWithOpenId(@Param(value = "openId")String openId);

    /**
     * 查询今天创建的用户数
     * @return
     */
    @Query(value = "SELECT count(id) FROM user WHERE datediff(entry_date , now()) = 0", nativeQuery = true)
    Integer findDailyUserReport();
}
