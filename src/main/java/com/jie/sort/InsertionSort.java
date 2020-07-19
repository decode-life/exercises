package com.jie.sort;

/**
 * @author jie
 * @date 2020/7/19
 */
public class InsertionSort {

    public static void main(String[] args) {
        int[] array = {8,1,9,1,2,3,7,6,5};
        insertionSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 插入排序
     * 时间复杂度 O(n2)  最佳 O(n) 因为有序情况下，第一层循环每次的pick值都比上一个大，所以第二层循环其实没有发生
     * 空间复杂度 O(1)
     * @param array
     * @return
     */
    public static int[] insertionSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int temp = array[i];
            for (int j = i - 1; j >= 0; j--) {
                if(temp < array[j]){
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }
}
