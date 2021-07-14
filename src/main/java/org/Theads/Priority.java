package org.Theads;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/13 17:34
 */
public class Priority implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500); // 线程休眠
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName()
                    + "运行，i = " + i);  // 取得当前线程的名字
        }
    }

    /**
     * 从程序的运行结果中可以观察到，线程将根据其优先级的大小来决定哪个线程会先运行，但是需要注意
     * 并非优先级越高就一定会先执行，哪个线程先执行将由 CPU 的调度决定。
     * @param args
     */
    public static void main(String args[]) {
        Thread t1 = new Thread(new Priority(), "线程A");  // 实例化线程对象
        Thread t2 = new Thread(new Priority(), "线程B");  // 实例化线程对象
        Thread t3 = new Thread(new Priority(), "线程C");  // 实例化线程对象
        t1.setPriority(Thread.MIN_PRIORITY);   // 优先级最低
        t2.setPriority(Thread.MAX_PRIORITY);   // 优先级最高
        t3.setPriority(Thread.NORM_PRIORITY);  // 优先级最中等
        t1.start();    // 启动线程
        t2.start();    // 启动线程
        t3.start();    // 启动线程
    }


}
