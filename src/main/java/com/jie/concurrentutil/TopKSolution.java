package com.jie.concurrentutil;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Top k问题解法--利用大顶堆，小顶堆
 *
 * @author jie
 * @date 2020/8/13
 */
public class TopKSolution {

    public static void main(String[] args) {
        int[] array = new int[]{4,5,2,6,1,7,3,8};

        priorityQueue(array, 4);
    }

    /**
     * 从array中找出最小的k个数
     * 利用大顶堆实现
     * @param array
     * @param k
     * @return
     */
    public static List<Integer> priorityQueue(int[] array, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, (o1, o2) -> o2.compareTo(o1));

        for (int i : array) {
            if(maxHeap.size() != k){
                maxHeap.offer(i);
            }else if (maxHeap.peek() > i){
                maxHeap.poll();
                maxHeap.offer(i);
            }
        }

        List<Integer> result = new ArrayList<>(k);
        for (Integer integer : maxHeap) {
            result.add(integer);
        }

        result.forEach(System.out::println);

        return result;
    }
}
