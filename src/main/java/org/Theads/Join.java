package org.Theads;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/13 16:31
 */


    class MyThread implements Runnable{ // 实现Runnable接口
        @Override
        public void run(){  // 覆写run()方法
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName()
                        + "运行，i = " + i) ;  // 取得当前线程的名字
            }
        }


    public static void main(String args[]){
            MyThread mt = new MyThread() ;  // 实例化Runnable子类对象
            Thread t = new Thread(mt,"线程");     // 实例化Thread对象
        Thread t1 = new Thread(mt,"线程1");     // 实例化Thread对象
        t1.start() ; // 启动线程
        t.start() ; // 启动线程
            for(int i=0;i<10;i++){
                if(i>5){
                    try{
                        t.join() ;  // 线程强制运行
                       t1.join() ;  // 线程强制运行

                       // System.out.println("Main线程运行 --> " + i) ;
                    }catch(InterruptedException e){
                    }
                }
                System.out.println("Main线程运行 --> " + i) ;
            }
        }


}
