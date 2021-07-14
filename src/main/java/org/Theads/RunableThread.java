package org.Theads;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/13 16:01
 */
public class RunableThread implements Runnable {
    private String name;       // 表示线程的名称

    public RunableThread(String name) {
        this.name = name;      // 通过构造方法配置name属性
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            System.out.println("MyRunnable执行...." + name + "运行，i = " + i);
        }
    }


    public static void main(String[] args) {
        RunableThread mt1 = new RunableThread("线程A ");    // 实例化对象
        RunableThread mt2 = new RunableThread("线程B ");    // 实例化对象
        Thread t1 = new Thread(mt1);       // 实例化Thread类对象
        Thread t2 = new Thread(mt2);       // 实例化Thread类对象

        //在线程启动虽然调用的是 start() 方法，但实际上调用的却是 run() 方法定义的主体。
        t1.start();    // 启动多线程
        t2.start();    // 启动多线程

//
//        在此提出一个问题，Java 程序每次运行至少启动几个线程？？？？
//
//        回答：至少启动两个线程，每当使用 Java 命令执行一个类时，实际上都会启动一个 JVM，
//        每一个JVM实际上就是在操作系统中启动一个线程，Java 本身具备了垃圾的收集机制。
//        所以在 Java 运行时至少会启动两个线程，一个是 main 线程，另外一个是垃圾收集线程。
//
    }
}
