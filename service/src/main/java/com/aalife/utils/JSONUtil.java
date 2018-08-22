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

    public static List<Map<String, Object>> jsonString2List(String contentStr) {
        JSONArray array = JSON.parseArray(contentStr);
        if (array == null || array.size() == 0){
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        array.forEach(object ->{
            JSONObject jsonObject = (JSONObject) object;
            Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
            Map<String, Object> data = new HashMap<>(2);
            entrySet.forEach(entry->{
                String key = entry.getKey();
                Object value =entry.getValue();
                data.put(key, value);
            });
            result.add(data);
        });
        return result;
    }
}
