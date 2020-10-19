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
import java.nio.file.Files;
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
        /* for (int i = 0; i < rawSudoku[0].length; i++) {   // checkt reihe
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
        }*/
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
            }
            if (!check9Felder(reihe)) {
                return false;
            }
            aktuelleStelle = 0;
            if (row == 9) {
                row = 0;
            } else {
                spalte = 0;
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
        // implement this method
        return new int[0][0]; // delete this line!
    }

    // add helper methods here if necessary
}
