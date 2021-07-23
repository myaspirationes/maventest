package org.JVM;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/1 12:41
 */
public class stackdemo {
    private static  int  index=1;
    public void test(){
        index ++;
        test();
    }

    public static void main(String[] args) {
        stackdemo demo= new stackdemo();
       // new Thread(group,target,name,stacksize);
        try {
            demo.test();
        }catch (Throwable e){
            System.out.println("STACK  DEEP: "+ index);
            System.out.println(e);
        }

    }
}
