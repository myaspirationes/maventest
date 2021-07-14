package org.Theads;

/**当一个线程运行时，另外一个线程可以直接通过interrupt()方法中断其运行状态。
 * @author tiger.wang
 * @version 1.0
 * @date 2021/7/13 17:17
 */
class MyThreadInterrupt implements Runnable{ // 实现Runnable接口
    @Override
    public void run(){  // 覆写run()方法
        System.out.println("1、进入run()方法") ;
        try{
            System.out.println("**我执行了。。。。") ;

            Thread.sleep(1000) ;   // 线程休眠10秒
            System.out.println("2、已经完成了休眠") ;
        }catch(InterruptedException e){
            System.out.println("3、休眠被终止") ;
            return ; // 返回调用处
        }
        System.out.println("4、run()方法正常结束") ;
    }


    public static void main(String args[]){
        MyThreadInterrupt mt = new MyThreadInterrupt() ;  // 实例化Runnable子类对象
        Thread t = new Thread(mt);     // 实例化Thread对象
        t.start() ; // 启动线程
        try{
            Thread.sleep(2000) ;    // 线程休眠2秒
        }catch(InterruptedException e){
            System.out.println("3、休眠被终止") ;
        }
        t.interrupt() ; // 中断线程执行
    }
};
