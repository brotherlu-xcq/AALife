package com.aalife.framework.annotation;

import com.aalife.framework.constant.PermissionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户校验用户在该账单中是否有对应权限，使用该注解的的方法的第一参数必须为账单编号
 * @author mosesc
 * @date 2018-06-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RolePermission {
    PermissionType needPermission();
}
