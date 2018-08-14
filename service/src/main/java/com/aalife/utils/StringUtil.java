package com.aalife.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mosesc
 * @date 2018-08-14
 */
public class StringUtil {
    private StringUtil(){}
    private static Pattern emojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 移除昵称中的图标
     * @param nickName
     * @return
     */
    public static String filterEmoji(String nickName){
        if (StringUtils.isEmpty(nickName)){
            return nickName;
        }
        Matcher emojiMatcher = emojiPattern.matcher(nickName);
        if (emojiMatcher.find()){
            nickName = emojiMatcher.replaceAll("*");
        }
        return nickName;
    }
}
