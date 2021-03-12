package org.Parameter;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/11 15:17
 */
public class Parameter {
    @Parameters({ "username","password" })
    @Test(groups = {"functiontest"})
    public void pamameterUse1(String username,String password){
        System.out.println(username+password);
    }

}
