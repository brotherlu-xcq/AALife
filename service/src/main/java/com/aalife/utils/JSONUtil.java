package com.aalife.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    /**
     * this method can be replaced by JSON.parseArray(String, Class<T>)
     * @param contentStr
     * @param clazz
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> List<T> jsonString2List(String contentStr, Class<T> clazz) {
        JSONArray array = JSON.parseArray(contentStr);
        if (array == null || array.size() == 0){
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i=0; i<array.size(); i++){
            T object = array.getObject(i, clazz);
            result.add(object);
        }
        return result;
    }
}
