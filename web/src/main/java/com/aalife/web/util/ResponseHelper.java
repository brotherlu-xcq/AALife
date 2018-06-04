package com.aalife.web.util;

/**
 * @author brother lu
 * @date 2018-06-04
 */
public class ResponseHelper {
    public static <T> JsonEntity<T> createInstance(T object){
        return new JsonEntity(object);
    }
}
