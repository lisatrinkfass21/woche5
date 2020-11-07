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
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SudokuSolver ss = new SudokuSolver();
        int[][] input = ss.readSudoku(new File("1_sudoku_level1.csv"));
        System.out.println(">--- ORIGINAL ---");
        //print the sudoku if you want
        ausgabe(input);
        int[][] output = ss.solveSudokuParallel(input);
        System.out.println(">--- SOLUTION ---");
        // print the sudoku if you want
        ausgabe(output);
        System.out.println(">----------------");
        System.out.println("SOLVED    = " + ss.checkSudokuParallel(output));
        System.out.println(">----------------");

        System.out.println("Benchmark (Laufzeit) nicht parallel: " + ss.benchmark(new int[9][9]) + " msec.");
        System.out.println("Benchmark (Laufzeit) parallel: " + ss.benchmarkParallel(new int[9][9]) + " msec.");

    }

    private static void ausgabe(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j] + "  ");

            }
            System.out.println(" ");

        }
    }
}
