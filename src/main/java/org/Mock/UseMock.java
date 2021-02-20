package org.Mock;

import org.Mock.BeMocked;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/1/28 16:09
 */


public class UseMock {


    @Autowired
    private BeMocked bemocked;

    public  int  addtest(){
        int x=3;
        int y=2;
        int z=bemocked.add();

        return (y+x)*z;

    }


}
