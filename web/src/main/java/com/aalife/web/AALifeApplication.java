package com.aalife.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author brotherlu
 * @date 2018-05-30
 */
@SpringBootApplication
@EnableSwagger2
@EntityScan(basePackages = "com.aalife.dao.entity")
@ComponentScan(basePackages = "com.aalife")
@EnableJpaRepositories(basePackages = "com.aalife.dao.repository")
@EnableTransactionManagement
public class AALifeApplication {
    public static void main(String[] args){
        SpringApplication.run(AALifeApplication.class, args);
    }
}
