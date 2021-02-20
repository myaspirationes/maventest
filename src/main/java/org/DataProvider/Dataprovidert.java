package org.DataProvider;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/20 18:12
 */
public class Dataprovidert {


    public int testAdd(int a, int b) {
        // Given:

        // When:
        int result = a + b;

        // Then:
        return result;
    }

}