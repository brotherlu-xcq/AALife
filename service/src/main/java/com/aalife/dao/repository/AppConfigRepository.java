package com.aalife.dao.repository;

import com.aalife.dao.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author mosesc
 * @date 2018-06-11
 */
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    /**
     * 根据name查询对应得值
     * @param appName
     * @param configName
     * @return
     */
    @Query("SELECT ac FROM AppConfig ac WHERE ac.appName = :appName AND ac.configName = :configName")
    AppConfig findAppConfigByName(@Param(value = "appName") String appName, @Param(value = "configName") String configName);
}
