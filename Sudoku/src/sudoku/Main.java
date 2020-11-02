/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author Lisa
 */
import java.io.File;

public class Main {

    private static final int[][] STUDENT_SUDOKU_WITH_ONLY_ONE_SOLUTION_SOLVED = new int[][]{
        {1, 7, 5, 9, 8, 2, 3, 6, 4},
        {9, 4, 6, 3, 7, 1, 5, 2, 8},
        {3, 8, 2, 4, 5, 6, 7, 1, 9},
        {8, 2, 7, 5, 3, 9, 1, 4, 6},
        {5, 9, 4, 6, 1, 8, 2, 3, 7},
        {6, 1, 3, 7, 2, 4, 8, 9, 5},
        {4, 3, 1, 8, 9, 7, 6, 5, 2},
        {2, 6, 8, 1, 4, 5, 9, 7, 3},
        {7, 5, 9, 2, 6, 3, 4, 8, 1}
    };

    public static void main(String[] args) {
        SudokuSolver ss = new SudokuSolver();
        int[][] input = ss.readSudoku(new File("1_sudoku_level1.csv"));

        System.out.println(">--- ORIGINAL ---");
        //print the sudoku if you want
        int[][] output = ss.solveSudoku(input);
        System.out.println(">--- SOLUTION ---");
        // print the sudoku if you want
        System.out.println(">----------------");
        System.out.println("SOLVED    = " + ss.checkSudoku(output));
        System.out.println(">----------------");
    }
}
