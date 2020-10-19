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

    private int[][] felder = new int[9][9];

    public SudokuSolver() {
        //initialize if necessary
    }

    @Override
    public final int[][] readSudoku(File file) {
        String line;
        int[] reihe = new int[9];
        int a = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(new File("1_sudoku_level1")));
            while ((line = bf.readLine()) != null) {
                String[] lines = line.split(";");
                for (int i = 0; i < felder.length; i++) {
                    reihe = Arrays.stream(lines)
                            .mapToInt(s -> Integer.parseInt(s))
                            .toArray();
                }
                felder[a] = reihe;
                a++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        // implement this method
        return false; // delete this line!
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    // add helper methods here if necessary
}
