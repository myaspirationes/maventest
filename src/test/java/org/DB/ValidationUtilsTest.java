package org.DB;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/2 14:09
 */
public class ValidationUtilsTest {
    @Autowired  ValidationUtils validationUtils;

    @Test
    public void isMobileNumTest(){

        String tel="1389090998a";
        Boolean result= validationUtils.isMobileNum(tel);
        Assert.assertEquals(tel+" 是电话号码?",result,true);

    }

    @Test
    public void isUserIDCardTest(){
        String idcard="770111198009080078";
        Boolean result= validationUtils.isUserIDCard(idcard);

        Assert.assertEquals(idcard+" 是身份证号码?",result,true);

    }


}