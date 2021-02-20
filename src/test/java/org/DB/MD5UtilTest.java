package org.DB;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/2 13:12
 */
public class MD5UtilTest {

    @Test
    public void md5test(){

        String  ss="123456";
        String  MD5String="";
        System.out.println(MD5Util.getMD5(ss));


    }



}