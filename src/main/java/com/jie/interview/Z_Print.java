package com.jie.interview;

import java.util.Arrays;

/**
 * Z字形变换
 *
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 *
 * @author jie
 * @date 2020/9/21
 */
public class Z_Print {

    public static void main(String[] args) {
        String s = "LEETCODEISHIRING";

        String resulotion = sulotion(s, 3);
        System.out.println(resulotion);
    }

    /**
     * 思路如下：
     * L   C   I   R
     * E T O E S I I G
     * E   D   H   N
     *
     * 几行就用一个几列数组存储
     *
     * 然后对于上述排列，可以拆成如下循环，以2*numOfRows-2个字符 为一组循环
     * L     C     I     R
     * E T   O E   S I   I G
     * E     D     H     N
     * @param s 源字符串
     * @param numOfRows 分割成几行
     * @return
     */
    public static String sulotion(String s, int numOfRows) {
        if(numOfRows == 1) return s;

        char[] chars = s.toCharArray();

        String[] result = new String[numOfRows];
        Arrays.fill(result,"");

        int periodLen = 2 * numOfRows - 2;
        for (int i = 0; i < chars.length; i++) {
            int mod = i % periodLen;
            if (mod < numOfRows) {
                result[mod] += chars[i];
            }else{
                result[periodLen - mod] += chars[i];
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s1 : result) {
            stringBuilder.append(s1).append("-");
        }
        return stringBuilder.toString();
    }
}
