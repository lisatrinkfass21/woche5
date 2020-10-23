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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
 *    Hier sollte die Antwort auf die Aufgabe 4 stehen.
 * </answerTask4>
 */
public class SudokuSolver implements ISodukoSolver {

    public SudokuSolver() {
        //initialize if necessary
    }

    @Override
    public final int[][] readSudoku(File file) {
        int[][] felder = new int[9][9];
        String line;
        int a = 0;

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            while ((line = bf.readLine()) != null) {
                String[] lines = line.split(";");
                felder[a] = Arrays.stream(lines)
                        .mapToInt(s -> Integer.parseInt(s))
                        .toArray();
                a++;

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return felder;

    }

    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        int[] reihe = new int[9];
        for (int i = 0; i < rawSudoku[0].length; i++) {   // checkt reihe
            reihe = rawSudoku[i];
            if (!check9Felder(reihe)) {
                return false;
            }
        }

        for (int j = 0; j < rawSudoku[1].length; j++) { // checkt spalte
            for (int a = 0; a < rawSudoku.length; a++) {
                reihe[a] = rawSudoku[j][a];
            }
            if (!check9Felder(reihe)) {
                return false;
            }
        }
        int row = 0;
        int spalte = 0;
        int aktuelleStelle = 0;
        for (int x = 0; x < 9; x++) {
            for (int e = 0; e < 3; e++) {
                for (int i = 0; i < 3; i++) {
                    reihe[aktuelleStelle] = rawSudoku[row][spalte];
                    spalte++;
                    aktuelleStelle++;
                }
                row++;
                spalte = spalte - 3;
            }
            if (!check9Felder(reihe)) {
                return false;
            }
            aktuelleStelle = 0;
            if (row >= 8) {
                row = 0;
                spalte += 3;
            }

        }

        return true;
    }

    private boolean check9Felder(int[] rowColumn) {
        int sum = Arrays.stream(rowColumn)
                .reduce(0, (a, b) -> a + b);
        if (sum == 45) {
            return true;
        }
        return false;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {

        return new int[0][0];
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        int[][][] arrOptions = new int[9][9][9];
        int[][] solution = new int[9][9];
        int[] block = new int[9];
        for (int i = 0; i < rawSudoku.length; i++) {
            for (int j = 0; j < rawSudoku.length; j++) {
                if (rawSudoku[i][j] == 0) {
                    solution[i][j] = rawSudoku[i][j];
                } else {
                    for (int k = 0; k < rawSudoku.length; k++) {
                        arrOptions[i][j][k] = k;
                    }
                }
            }
        }

        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution.length; j++) {
                if (solution[i][j] != 0) {
                    block = solution[i];

                }

            }

        }

        return new int[0][0]; // delete this line!
    }

    private int[] checkOptions(int[] options, int[] block) {
        for (int i = 0; i < options.length; i++) {
            for (int j = 0; j < block.length; j++) {
                if (block[j] == options[i]) {
                    break;

                }

            }

        }
        return options;
    }

    public long benchmark(int[][] rawSudoku) {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {

        }
        long after = System.currentTimeMillis();
        return before - after / 10;
    }
}
