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

    int[][] solution = new int[9][9];

    public SudokuSolver() {

    }

    @Override
    public final int[][] readSudoku(File file) { //fertig
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

        /*for (int i = 0; i < rawSudoku[0].length; i++) {   // checkt reihe
            reihe = rawSudoku[i];
            if (!check9Felder(reihe)) {
                return false;
            }
        }*/
        for (int i = 0; i < rawSudoku.length; i++) { //checkt reihe mit streams
            if (Arrays.stream(rawSudoku[i])
                    .distinct(). //löscht alle doppelten Elemente in Liste (alle 0) oder doppelt vorkommende Zahlen
                    toArray().length != 9) {
                return false;
            }
        }

        int[] reihe = new int[9];
        /*for (int zeile = 0; zeile < rawSudoku.length; zeile++) { // checkt spalte
            for (int spalte = 0; spalte < rawSudoku.length; spalte++) {
                reihe[spalte] = rawSudoku[zeile][spalte];
            }
            if (!check9Felder(reihe)) {
                return false;
            }
        }*/
        for (int zeile = 0; zeile < rawSudoku.length; zeile++) { // checkt spalte mit streams
            for (int spalte = 0; spalte < rawSudoku.length; spalte++) {
                reihe[spalte] = rawSudoku[zeile][spalte];
            }
            if (Arrays.stream(reihe)
                    .distinct().
                    toArray().length != 9) {
                return false;
            }
        }

        reihe = new int[9];
        int row = 0;
        int spalte = 0;

        /*while(row !=9){ //check 9erBlock
                int counter = 0;
                int[] arr = new int[9];
                for(int i = row; i < row+3; i++){
                    for (int j = spalte; j < spalte+3; j++) {
                        arr[counter] = rawSudoku[i][j];
                        counter++;
                    }
                }
                if (!check9Felder(reihe)) {
                    return false;
                }
                spalte += 3;
                if (spalte == 9) {
                    spalte = 0;
                    row += 3;
                }

            }*/
        while (row != 9) { // check 9erBlock mit streams
            int counter = 0;
            int[] arr = new int[9];
            for (int i = row; i < row + 3; i++) {
                for (int j = spalte; j < spalte + 3; j++) {
                    arr[counter] = rawSudoku[i][j];
                    counter++;
                }
            }
            if (Arrays.stream(arr).distinct().toArray().length != 9) {
                return false;
            }
            spalte += 3;
            if (spalte == 9) {
                spalte = 0;
                row += 3;
            }
        }

        reihe = new int[9];
        return true;
    }

    private boolean check9Felder(int[] rowColumn) { //wird nur ohne Threads benötigt
        int sum = Arrays.stream(rowColumn)
                .reduce(0, (a, b) -> a + b);
        if (sum == 45) {
            return true;
        }
        return false;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        int[][][] arrOptions = new int[9][9][9];
        int[] block = new int[9];

        for (int i = 0; i < rawSudoku.length; i++) {
            for (int j = 0; j < rawSudoku.length; j++) {
                if (rawSudoku[i][j] != 0) {
                    solution[i][j] = rawSudoku[i][j];
                } else {
                    for (int k = 0; k < rawSudoku.length; k++) {
                        arrOptions[i][j][k] = k + 1;   //befüllen von arrOption mit allen Ziffern (1-9)
                    }
                }
            }
        }

        while (checkSudoku(solution) != true) {
            //Überprüfung der Reihen
            for (int i = 0; i < solution.length; i++) {
                for (int j = 0; j < solution.length; j++) {
                    if (solution[i][j] == 0) {  //überprüfung ob Stelle schon fixe Zahl hat
                        block = solution[i];

                        for (int k = 0; k < solution.length; k++) {
                            if (arrOptions[i][j][k] != 0) { //Überprüfung ob die Option nicht schon gelöscht wurde
                                if (existenz(block, arrOptions[i][j][k])) {//Überprüfung ob Option hier möglich ist
                                    arrOptions[i][j][k] = 0;
                                }
                            }

                        }
                        only1Option(arrOptions, i, j); // überprüfung ob nur mehr eine Option über bleibt und Speicherung in Solutions

                    }

                }

            }

            block = new int[9];

            //Überprüfung der Spalten
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (solution[j][i] == 0) {
                        for (int k = 0; k < 9; k++) {
                            block[k] = solution[k][i];

                        }

                        for (int k = 0; k < solution.length; k++) {
                            if (arrOptions[j][i][k] != 0) { //Überprüfung ob die Option nicht schon gelöscht wurde
                                if (existenz(block, arrOptions[j][i][k])) {//Überprüfung ob Option hier möglich ist
                                    arrOptions[j][i][k] = 0;
                                }
                            }

                        }
                        only1Option(arrOptions, j, i); // überprüfung ob nur mehr eine Option über bleibt und Speicherung in Solutions
                    }
                }

            }

            //Überprüfung der 9erBlöcke
            block = new int[9];
            int row = 0;
            int spalte = 0;
            int aktuelleStelle = 0;
            for (int y = 0; y < solution.length; y++) {
                for (int x = 0; x < solution.length; x++) {
                    if (solution[y][x] == 0) { // überprüft ob Stelle schon fixen Wert hat

                        if (y < 3) { //-> zuweisen der 9erBlock Bereiche
                            row = 0;
                        } else if (y < 6) {
                            row = 3;
                        } else {
                            row = 6;
                        }
                        if (x < 3) {
                            spalte = 0;
                        } else if (x < 6) {
                            spalte = 3;
                        } else {
                            spalte = 6;
                        }
                        aktuelleStelle = 0;

                        for (int e = 0; e < 3; e++) {
                            for (int i = 0; i < 3; i++) {
                                block[aktuelleStelle] = solution[row][spalte];
                                spalte++;
                                aktuelleStelle++;
                            }
                            row++;
                            spalte = spalte - 3;
                        }
                        for (int i = 0; i < solution.length; i++) {
                            if (arrOptions[y][x][i] != 0) { //Überprüfung ob die Option nicht schon gelöscht wurde
                                if (existenz(block, arrOptions[y][x][i])) {//Überprüfung ob Option hier möglich ist
                                    arrOptions[y][x][i] = 0;
                                }
                            }

                        }
                        only1Option(arrOptions, y, x);
                    }
                }

            }
        }
        block = new int[9];
        arrOptions = new int[9][9][9];

        return solution;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {

        return new int[0][0];
    }

    public long benchmark(int[][] rawSudoku) { //keine Ahnung wofür der Parameter notwendig war,wenn wir auch readSudoku messen müssen müssen?
        File file = new File("2_sudoku_level1.csv");
        long before = System.currentTimeMillis();
        int[][] unsolved = new int[9][9];
        int[][] solved = new int[9][9];
        for (int i = 0; i < 10; i++) {
            solution = new int[9][9];
            unsolved = readSudoku(file);
            solved = solveSudoku(unsolved);
            checkSudoku(solved);
            unsolved = new int[9][9];
            solved = new int[9][9];

        }
        long after = System.currentTimeMillis();
        return (after - before) / 10;
    }

    private boolean existenz(int[] block, int option) {
        for (int i = 0; i < block.length; i++) {
            if (block[i] == option) {
                return true;
            }

        }
        return false;
    }

    private int only1Option(int[][][] options, int stellei, int stellej) {
        int count = 0; //zählt die Anzahl der Zahlen über 0
        int stelle = 0;
        for (int k = 0; k < options.length; k++) {

            if (options[stellei][stellej][k] != 0) {
                count++;
                stelle = k;
            }

        }
        if (count == 1) {
            solution[stellei][stellej] = options[stellei][stellej][stelle];
        }
        return count;

    }
}
