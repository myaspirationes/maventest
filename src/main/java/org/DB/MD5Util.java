package org.DB;

import org.springframework.util.DigestUtils;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/2 13:11
 */
public class MD5Util {



        //盐，用于混交md5
        private static final String slat = "";
        /**
         * 生成md5
         * @param
         * @return
         */
        public static String getMD5(String str) {
            //String base = str +"/"+slat;//加盐
            String base = str ;

            String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
            return md5;
        }

    }



