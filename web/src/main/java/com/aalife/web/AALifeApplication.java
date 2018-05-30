package com.aalife.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author brotherlu
 * @date 2018-05-30
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.aalife"})
public class AALifeApplication {
    public static void main(String[] args){
        SpringApplication.run(AALifeApplication.class, args);
    }
}
