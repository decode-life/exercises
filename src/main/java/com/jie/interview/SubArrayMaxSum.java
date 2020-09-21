package com.jie.interview;

import sun.util.resources.cldr.en.CalendarData_en_UM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数组最长子数组且累加和最大
 *
 * @author jie
 * @date 2020/8/26
 */
public class SubArrayMaxSum {

    public static void main(String[] args) {
        int[] array = {1,-2,1,1,3,-6,3,-4,5,-7,3,1,1};

        List<List<Integer>> ans = maxSumAndMaxLen(array);
        for (List<Integer> an : ans) {
            System.out.println("start:" + an.get(0) + ",end:" + an.get(1));
        }
    }

    /**
     * 主题思路，最长子串，最大和，肯定是 一直加后面的值，如果出现了小于加之前的和值，说明后面这位是负数，并且前面的子串也不可能再用了
     * 从后面的坐标为重新开始累加
     * @param array
     * @return
     */
    public static List<List<Integer>> maxSumAndMaxLen(int[] array) {
        List<List<Integer>> result = new ArrayList<>();
        if(array == null || array.length == 0){
            return result;
        }

        int l = 0;
        int curSum = 0;
        int maxSum = Integer.MIN_VALUE;
        int maxLen = -1;

        for (int i = 0; i < array.length; i++) {
            curSum += array[i];
            int curLen = i - l + 1;

            if(curSum > maxSum || (curSum == maxSum && maxLen < curLen)){
                result.clear();
                result.add(Arrays.asList(l, i));
                maxLen = curLen;
            } else if (curSum == maxSum && maxLen == curLen) {
                result.add(Arrays.asList(l, i));
            }
            maxSum = Math.max(maxSum, curSum);

            if (curSum < 0) {
                curSum = 0;
                l = i + 1;
            }
        }

        System.out.println(maxSum);
        return result;
    }
}
