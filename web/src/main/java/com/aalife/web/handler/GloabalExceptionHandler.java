package com.aalife.web.handler;

import com.aalife.web.util.JsonEntity;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 捕获全局异常
 * @author brother lu
 * @date 2018-06-04
 */
@RestControllerAdvice
public class GloabalExceptionHandler {

    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonEntity handle401(ShiroException e){
        return new JsonEntity(null, 401, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public JsonEntity handle403(){
        return new JsonEntity(null, 403, "无权限");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonEntity handle401(UnauthenticatedException e){
        return new JsonEntity(null, 401, "未登录");
    }

//    @ExceptionHandler()
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public JsonEntity handle404(){
//        return new JsonEntity(null, 404, "你访问的资源不存在");
//    }

    /**
     * 内部错误异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonEntity globalException(Exception e, HttpServletRequest request){
        return new JsonEntity(null, getStatus(request).value(), e.getMessage());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
