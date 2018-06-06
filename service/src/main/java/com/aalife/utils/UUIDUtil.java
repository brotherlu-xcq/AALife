package com.aalife.utils;

import java.util.UUID;

/**
 * @author brother lu
 * @date 2018-06-05
 */
public class UUIDUtil {
    private UUIDUtil(){}

    /**
     * 生成16位UUID
     * @return
     */
    public static String get16BitUUID(){
        int matchId = 1;
        int hashCode = UUID.randomUUID().toString().hashCode();
        hashCode = hashCode < 0 ? -hashCode : hashCode;
        return matchId + String.format("%015d", hashCode);
    }
}
