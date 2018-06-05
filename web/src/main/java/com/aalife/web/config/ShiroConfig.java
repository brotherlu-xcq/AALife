package com.aalife.web.config;

import com.aalife.service.realm.AALifeShiroRealm;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 *
 * @auther brother lu
 * @date 2018-05-31
 */

@Configuration
public class ShiroConfig {
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("aaLifeShiroRealm") AALifeShiroRealm aaLifeShiroRealm){
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(aaLifeShiroRealm);
        return webSecurityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "aaLifeShiroRealm")
    public AALifeShiroRealm aaLifeShiroRealm(@Qualifier("allowAllCredentialsMatcher") AllowAllCredentialsMatcher credentialsMatcher){
        AALifeShiroRealm aaLifeShiroRealm = new AALifeShiroRealm();
        aaLifeShiroRealm.setCredentialsMatcher(credentialsMatcher);
        return aaLifeShiroRealm;
    }

    @Bean(name = "allowAllCredentialsMatcher")
    public AllowAllCredentialsMatcher allowAllCredentialsMatcher() {
        AllowAllCredentialsMatcher credentialsMatcher = new AllowAllCredentialsMatcher();

        return credentialsMatcher;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @DependsOn("lifecycleBeanPostProcessor")
    @Bean
    public DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor sourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }
}
