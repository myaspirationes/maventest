package org.Theads;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/12 17:05
 */
public class MyThead  extends Thread{
    @Override
    public void run() {
        super.run();
        System.out.println("执行我的线程.....");
    }


    public static void main(String[] args) {
        MyThead myThead=new MyThead();
        myThead.start();
    }
}
