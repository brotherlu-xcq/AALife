package com.aalife.framework.annotation;

import com.aalife.framework.constant.PermissionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mosesc
 * @date 2018-06-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RolePermission {
    PermissionType needPermission();
}
