package com.jie.sort;

/**
 * 希尔排序算法
 *
 * @author jie
 * @date 2020/8/14
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] array = {8,1,9,1,2,3,7,6,5};
        shellSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }

    public static int[] shellSort(int[] array) {
        int len = array.length;
        int gap = len/2;
        int tmp;
        while (gap > 0) {
            for (int i = gap; i < len; i++) {
                tmp = array[i];
                int preIndex = i-gap;
                while(preIndex >= 0 && array[preIndex] > tmp){
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }

                array[preIndex + gap] = tmp;
            }
            gap = gap/2;
        }
        return array;
    }
}
