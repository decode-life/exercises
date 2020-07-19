package com.jie.sort;

/**
 * 选择排序
 * @author jie
 * @date 2020/7/19
 */
public class SelectionSort {

    public static void main(String[] args) {
        int[] array = {8,1,9,1,2,3,7,6,5};
        selectionSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 时间复杂度 O(n2)
     * 空间复杂度 O(1)
     * @param array
     * @return
     */
    public static int[] selectionSort(int[] array) {

        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int i1 = i; i1 < array.length; i1++) {
                if(array[i1] < array[minIndex]){
                    minIndex = i1;
                }
            }
            int start = array[i];
            array[i] = array[minIndex];
            array[minIndex] = start;
        }
        return array;
    }
}
