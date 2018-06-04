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
    @Query("SELECT u from User u where u.wxOpenId = :loginKey")
    User findUserWithOpenId(@Param(value = "loginKey")String openId);
}
