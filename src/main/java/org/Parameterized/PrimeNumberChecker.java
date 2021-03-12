package org.Parameterized;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/11 14:40
 */
public class PrimeNumberChecker {
    public Boolean validate(final Integer primeNumber) {
        for (int i = 2; i < (primeNumber / 2); i++) {
            if (primeNumber % i == 0) {
                return false;
            }
        }
        return true;
    }

}
