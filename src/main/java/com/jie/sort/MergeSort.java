package com.jie.sort;

import java.util.Arrays;

/**
 * 归并排序
 * @author jie
 * @date 2020/7/19
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] array = {8,1,9,1,2,3,7,6,5};
        array = MergeSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 归并排序
     * 时间复杂度 O(n log n)
     * 空间复杂度 O(n)
     * @param array
     * @return
     */
    public static int[] MergeSort(int[] array){
        if(array.length < 2){
            return array;
        }
        int middle = array.length/2;
        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);
        return MergeSort(MergeSort(left), MergeSort(right));
    }

    /**
     * 归并排序，2路归并，直接一个大循环相当于选择排序了
     * @param left
     * @param right
     * @return
     */
    public static int[] MergeSort(int[] left,int[] right){
        int[] result = new int[left.length + right.length];

        for (int index = 0,i = 0,j=0; index < result.length; index++) {
            if (i >= left.length) {
                result[index] = right[j++];
            } else if (j >= right.length) {
                result[index] = left[i++];
            } else if (left[i] > right[j]) {
                result[index] = right[j++];
            }else {
                result[index] = left[i++];
            }
        }
        return result;
    }

}
