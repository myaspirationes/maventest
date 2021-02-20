package org.DB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/20 16:14
 */
public class ValidationUtils {



    /**
     * 验证手机号是否正确
     * @param mobile
     * @return true正确；false错误
     */
    public static boolean isMobileNum(String mobile){
//        Pattern pattern=Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Pattern pattern=Pattern.compile("^((1(3|4|5|6|7|8|9)[0-9]))\\d{8}$");
        Matcher matcher=pattern.matcher((mobile));
        return matcher.matches();
    }

    /**
     *验证身份证号码
     */
    public static boolean isUserIDCard(String idcard){
        Pattern pattern = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$");
        Matcher matcher = pattern.matcher(idcard);
        if (idcard.length() == 18 && matcher.matches()) {
            String[] idSplit = idcard.split("");
            if (("X".equals(idSplit[17]))) {
                idSplit[17] = "x";
            }
            //∑(ai×Wi)(mod 11)
            //加权因子
            int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            //校验位
            String[] parity = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
            int sum = 0;
            int ai = 0;
            int wi = 0;
            for (int i = 0; i < 17; i++)
            {
                ai = Integer.valueOf(idSplit[i]);
                wi = factor[i];
                sum += ai * wi;
            }
            if (!idSplit[17].equals(parity[sum % 11])) {
                return false;
            }
            return true;
        }else{
            return false;
        }
    }




}
