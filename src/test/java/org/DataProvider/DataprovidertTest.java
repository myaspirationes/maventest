package org.DataProvider;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/20 19:02
 */


@RunWith(DataProviderRunner.class)
public class DataprovidertTest {


    @DataProvider
    public static Object[][] dataProviderAdd() {
        int k = 0;
        String[] param = {"2367952", "2367141", "2366552", "2364334"};
        Object[][] result = new Object[param.length][];
        for (int i = 0; i < param.length; i++) {

            String url = "https://blog.51cto.com/357712148/" + param[i];
            result[k++] = new Object[]{url};
        }
        //System.out.println(Arrays.stream(result).findFirst().toString());

        return result;
    }


    @Test
    @UseDataProvider("dataProviderAdd")
    public void testAddTest(String url) {
                System.out.println(url);
    }

}
