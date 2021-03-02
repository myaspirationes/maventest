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
 * @date 2021/3/2 15:46
 */


@SpringBootTest(classes=IdCardGenerator.class)
@RunWith(SpringRunner.class)
public class IdCardGeneratorTest {

    @Autowired
    private IdCardGenerator idCardGenerator;

    @Test
    public  void IdCardGeneratorTest(){
        //IdCardGenerator idCardGenerator= new IdCardGenerator();
       // String IdNumber= null;
        String IdNumber=idCardGenerator.generate();
        System.out.println(IdNumber);

    }

}