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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
 *    Hier sollte die Antwort auf die Aufgabe 4 stehen.
 * </answerTask4>
 */
public class SudokuSolver implements ISodukoSolver {

    int[][] solution = new int[9][9];
    private static boolean changed = false;

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
            reihe = new int[9];
            for (int i = row; i < row + 3; i++) {
                for (int j = spalte; j < spalte + 3; j++) {
                    reihe[counter] = rawSudoku[i][j];
                    counter++;
                }
            }
            if (Arrays.stream(reihe).distinct().toArray().length != 9) {
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

    public boolean checkSudokuParallel(int[][] rawSudoku) throws InterruptedException, ExecutionException {
        List<Callable<Boolean>> calls = new ArrayList<>();

        Callable<Boolean> calRow = () -> {
            for (int i = 0; i < rawSudoku.length; i++) { //checkt reihe mit streams
                if (Arrays.stream(rawSudoku[i])
                        .distinct(). //löscht alle doppelten Elemente in Liste (alle 0) oder doppelt vorkommende Zahlen
                        toArray().length != 9) {
                    return false;
                }
            }
            return true;
        };
        Callable<Boolean> calColumn = () -> {
            int[] reihe = new int[9];
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
            return true;
        };
        Callable<Boolean> calBloc = () -> {
            int row = 0;
            int spalte = 0;
            while (row != 9) {
                int counter = 0;
                int[] reihe = new int[9];
                for (int i = row; i < row + 3; i++) {
                    for (int j = spalte; j < spalte + 3; j++) {
                        reihe[counter] = rawSudoku[i][j];
                        counter++;
                    }
                }
                if (Arrays.stream(reihe).distinct().toArray().length != 9) {
                    return false;
                }
                spalte += 3;
                if (spalte == 9) {
                    spalte = 0;
                    row += 3;
                }
            }
            return true;
        };

        calls.add(calRow);
        calls.add(calBloc);
        calls.add(calColumn);

        ExecutorService ex = Executors.newFixedThreadPool(3);
        boolean sol = true;
        List<Future<Boolean>> results = ex.invokeAll(calls);
        for (int i = 0; i < results.size(); i++) {
            if (!(results.get(i).get())) {
                sol = false;
            }
        }
        ex.shutdown();
        return sol;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        int[][][] options = new int[9][9][9];
        int row = 0;
        int column = 0;

        for (int i = 0; i < rawSudoku.length; i++) {
            for (int j = 0; j < rawSudoku[i].length; j++) {
                if (rawSudoku[i][j] != 0) {
                    options[i][j] = new int[]{rawSudoku[i][j]};
                } else {
                    options[i][j] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};//befüllen von allen möglichen Optionen
                }
            }
        }

        boolean change = false;
        while (!change) {
            for (int i = 0; i < options.length; i++) { //löscht alle Optionen von einer Reihe
                int[][] numbers = Arrays.stream(options[i]).filter(arr -> arr.length == 1).toArray(int[][]::new);//speichert alle fixen Werte einer Reihe in numbers
                for (int j = 0; j < numbers.length; j++) {
                    int index = numbers[j][0] - 1; //stelle des fixen Wertes in optionsarray
                    Arrays.stream(options[i]).forEach(arr -> {
                        if (arr.length == 9) {
                            arr[index] = 0;
                        }
                    });
                }
            }

            for (int i = 0; i < options.length; i++) {//löscht alle Optionen von einer Spalte
                int[][] columnArr = new int[9][];
                for (int j = 0; j < options.length; j++) {
                    columnArr[j] = options[j][i];//speichert alle möglichen Optionen spaltenweise ab bzw 1 zahl bei schon fixen Werten
                }
                int[][] numbers = Arrays.stream(columnArr).filter(arr -> arr.length == 1).toArray(int[][]::new); //speichert alle fixen Werte
                for (int j = 0; j < numbers.length; j++) {
                    int index = numbers[j][0] - 1; //speichert Stelle wo sich fixer Wert befindet
                    Arrays.stream(columnArr).forEach(arr -> {
                        if (arr.length == 9) {
                            arr[index] = 0;
                        }
                    });
                }
                for (int j = 0; j < columnArr.length; j++) {
                    options[j][i] = columnArr[j]; //speichert alle optionen in options
                }
            }

            while (row != 9) { //9erBlock iteration -> löscht alle Optionen von einem 9erBlock
                for (int i = row; i < row + 3; i++) {
                    for (int j = column; j < column + 3; j++) {
                        if (options[i][j].length == 1) {//wenn stelle fixen Wert hat
                            for (int k = row; k < row + 3; k++) {
                                for (int l = column; l < column + 3; l++) {
                                    if (options[k][l].length != 1) {
                                        options[k][l][options[i][j][0] - 1] = 0;//löscht die option wenn es in diesem block schon vorhanden ist
                                    }
                                }
                            }
                        }
                    }
                }
                column += 3; //springt um 3 spalten weiter
                if (column == 9) {// wenn am ende der Tabelle
                    column = 0;//wird auf spalte 1 zurück gesetzt
                    row += 3;//und reihen werden um 3 verändert
                }
            } // bis jetzt wurden alle möglichen Optionen aus dem optionen array gelöscht
            change = true;
            while (change) { //reihenweise überprüfung
                change = false;

                for (int i = 0; i < options.length; i++) {
                    int[][] currentArr = options[i]; //speichert reihenweise die Optionen
                    for (int j = 0; j < currentArr.length; j++) {
                        if (currentArr[j].length == 9) {
                            List<Integer> numbers = new ArrayList<>();
                            for (int k = 0; k < currentArr[j].length; k++) {
                                if (currentArr[j][k] != 0) { //wenn möglichkeit noch nicht gelöscht wurde
                                    numbers.add(currentArr[j][k]);//möglichkeiten werden in arraylist gespeichert
                                }
                            }
                            for (int a = 0; a < numbers.size(); a++) {
                                int counter = 0;
                                for (int k = 0; k < currentArr.length; k++) {
                                    List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList()); //boxed speichert alle arrayelemente in integers
                                    if (temp.contains(numbers.get(a))) {//schaut ob number(a) in temp vorkommt
                                        counter++;
                                    }
                                }
                                if (counter == 1) {//wen nur eine zahl in temp vorkommt kann diese fixiert werden
                                    options[i][j] = new int[]{numbers.get(a)}; //in options wird nur mehr 1 Wert gespeichert
                                    change = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < options.length; i++) {//spaltenweiseüberprüfung
                    int[][] currentArr = new int[options[i].length][];
                    for (int x = 0; x < options[i].length; x++) {
                        currentArr[x] = options[x][i]; //speichert alle offene optionen (spaltenweises durchiterieren -> bsp alle optionen von reihe 0 und spalte 0 / alle optionen von reihe 1 und spalte 0)
                    }
                    for (int j = 0; j < currentArr.length; j++) {
                        if (currentArr[j].length == 9) {
                            List<Integer> numbers = new ArrayList<>(); // alle optionen werden in list gespeichert
                            for (int k = 0; k < currentArr[j].length; k++) {
                                if (currentArr[j][k] != 0) {
                                    numbers.add(currentArr[j][k]);//mögliche Optionen werden in arrayList gespeichert
                                }
                            }
                            for (int a = 0; a < numbers.size(); a++) {
                                int counter = 0;
                                for (int k = 0; k < currentArr.length; k++) {
                                    List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList()); //selbe wie oben -> erklärung siehe oben
                                    if (temp.contains(numbers.get(a))) {
                                        counter++;
                                    }
                                }
                                if (counter == 1) {//Erklärung siehe oben
                                    options[j][i] = new int[]{numbers.get(a)};
                                    change = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                row = 0; //9erblockweises durchiterieren -> erklärung siehe oben genau
                column = 0;
                while (row != 9) {
                    int tempCounter = 0;
                    int[][] currentArr = new int[options.length][];
                    for (int i = row; i < row + 3; i++) {                       //durchiterieren von 9erBlöcken
                        for (int j = column; j < column + 3; j++) {
                            currentArr[tempCounter] = options[i][j];
                            tempCounter++;
                        }
                    }

                    //weitere Zeilen sind genau gleich wie oben -> siehe Erklärung
                    for (int j = 0; j < currentArr.length; j++) {
                        if (currentArr[j].length == 9) {
                            List<Integer> numbers = new ArrayList<>();
                            for (int k = 0; k < currentArr[j].length; k++) {
                                if (currentArr[j][k] != 0) {
                                    numbers.add(currentArr[j][k]);
                                }
                            }
                            for (int a = 0; a < numbers.size(); a++) {
                                int counter = 0;
                                for (int k = 0; k < currentArr.length; k++) {
                                    List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList());
                                    if (temp.contains(numbers.get(a))) {
                                        counter++;
                                    }
                                }
                                if (counter == 1) {
                                    int rawIndex = (j / 3) + row; //richtige Reihe finden
                                    int columnIndex = (j % 3) + column;//richtige Spalte finden
                                    options[rawIndex][columnIndex] = new int[]{numbers.get(a)}; //löschen von anderen optionen
                                    change = true;
                                    break;
                                }
                            }
                        }
                    }
                    column += 3; //spalte wird erhöht
                    if (column == 9) {//wenn ende der Tabelle angelangt wurde
                        column = 0;
                        row += 3;
                    }
                }
            }
            change = true;
            for (int i = 0; i < options.length; i++) {//Überprüfung ob options schon fertig ist
                for (int j = 0; j < options[i].length; j++) {
                    if (options[i][j].length == 9) {//schaut ob noch irgendwo mehr als eine Option ist
                        change = false;
                        break;
                    }
                }
                if (!change) {
                    break;
                }
            }
        }

        int[][] ergebnis = Arrays.stream(options).map(arr -> Arrays.stream(arr).mapToInt(i -> i[0]).toArray()).toArray(int[][]::new); //wandelt 3D array in 2D array
        return ergebnis;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        int[][][] options = new int[9][9][9];
        ExecutorService executor;

        for (int i = 0; i < rawSudoku.length; i++) { //wie oben options wird mit allen möglichen zahlen befüllt
            for (int j = 0; j < rawSudoku[i].length; j++) {
                if (rawSudoku[i][j] == 0) {
                    options[i][j] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
                } else {
                    options[i][j] = new int[]{rawSudoku[i][j]};
                }
            }
        }

        Runnable deleteRows = () -> {//Funtion wie oben
            for (int i = 0; i < options.length; i++) {
                int[][] numbers = Arrays.stream(options[i]).filter(arr -> arr.length == 1).toArray(int[][]::new);
                for (int j = 0; j < numbers.length; j++) {
                    int index = numbers[j][0] - 1;
                    Arrays.stream(options[i]).forEach(arr -> {
                        if (arr.length == 9) {
                            arr[index] = 0;
                        }
                    });
                }
            }
        };

        Runnable deleteColumn = () -> {//Funktion wie oben
            for (int i = 0; i < options.length; i++) {
                int[][] columnArr = new int[9][];
                for (int j = 0; j < options.length; j++) {
                    columnArr[j] = options[j][i];
                }
                int[][] numbers = Arrays.stream(columnArr).filter(arr -> arr.length == 1).toArray(int[][]::new);
                for (int j = 0; j < numbers.length; j++) {
                    int index = numbers[j][0] - 1;
                    Arrays.stream(columnArr).forEach(arr -> {
                        if (arr.length == 9) {
                            arr[index] = 0;
                        }
                    });
                }
                for (int j = 0; j < columnArr.length; j++) {
                    options[j][i] = columnArr[j];
                }
            }
        };

        Runnable deleteBoxes = () -> {//Funktion wie oben
            int indexRaw = 0;
            int indexColumn = 0;
            while (indexRaw != 9) {
                for (int i = indexRaw; i < indexRaw + 3; i++) {
                    for (int j = indexColumn; j < indexColumn + 3; j++) {
                        if (options[i][j].length == 1) {
                            for (int k = indexRaw; k < indexRaw + 3; k++) {
                                for (int l = indexColumn; l < indexColumn + 3; l++) {
                                    if (options[k][l].length != 1) {
                                        options[k][l][options[i][j][0] - 1] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                indexColumn += 3;
                if (indexColumn == 9) {
                    indexColumn = 0;
                    indexRaw += 3;
                }
            }
        };

        Runnable checkRows = () -> {//Funktion wie oben
            for (int i = 0; i < options.length; i++) {
                int[][] currentArr = options[i];
                for (int j = 0; j < currentArr.length; j++) {
                    if (currentArr[j].length == 9) {
                        List<Integer> numbers = new ArrayList<>();
                        for (int k = 0; k < currentArr[j].length; k++) {
                            if (currentArr[j][k] != 0) {
                                numbers.add(currentArr[j][k]);
                            }
                        }
                        for (int a = 0; a < numbers.size(); a++) {
                            int counter = 0;
                            for (int k = 0; k < currentArr.length; k++) {
                                List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList());
                                if (temp.contains(numbers.get(a))) {
                                    counter++;
                                }
                            }
                            if (counter == 1) {
                                options[i][j] = new int[]{numbers.get(a)};
                                this.changed = true;
                                break;
                            }
                        }
                    }
                }
            }
        };

        Runnable checkColumns = () -> {//Funktion wie oben
            for (int i = 0; i < options.length; i++) {
                int[][] currentArr = new int[options[i].length][];
                for (int x = 0; x < options[i].length; x++) {
                    currentArr[x] = options[x][i];
                }
                for (int j = 0; j < currentArr.length; j++) {
                    if (currentArr[j].length == 9) {
                        List<Integer> numbers = new ArrayList<>();
                        for (int k = 0; k < currentArr[j].length; k++) {
                            if (currentArr[j][k] != 0) {
                                numbers.add(currentArr[j][k]);
                            }
                        }
                        for (int a = 0; a < numbers.size(); a++) {
                            int counter = 0;
                            for (int k = 0; k < currentArr.length; k++) {
                                List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList());
                                if (temp.contains(numbers.get(a))) {
                                    counter++;
                                }
                            }
                            if (counter == 1) {
                                options[j][i] = new int[]{numbers.get(a)};
                                this.changed = true;
                                break;
                            }
                        }
                    }
                }
            }
        };

        Runnable checkBoxes = () -> {//Funktion wie oben
            int indexRaw = 0;
            int indexColumn = 0;
            while (indexRaw != 9) {
                int tempCounter = 0;
                int[][] currentArr = new int[options.length][];
                for (int i = indexRaw; i < indexRaw + 3; i++) {
                    for (int j = indexColumn; j < indexColumn + 3; j++) {
                        currentArr[tempCounter] = options[i][j];
                        tempCounter++;
                    }
                }
                for (int j = 0; j < currentArr.length; j++) {
                    if (currentArr[j].length == 9) {
                        List<Integer> numbers = new ArrayList<>();
                        for (int k = 0; k < currentArr[j].length; k++) {
                            if (currentArr[j][k] != 0) {
                                numbers.add(currentArr[j][k]);
                            }
                        }
                        for (int a = 0; a < numbers.size(); a++) {
                            int counter = 0;
                            for (int k = 0; k < currentArr.length; k++) {
                                List<Integer> temp = Arrays.stream(currentArr[k]).boxed().collect(Collectors.toList());
                                if (temp.contains(numbers.get(a))) {
                                    counter++;
                                }
                            }
                            if (counter == 1) {
                                int rawIndex = (j / 3) + indexRaw;
                                int columnIndex = (j % 3) + indexColumn;
                                options[rawIndex][columnIndex] = new int[]{numbers.get(a)};
                                this.changed = true;
                                break;
                            }
                        }
                    }
                }
                indexColumn += 3;
                if (indexColumn == 9) {
                    indexColumn = 0;
                    indexRaw += 3;
                }
            }
        };

        while (!(this.changed)) {
            executor = Executors.newCachedThreadPool();
            executor.execute(deleteRows);
            executor.execute(deleteColumn);
            executor.execute(deleteBoxes);
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.HOURS);//wartet bis alle fertig sind
            } catch (InterruptedException ex) {
                Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
            executor = Executors.newCachedThreadPool();
            this.changed = true;
            while (this.changed) {
                executor = Executors.newCachedThreadPool();
                this.changed = false;
                executor.execute(checkRows);
                executor.execute(checkColumns);
                executor.execute(checkBoxes);
                executor.shutdown();
                try {
                    executor.awaitTermination(1, TimeUnit.HOURS);//wartet bis alle fertig sind
                } catch (InterruptedException ex) {
                    Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.changed = true;//wie oben -> überprüft ob sudoku schon fertig ist
            for (int i = 0; i < options.length; i++) {
                for (int j = 0; j < options[i].length; j++) {
                    if (options[i][j].length == 9) {
                        this.changed = false;
                        break;
                    }
                }
                if (!(this.changed)) {
                    break;
                }
            }
        }

        int[][] sol = Arrays.stream(options).map(arr -> Arrays.stream(arr).mapToInt(i -> i[0]).toArray()).toArray(int[][]::new);
        return sol;
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

}
