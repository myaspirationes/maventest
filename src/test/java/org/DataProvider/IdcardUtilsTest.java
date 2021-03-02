package org.DataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/2 17:18
 */

@SpringBootTest(classes=IdcardUtils.class)
@RunWith(SpringRunner.class)
public class IdcardUtilsTest {

    @Autowired
    private IdcardUtils idcardUtils;

    @Test
    public void conver15CardTo18() {

        String idCard15 = "420123880909009";
        String IdCard18 = idcardUtils.conver15CardTo18(idCard15);
        System.out.println(IdCard18);


    }

    @Test
    public void validateCard() {
    }

    @Test
    public void validateIdCard18() {
    }

    @Test
    public void validateIdCard15() {
    }

    @Test
    public void validateIdCard10() {
    }

    @Test
    public void validateTWCard() {
    }

    @Test
    public void validateHKCard() {
    }

    @Test
    public void converCharToInt() {
    }

    @Test
    public void getPowerSum() {
    }

    @Test
    public void getCheckCode18() {
    }

    @Test
    public void getAgeByIdCard() {
    }

    @Test
    public void getBirthByIdCard() {
    }

    @Test
    public void getYearByIdCard() {
    }

    @Test
    public void getMonthByIdCard() {
    }

    @Test
    public void getDateByIdCard() {
    }

    @Test
    public void getGenderByIdCard() {
    }

    @Test
    public void getProvinceByIdCard() {
    }

    @Test
    public void isNum() {
    }

    @Test
    public void valiDate() {
    }
}