package org.DB;


import org.Mock.BeMocked;
import org.Mock.UseMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/1/28 17:46
 */


@RunWith(PowerMockRunner.class) // 告诉JUnit使用PowerMockRunner进行测试
@PrepareForTest({BeMocked.class}) // 所有需要测试的类列在此处，适用于模拟final类或有final, private, static, native方法的类
@PowerMockIgnore("javax.management.*") //为了解决使用powermock后，提示classloader错误
public class MockTest {


    @InjectMocks  //创建一个实例，简单地说就是这个Mock可以调用真实代码的方法，其余用@Mock（或@Spy）注解创建的mock将被注入到用该实例中。
    private UseMock useMock;//    mockito 会将 @Mock、@Spy 修饰的对象自动注入到 @InjectMocks 修饰的对象中
    @Mock
    private BeMocked beMocked;//需要mock的类

    /**
     *
     * 指定被 mock的方法的返回值
     */
    @Test
    public void testpw(){
        PowerMockito.mockStatic(BeMocked.class);//mock类中的静态方法
        PowerMockito.when(beMocked.add()).thenReturn(7);//mock该方法，返回指定的值
        System.out.println("useMock = " + useMock);
        Assert.assertEquals(325, useMock.addtest());
    }

}
