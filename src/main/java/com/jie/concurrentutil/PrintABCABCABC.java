package com.jie.concurrentutil;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
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

    private static volatile Flag flag = Flag.T1;

    private static Thread worker = null;
    private static Thread worker1 = null;
    private static Thread worker2 = null;

    public static void main(String[] args) {
        transferQueue();
    }

    public static void volatileResolve() {
        worker = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    while (flag != Flag.T1) {
                        Thread.sleep(100);
                    }
                    System.out.println(i + "- A");
                    flag = Flag.T2;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        worker1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    while (flag != Flag.T2) {
                        Thread.sleep(100);
                    }
                    System.out.println(i + "- B");
                    flag = Flag.T3;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        worker2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    while (flag != Flag.T3) {
                        Thread.sleep(100);
                    }
                    System.out.println(i + "- C");
                    flag = Flag.T1;
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

    public static void transferQueue() {
        TransferQueue<String> transferQueueAB = new LinkedTransferQueue<>();
        TransferQueue<String> transferQueueBC = new LinkedTransferQueue<>();
        TransferQueue<String> transferQueueCA = new LinkedTransferQueue<>();

        worker = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    transferQueueAB.transfer("A");
                    System.out.println(i + "A===== " + transferQueueCA.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A");

        worker1 = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println(i + "B===== " + transferQueueAB.take());
                    transferQueueBC.transfer("B");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B");

        worker2 = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println(i + "C===== " + transferQueueBC.take());
                    transferQueueCA.transfer("C");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"C");

        worker.start();
        worker1.start();
        worker2.start();
    }

    //TODO 还可以用blockingqueue

    enum Flag{
        T1,T2,T3;
    }
}
