package com.jie.interview;

/**
 * 矩阵最短路径
 *
 * @author jie
 * @date 2020/7/8
 */
public class ShortRoad {

    public static void main(String[] args) {
        int[][] array = new int[][]{
                {1,3,4,8},
                {3,2,2,4},
                {5,7,1,9},
                {2,3,2,3}};

        System.out.println(calculate(array,0,0));
        System.out.println(shortRoad(array));
        System.out.println(shortRoadBetter(array));
    }

    public static int calculate(int[][] grid, int i, int j) {
        if (i == grid.length || j == grid[0].length) return Integer.MAX_VALUE;
        if (i == grid.length - 1 && j == grid[0].length - 1) return grid[i][j];
        return grid[i][j] + Math.min(calculate(grid, i + 1, j), calculate(grid, i, j + 1));
    }

    public static int shortRoad(int[][] array) {
        int[][] calc = new int[array.length][array[0].length];

        calc[0][0] = array[0][0];
        // 第一列只能向下
        for (int i = 1; i < array.length; i++) {
            calc[i][0] = calc[i-1][0] + array[i][0];
        }
        for (int i = 1; i < array[0].length; i++) {
            calc[0][i] = calc[0][i-1] + array[0][i];
        }

        for (int i = 1; i < array.length; i++) {
            for (int j = 1; j < array[0].length; j++) {
                calc[i][j] = array[i][j] + Math.min(calc[i - 1][j], calc[i][j - 1]);
            }
        }
        return calc[array.length -1][array[0].length -1];
    }

    public static int shortRoadBetter(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if(i == 0 && j != array[0].length -1){
                    array[i][j + 1] = array[i][j] + array[i][j + 1];
                }
                if (j == 0 && i != array.length -1) {
                    array[i+1][j] = array[i][j] + array[i + 1][j];
                }
                if(j != 0 && i != 0){
                    array[i][j] = array[i][j] + Math.min(array[i][j - 1], array[i - 1][j]);
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(array[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
        return array[array.length -1][array[0].length -1];
    }
}
