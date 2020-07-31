package com.jie.interview;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 某场演唱会门票1000张（票面编号0-999），落入了黄牛A、B、C手中，
 * 三人同时对外售票（按编号顺序卖票），票价500/张，总票数每卖掉100张，每张票涨价100元。
 * 请编写出程序，最终打印出三个人卖掉每张票的编号和每人的总收入。
 *
 * @author jie
 * @date 2020/7/7
 */
public class SaleTicket {

    private static ArrayBlockingQueue<Ticket> arrayBlockingQueue = new ArrayBlockingQueue<>(1000);

    public static void main(String[] args){
        automicPlan();
    }

    // 已经卖出的票数
    private static AtomicInteger saledCnt = new AtomicInteger();
    private static AtomicInteger leftCnt = new AtomicInteger(1000);
    // 票价，volatile保持可见性
    private static volatile int ticketPrice = 500;
    private static CountDownLatch countDownLatch = new CountDownLatch(3);

    public static void automicPlan() {
        Sale1Task s1= new Sale1Task("黄牛1");
        Sale1Task s2= new Sale1Task("黄牛2");
        Sale1Task s3= new Sale1Task("黄牛3");
        Thread p1 = new Thread(s1);
        Thread p2 = new Thread(s2);
        Thread p3 = new Thread(s3);

        p1.start();
        p2.start();
        p3.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("黄牛1卖出" + s1.getSelfSaleCnt() + "张，销售额" + s1.getSelfSaleTotalPrice());
        System.out.println("黄牛2卖出" + s2.getSelfSaleCnt() + "张，销售额" + s2.getSelfSaleTotalPrice());
        System.out.println("黄牛3卖出" + s3.getSelfSaleCnt() + "张，销售额" + s3.getSelfSaleTotalPrice());
        System.out.println(saledCnt.get() + "张票总售价=" + (s1.getSelfSaleTotalPrice() + s2.getSelfSaleTotalPrice() + s3.getSelfSaleTotalPrice()));
    }

    /**
     * 利用blockQueue直接将票和票价排好，直接消费即可，不知道算不算解决方案
     */
    public static void blockQueue() {
        for (int i = 0; i < 1000; i++) {
            Ticket ticket = new Ticket(i,500+(i/100)*100);
            try {
                arrayBlockingQueue.put(ticket);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int sum = arrayBlockingQueue.stream().mapToInt(t -> t.getPrice()).sum();
        System.out.println("---"+sum);

        SaleTask s1= new SaleTask("黄牛1",arrayBlockingQueue);
        SaleTask s2= new SaleTask("黄牛2",arrayBlockingQueue);
        SaleTask s3= new SaleTask("黄牛3",arrayBlockingQueue);
        Thread p1 = new Thread(s1);
        Thread p2 = new Thread(s2);
        Thread p3 = new Thread(s3);

        p1.start();
        p2.start();
        p3.start();

        while(true){
            if(arrayBlockingQueue.isEmpty()){
                System.out.println("黄牛1卖出" + s1.getTotalPrice());
                System.out.println("黄牛2卖出" + s2.getTotalPrice());
                System.out.println("黄牛3卖出" + s3.getTotalPrice());
                System.out.println("最终1000张票售出总价=" + s1.getTotalPrice() + s2.getTotalPrice() + s3.getTotalPrice());
                break;
            }

        }
    }

    static class SaleTask implements Runnable{

        private String name;

        private ArrayBlockingQueue arrayBlockingQueue;


        private int selfSaleTotalPrice = 0;

        public SaleTask(String name,ArrayBlockingQueue arrayBlockingQueue){
            this.name= name;
            this.arrayBlockingQueue = arrayBlockingQueue;
        }


        @Override
        public void run(){
            while(true){
                try {
                    Ticket take = (Ticket) arrayBlockingQueue.take();
                    if (take != null){
                        selfSaleTotalPrice += take.getPrice();
                        System.out.println(this.name + "-卖出：" + take.getNum() + "号票");
                    }else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public int getTotalPrice(){
            return this.selfSaleTotalPrice;
        }
    }

    static class Sale1Task implements Runnable{

        private String name;
        private int selfSaleTotalPrice = 0;
        private int selfSaleCnt = 0;

        public Sale1Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (true){
                int left = leftCnt.getAndDecrement();
                if(left <= 0){
                    countDownLatch.countDown();
                    break;
                }

                // 每卖掉100张，票价涨100
                int i = saledCnt.get();
                int salePrice = ticketPrice;
                if(i > 0 && i % 100 == 0){
                    ticketPrice = 500 + (i/100)*100;
                    salePrice = ticketPrice;
                    System.out.println("涨价的票号=====" + i + "，价格变为=" + ticketPrice);
                }

                selfSaleCnt++;
                System.out.println(name + " 卖出" + saledCnt.getAndIncrement() + "，票价=" + salePrice);
                selfSaleTotalPrice += salePrice;
            }
        }

        public int getSelfSaleCnt() {
            return selfSaleCnt;
        }

        public int getSelfSaleTotalPrice() {
            return selfSaleTotalPrice;
        }
    }
}

class Ticket {
    private int num;
    private int price;

    public Ticket(int num,int price) {
        this.num = num;
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
