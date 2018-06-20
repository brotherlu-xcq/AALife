package com.aalife.web.handler;

import com.aalife.web.util.JsonEntity;
import org.apache.log4j.Logger;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 捕获全局异常
 * @author brother lu
 * @date 2018-06-04
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonEntity handle401(ShiroException e){
        logger.error("Shiro 异常", e);
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
        logger.error(e);
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
        logger.error("内部错误", e);
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
