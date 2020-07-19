package com.jie.sort;

/**
 * 冒泡排序
 * @author jie
 * @date 2020/7/19
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] array = {5,2,1,3,1,0};
        bubbleSort(array);
        System.out.println(array);
    }

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
