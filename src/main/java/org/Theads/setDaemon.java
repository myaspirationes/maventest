package org.Theads;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/13 17:26
 */
public class setDaemon implements Runnable {

    @Override
    public void run() {  // 覆写run()方法
        while (true) {
            System.out.println(Thread.currentThread().getName() + "在运行。");
        }
    }

    /**
     *
     * 在 Java 程序中，只要前台有一个线程在运行，则整个 Java 进程都不会消失，所以此时可以设置一个后台线程，
     * 这样即使 Java 线程结束了，此后台线程依然会继续执行，要想实现这样的操作，直接使用 setDaemon() 方法即可。
     */


    public static void main(String args[]) {
        setDaemon mt = new setDaemon();  // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程");     // 实例化Thread对象
        t.setDaemon(true); // 此线程在后台运行
        t.start(); // 启动线程
    }
};
