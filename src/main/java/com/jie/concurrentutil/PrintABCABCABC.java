package com.jie.concurrentutil;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * d
 *
 * @author jie
 * @date 2020/7/17
 */
public class PrintABCABCABC {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition a = lock.newCondition();
    private static Condition b = lock.newCondition();
    private static Condition c = lock.newCondition();

    private static volatile int flag = 0;

    private static Thread worker = null;
    private static Thread worker1 = null;
    private static Thread worker2 = null;

    public static void main(String[] args) {
        lockSupport();
    }

    public static void volatileResolve() {
        worker = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    if(i != 0) {
                        while (flag != 0) {
                            Thread.sleep(100);
                        }
                    }
                    System.out.println(i + "- A");
                    flag = 1;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        worker1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    while (flag != 1) {
                        Thread.sleep(100);
                    }
                    System.out.println(i + "- B");
                    flag = 2;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        worker2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    while (flag != 2) {
                        Thread.sleep(100);
                    }
                    System.out.println(i + "- C");
                    flag = 0;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        worker.start();
        worker1.start();
        worker2.start();
    }

    /**
     * Lock 配合condition
     */
    public static void lockCondition() {
        worker = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    if(lock.tryLock(100L, TimeUnit.MILLISECONDS)) {
                        System.out.println("A lock");
                        if (i != 0) {
                            c.await();
                        }
                        System.out.println(i + "===== A");
                        a.signal();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                    System.out.println("A unlock");
                }

            }
        });
        worker1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                lock.lock();
                try {
                    System.out.println("B lock");
                    a.await();
                    System.out.println(i + "===== B");
                    b.signal();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                    System.out.println("B unlock");
                }

            }
        });
        worker2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                lock.lock();
                try {
                    System.out.println("C lock");
                    b.await();
                    System.out.println(i + "===== C");
                    c.signal();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                    System.out.println("C unlock");
                }

            }
        });

        worker.start();
        worker1.start();
        worker2.start();
    }

    /**
     * lockSupport解法
     */
    public static void lockSupport(){

        worker = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                if (i != 0) {
                    LockSupport.park();
                }
                System.out.println(i + "===== A");
                LockSupport.unpark(worker1);
            }
        },"A");

        worker1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                LockSupport.park();
                System.out.println(i + "===== B");
                LockSupport.unpark(worker2);
            }
        },"B");

        worker2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                LockSupport.park();
                System.out.println(i + "===== C");
                LockSupport.unpark(worker);
            }
        },"C");

        worker.start();
        worker1.start();
        worker2.start();
    }
}
