package org.YDF;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/4/6 17:12
 */
public class BeanShellUtils {

    public String getCurrentTime(){

        long date=System.currentTimeMillis();
        String time=date+"";
        return  time;
    }

    public static void main(String[] args) {
        System.out.println("args = " + args);;
    }

}
