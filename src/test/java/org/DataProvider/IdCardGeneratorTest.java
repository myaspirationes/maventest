package org.DataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/2 15:46
 */


@SpringBootTest(classes=IdCardGenerator.class)
@RunWith(SpringRunner.class)
/**
 * @SpringBootTest
 * 一旦依赖了spring-boot-starter-test，下面这些类库将被一同依赖进去：
 *
 * JUnit：java测试事实上的标准，默认依赖版本是4.12（JUnit5和JUnit4差别比较大，集成方式有不同）。
 * Spring Test & Spring Boot Test：Spring的测试支持。
 * AssertJ：提供了流式的断言方式。
 * Hamcrest：提供了丰富的matcher。
 * Mockito：mock框架，可以按类型创建mock对象，可以根据方法参数指定特定的响应，也支持对于mock调用过程的断言。
 * JSONassert：为JSON提供了断言功能。
 * JsonPath：为JSON提供了XPATH功能。
 */
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