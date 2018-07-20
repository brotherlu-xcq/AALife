package com.aalife.utils;

import java.util.Date;
import java.util.Random;
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
        char[] letters = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z'};
        StringBuffer tail = new StringBuffer();
        for (int i=0; i< 3; i++){
            int randomId = new Random().nextInt(letters.length);
            tail.append(letters[randomId]);
        }
        StringBuffer date = new StringBuffer(FormatUtil.formatDate2String(new Date(), "yyMMddHHmmsss"));
        return date.append(tail).toString();
    }
}
