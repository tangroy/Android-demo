package com.oxygen.www.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextUtils {
	
	/**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0  || "null".equals(str))
            return true;
        else
            return false;
    }
    
    /**
     * 正则-手机号
     * @param mobiles
     * @return
     */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
 
    }  
	
	/**
	　　* 验证邮箱地址是否正确
	　　* @param email
	　　* @return
	　　*/
	public static boolean isEmail(String email) {
		Pattern regex = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}
			
	static void test() {
	
	}
}
