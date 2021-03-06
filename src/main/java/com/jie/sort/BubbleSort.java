package com.jie.sort;

/**
 * 冒泡排序
 * @author jie
 * @date 2020/7/19
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] array = {8,1,9,1,2,3,7,6,5};
        bubbleSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 冒泡排序
     *  时间复杂度 O(n*n),最好情况下 O(n),只要内层循环做个标识，没有替换标识已经排好序
     *  空间复杂度 O(1)
     *  稳定排序
     * @param array
     * @return
     */
    public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if(array[j] > array[j + 1]){
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }
}
