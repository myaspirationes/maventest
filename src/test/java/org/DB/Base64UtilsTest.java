package org.DB;

import org.junit.Test;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/6/1 18:25
 */
public class Base64UtilsTest {


    @Test
    public void decodeTest(){

        byte[] a= Base64Utils.decode("AQI=");
        System.out.println(a);
    }

    @Test
    public void encodeTest(){
        byte[] B={1,3,2};
        String a= Base64Utils.encode(B);
        System.out.println(a);
    }


}