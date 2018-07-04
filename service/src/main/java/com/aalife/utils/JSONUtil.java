package com.aalife.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author mosesc
 * @date 2018-06-03
 */
public class JSONUtil {
    private JSONUtil(){}

    public static String object2JsonString(Object object){
        if (object == null){
            return null;
        }
        return JSONObject.toJSONString(object);
    }
}
